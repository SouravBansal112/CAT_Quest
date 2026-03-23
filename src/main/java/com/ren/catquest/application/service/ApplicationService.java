package com.ren.catquest.application.service;

import com.ren.catquest.application.dto.*;
import com.ren.catquest.application.model.Application;
import com.ren.catquest.application.model.ApplicationStatus;
import com.ren.catquest.application.repository.ApplicationRepository;
import com.ren.catquest.job.entity.Job;
import com.ren.catquest.job.repository.JobRepository;
import com.ren.catquest.user.entity.User;
import com.ren.catquest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // APPLY TO job
    @Transactional
    public void apply(ApplicationCreateRequest request, String resumeUrl, String resumeFileName) {

        // 1. Fetch user & job
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 2. Prevent duplicate application
        boolean exists = applicationRepository
                .existsByApplicantIdAndJobId(user.getId(), job.getId());

        if (exists) {
            throw new RuntimeException("You have already applied to this job");
        }

        if (job.getExpiresAt() != null && job.getExpiresAt().isBefore(java.time.Instant.now())) {
            throw new RuntimeException("Cannot apply: job is expired");
        }

        // 3. Create entity
        Application application = Application.create(
                job,
                user,
                request.applicantName(),
                request.applicantEmail(),
                request.applicantPhone(),
                resumeUrl,
                resumeFileName,
                request.coverLetter()
        );

        // 4. Save
        applicationRepository.save(application);
    }

    // BULK UPDATE STATUS FOR RECRUITER
    @Transactional
    public int bulkUpdateStatus(Long recruiterId, List<Long> applicationIds, ApplicationStatus status) {
        // Only allow updating applications for jobs posted by this recruiter
        return applicationRepository.updateStatusBulk(applicationIds, status, recruiterId);
    }

    // USER: GET APPLICATIONS
    @Transactional(readOnly = true)
    public PagedResponse<UserApplicationListItem> getUserApplications(
            Long userId,
            ApplicationStatus status,
            Pageable pageable
    ) {

        Page<Application> page;

        if (status != null) {
            page = applicationRepository.findWithJobByApplicantIdAndStatus(userId, status, pageable);
        } else {
            page = applicationRepository.findWithJobByApplicantId(userId, pageable);
        }

        return mapToUserPagedResponse(page);
    }


    // USER: GET APPLICATION DETAIL
    @Transactional(readOnly = true)
    public ApplicationDetailResponse getUserApplicationDetail(
            Long applicationId,
            Long userId
    ) {

        Application app = applicationRepository
                .findWithAllByIdAndApplicantId(applicationId, userId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return mapToDetailResponse(app);
    }


    // RECRUITER: GET APPLICATIONS FOR A JOB
    @Transactional(readOnly = true)
    public PagedResponse<RecruiterApplicationListItem> getJobApplications(
            Long jobId,
            ApplicationStatus status,
            Pageable pageable
    ) {

        Page<Application> page;

        if (status != null) {
            page = applicationRepository.findWithApplicantByJobIdAndStatus(jobId, status, pageable);
        } else {
            page = applicationRepository.findWithApplicantByJobId(jobId, pageable);
        }

        return mapToRecruiterPagedResponse(page);
    }

    // RECRUITER : GET APPLICATION DETAIL
    @Transactional(readOnly = true)
    public ApplicationDetailResponse getRecruiterApplicationDetail(
            Long applicationId,
            Long recruiterId
    ) {

        Application app = applicationRepository
                .findWithAllById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // AUTH CHECK
        if (!app.getJob().getPostedBy().getId().equals(recruiterId)) {
            throw new RuntimeException("Access denied");
        }

        return mapToDetailResponse(app);
    }


    // ============MAPPERS==============

    private PagedResponse<UserApplicationListItem> mapToUserPagedResponse(Page<Application> page) {

        Page<UserApplicationListItem> mapped = page.map(app ->
                new UserApplicationListItem(
                        app.getId(),
                        app.getJob().getId(),
                        app.getJob().getTitle(),
                        app.getJob().getCompany().getName(),
                        app.getStatus(),
                        app.getAppliedAt()
                )
        );

        return buildPagedResponse(mapped);
    }


    private PagedResponse<RecruiterApplicationListItem> mapToRecruiterPagedResponse(Page<Application> page) {

        Page<RecruiterApplicationListItem> mapped = page.map(app ->
                new RecruiterApplicationListItem(
                        app.getId(),
                        app.getJob().getId(),
                        app.getApplicant().getId(),
                        app.getApplicantName(),
                        app.getApplicantEmail(),
                        app.getStatus(),
                        app.getAppliedAt()
                )
        );

        return buildPagedResponse(mapped);
    }


    private ApplicationDetailResponse mapToDetailResponse(Application app) {
        return new ApplicationDetailResponse(
                app.getId(),

                app.getJob().getId(),
                app.getJob().getTitle(),
                app.getJob().getCompany().getName(),

                app.getApplicant().getId(),
                app.getApplicantName(),
                app.getApplicantEmail(),
                app.getApplicantPhone(),

                app.getResumeUrl(),
                app.getResumeFileName(),

                app.getCoverLetter(),

                app.getStatus(),
                app.getAppliedAt()
        );
    }


    private <T> PagedResponse<T> buildPagedResponse(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}