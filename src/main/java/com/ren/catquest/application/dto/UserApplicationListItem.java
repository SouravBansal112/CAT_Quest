package com.ren.catquest.application.dto;

import com.ren.catquest.application.model.ApplicationStatus;

import java.time.Instant;

public record UserApplicationListItem(

        Long applicationId,

        Long jobId,
        String jobTitle,
        String companyName,

        ApplicationStatus status,
        Instant appliedAt
) {}