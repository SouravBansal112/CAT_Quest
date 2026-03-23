package com.ren.catquest.job.service;


import com.ren.catquest.job.dto.request.JobSearchRequest;
import com.ren.catquest.job.dto.request.JobSortRequest;
import com.ren.catquest.job.dto.response.JobDetailResponse;
import com.ren.catquest.job.dto.response.JobSummaryResponse;
import com.ren.catquest.job.dto.response.PagedResponse;
import com.ren.catquest.job.entity.Job;
import com.ren.catquest.job.mapper.JobMapper;
import com.ren.catquest.job.repository.CompanyRepository;
import com.ren.catquest.job.repository.JobRepository;
import com.ren.catquest.job.repository.LocationRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class JobQueryService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    private final JobMapper jobMapper;

    public PagedResponse<JobSummaryResponse> search(JobSearchRequest request){
        List<Long> companyIds= resolveCompanyIds(request);
        List<Long> locationIds= resolveLocationIds(request);

        Specification<Job> spec = buildSpecification(request, companyIds, locationIds);

        Pageable pageable = buildPageable(request);

        Page<Job> pageResult = jobRepository.findAll(spec, pageable);

        List<JobSummaryResponse> content = pageResult.getContent()
                .stream()
                .map(jobMapper::toSummary)
                .toList();

        return new PagedResponse<>(
            content,
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages(),
            pageResult.isLast()
        );
    }

    public JobDetailResponse getJobDetails(Long id){
        Job job = jobRepository.findById(id)
                .orElseThrow();
        return jobMapper.toDetail(job);
    }

    private Specification<Job> buildSpecification(JobSearchRequest request,
                                                  List<Long> companyIds,
                                                  List<Long> locationIds)
    {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // keyword search
            if (request.keyword() != null && !request.keyword().isBlank()) {
                String pattern = "%" + request.keyword() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("title")), pattern),
                                cb.like(cb.lower(root.get("description")), pattern)
                        )
                );
            }

            //Company filter
            if(!companyIds.isEmpty()){
                predicates.add( root.get("company").get("id").in(companyIds));
            }

            //location filter
            if(!locationIds.isEmpty()){
                predicates.add( root.get("location").get("id").in(locationIds));
            }

            //Salary range
            if(request.salary() != null){
                Long min = request.salary().min();
                Long max = request.salary().max();

                if (min != null) {
                    predicates.add(cb.greaterThanOrEqualTo(
                            root.get("salaryMax"), min));
                }

                if (max != null) {
                    predicates.add(cb.lessThanOrEqualTo(
                            root.get("salaryMin"), max));
                }
            }

            //date filter
            if(request.postedAfter() != null){
                Instant start = request.postedAfter()
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant();
                predicates.add( cb.greaterThanOrEqualTo(
                        root.get("createdAt"), start));
            }
            if (request.postedBefore() != null) {
                Instant end = request.postedBefore()
                        .plusDays(1)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .minusNanos(1);
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("createdAt"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(JobSearchRequest request){
        int page = request.page()!=null?request.page():0;
        int size = request.size()!=null?request.size():20;

        List<Sort.Order> orders = new ArrayList<>();

        if(request.sorts()!=null){
            for( JobSortRequest sort : request.sorts() ){
                String safeField = mapToSafeField(sort.field());
                Sort.Direction direction = Sort.Direction.fromString(sort.direction());
                orders.add( new Sort.Order( direction, safeField));
            }
        }
        Sort sort = orders.isEmpty()
                ? Sort.by( Sort.Direction.DESC, "createdAt")
                : Sort.by(orders);

        return PageRequest.of(page, size, sort);
    }

    private String mapToSafeField(String field) {
        return switch (field) {
            case "salaryMin" -> "salaryMin";
            case "salaryMax" -> "salaryMax";
            case "createdAt" -> "createdAt";
            case "title" -> "title";
            default -> throw new IllegalArgumentException("Invalid sort field");
        };
    }

    private List<Long> resolveCompanyIds(JobSearchRequest request){

        if(request.companyNames() == null || request.companyNames().isEmpty())
            return List.of();

        return companyRepository.findIdsByNameIn(request.companyNames());
    }

    private List<Long> resolveLocationIds(JobSearchRequest request){

        if(request.locationNames() == null || request.locationNames().isEmpty())
            return List.of();

        return locationRepository.findIdsByNameIn(request.locationNames());
    }

    public PagedResponse<JobSummaryResponse> getJobsByRecruiter(Long recruiterId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Job> pageResult = jobRepository.findAllByPostedById(recruiterId, pageable);

        List<JobSummaryResponse> content = pageResult.getContent()
                .stream()
                .map(jobMapper::toSummary)
                .toList();

        return new PagedResponse<>(
                content,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }

}
