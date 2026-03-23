package com.ren.catquest.application.dto;

import com.ren.catquest.application.model.ApplicationStatus;

import java.time.Instant;

public record RecruiterApplicationListItem(

        Long applicationId,

        Long jobId,

        Long applicantId,
        String applicantName,
        String applicantEmail,

        ApplicationStatus status,
        Instant appliedAt
) {}