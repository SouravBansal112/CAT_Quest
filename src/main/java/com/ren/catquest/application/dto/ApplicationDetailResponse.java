package com.ren.catquest.application.dto;

import com.ren.catquest.application.model.ApplicationStatus;

import java.time.Instant;

public record ApplicationDetailResponse(

        Long applicationId,

        // job info
        Long jobId,
        String jobTitle,
        String companyName,

        // applicant info
        Long applicantId,
        String applicantName,
        String applicantEmail,
        String applicantPhone,

        // resume
        String resumeUrl,
        String resumeFileName,

        String coverLetter,

        ApplicationStatus status,

        Instant appliedAt
) {}