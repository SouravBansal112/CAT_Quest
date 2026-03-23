package com.ren.catquest.job.service;

import com.ren.catquest.common.exception.ForbiddenException;
import com.ren.catquest.common.exception.ResourceNotFoundException;
import com.ren.catquest.job.dto.request.JobCreateRequest;
import com.ren.catquest.job.dto.request.JobUpdateRequest;
import com.ren.catquest.job.entity.Company;
import com.ren.catquest.job.entity.Job;
import com.ren.catquest.job.entity.Location;
import com.ren.catquest.job.repository.JobRepository;
import com.ren.catquest.job.repository.LocationRepository;
import com.ren.catquest.recruiter.entity.RecruiterProfile;
import com.ren.catquest.recruiter.repository.RecruiterProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class JobService {
    private final JobRepository jobRepository;
    private final LocationRepository locationRepository;
    private final RecruiterProfileRepository recruiterRepo;

    public Job createJob(JobCreateRequest request, Long userId){

        RecruiterProfile recruiter = recruiterRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        Company company = recruiter.getCompany();

        Location location = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        Job job = Job.createJob(
                request.title(),
                request.description(),
                company,
                location,
                request.type(),
                request.salaryMin(),
                request.salaryMax()
        );

        if(request.openings() != null)
            job.updateOpenings(request.openings());

        if(request.experienceLevel() != null)
            job.setExperienceYears(request.experienceLevel());

        if(request.deadline() != null)
            job.setExpiresAt(request.deadline());

        return jobRepository.save(job);
    }

    public void updateJob(JobUpdateRequest request, Long userId) {

        RecruiterProfile recruiter = recruiterRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        Job job = jobRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if(job.getPostedBy()==null || !recruiter.getId().equals(job.getPostedBy().getId())){
            throw new ForbiddenException("You are not allowed to modify this job");
        }


        if(request.experienceLevel() != null)
            job.setExperienceYears(request.experienceLevel());


        if(request.salaryMin() != null || request.salaryMax() != null)
            job.updateSalary(request.salaryMin(), request.salaryMax());

        if(request.openings() != null)
            job.updateOpenings(request.openings());

        if(request.deadline() != null)
            job.extendDeadline(request.deadline());

        if(request.active() != null) {
            if(request.active())
                job.reopenJob();
            else
                job.closeJob();
        }
    }

    //delete
    public void deleteJob(Long jobId, Long userId) {

        RecruiterProfile recruiter = recruiterRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if(job.getPostedBy()==null || !recruiter.getId().equals(job.getPostedBy().getId())){
            throw new ForbiddenException("You are not allowed to modify this job");
        }

        jobRepository.delete(job); // triggers soft delete
    }

}
