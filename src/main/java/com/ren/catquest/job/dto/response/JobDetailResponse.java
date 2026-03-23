package com.ren.catquest.job.dto.response;

import com.ren.catquest.job.entity.JobType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record JobDetailResponse(

        Long id,
        String title,
        String description,

        String companyName,

        String locationName,

        Integer salaryMin,
        Integer salaryMax,

        JobType jobType,
        Integer experienceLevel,

        String source,

        Boolean active,

        Instant applicationDeadline,

        Instant createdAt,
        Instant updatedAt
) {}