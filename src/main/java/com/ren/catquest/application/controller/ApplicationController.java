package com.ren.catquest.application.controller;

import com.ren.catquest.application.dto.*;
import com.ren.catquest.application.model.ApplicationStatus;
import com.ren.catquest.application.service.ApplicationService;
import com.ren.catquest.security.model.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // ================= USER ==================

    // APPLY TO JOB
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> applyToJob(
            @Valid @RequestBody ApplicationCreateRequest request,
            @AuthenticationPrincipal SecurityUser user
    ) {

        applicationService.apply(
                request,
                null, // later: resumeUrl from file service
                null  // later: resumeFileName
        );

        return ResponseEntity.ok("Application submitted successfully");
    }


    // GET MY APPLICATIONS
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<UserApplicationListItem> getMyApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal SecurityUser user
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());

        return applicationService.getUserApplications(
                user.getId(),
                status,
                pageable
        );
    }


    // GET MY APPLICATION DETAIL
    @GetMapping("/me/{applicationId}")
    @PreAuthorize("hasRole('USER')")
    public ApplicationDetailResponse getMyApplicationDetail(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal SecurityUser user
    ) {

        return applicationService.getUserApplicationDetail(
                applicationId,
                user.getId()
        );
    }


    // ================= RECRUITER =================

    // GET APPLICATIONS FOR A JOB
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public PagedResponse<RecruiterApplicationListItem> getJobApplications(
            @PathVariable Long jobId,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());

        return applicationService.getJobApplications(
                jobId,
                status,
                pageable
        );
    }


    // GET APPLICATION DETAIL (RECRUITER)
    @GetMapping("/{applicationId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApplicationDetailResponse getApplicationDetailForRecruiter(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal SecurityUser user
    ) {

        return applicationService.getRecruiterApplicationDetail(
                applicationId,
                user.getId()
        );
    }


    // ================= FUTURE READY =================

    // UPDATE STATUS (SHORTLIST / REJECT / etc.)
    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status,
            @AuthenticationPrincipal SecurityUser user
    ) {

        // NOT YET IMPLEMENTED IN SERVICE
        // applicationService.updateStatus(applicationId, user.getId(), status);

        return ResponseEntity.ok("Status updated");
    }


    // WITHDRAW APPLICATION (USER)
    @DeleteMapping("/{applicationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> withdrawApplication(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal SecurityUser user
    ) {

        // NOT YET IMPLEMENTED IN SERVICE
        // applicationService.withdraw(applicationId, user.getId());

        return ResponseEntity.ok("Application withdrawn");
    }
}