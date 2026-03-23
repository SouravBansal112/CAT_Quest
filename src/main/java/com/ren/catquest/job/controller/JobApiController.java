package com.ren.catquest.job.controller;

import com.ren.catquest.job.dto.request.JobCreateRequest;
import com.ren.catquest.job.dto.request.JobSearchRequest;
import com.ren.catquest.job.dto.request.JobUpdateRequest;
import com.ren.catquest.job.dto.response.JobDetailResponse;
import com.ren.catquest.job.dto.response.JobSummaryResponse;
import com.ren.catquest.job.dto.response.PagedResponse;
import com.ren.catquest.job.entity.Job;
import com.ren.catquest.job.service.JobQueryService;
import com.ren.catquest.job.service.JobService;
import com.ren.catquest.security.model.SecurityUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApiController {

    private final JobService jobService;
    private final JobQueryService jobQueryService;

    //CREATE
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Long> createJob(
            @RequestBody @Valid JobCreateRequest request,
            @AuthenticationPrincipal SecurityUser user
    ) {
        Job job = jobService.createJob(request, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(job.getId());
    }

    // UPDATE
    @PutMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateJob(
            @RequestBody @Valid JobUpdateRequest request,
            @AuthenticationPrincipal SecurityUser user
    ) {
        jobService.updateJob(request, user.getId());
        return ResponseEntity.noContent().build();
    }

    // DELETE
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal SecurityUser user
    ) {
        jobService.deleteJob(jobId, user.getId());
        return ResponseEntity.noContent().build();
    }

    // SEARCH
    @PostMapping("/search")
    public ResponseEntity<PagedResponse<JobSummaryResponse>> searchJobs(
            @RequestBody @Valid JobSearchRequest request
    ) {
        PagedResponse<JobSummaryResponse> response = jobQueryService.search(request);
        return ResponseEntity.ok(response);
    }

    // GET DETAILS
    @GetMapping("/{jobId}")
    public ResponseEntity<JobDetailResponse> getJobDetails(
            @PathVariable Long jobId
    ) {
        JobDetailResponse job = jobQueryService.getJobDetails(jobId);
        return ResponseEntity.ok(job);
    }

    // GET JOBS BY RECRUITER
    @GetMapping("/my")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<PagedResponse<JobSummaryResponse>> getJobsByRecruiter(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ResponseEntity.ok(jobQueryService.getJobsByRecruiter(user.getId(), page, size));
    }
}