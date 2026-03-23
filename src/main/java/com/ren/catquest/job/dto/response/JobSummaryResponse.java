package com.ren.catquest.job.dto.response;

import com.ren.catquest.job.entity.JobType;

import java.time.LocalDateTime;

public record JobSummaryResponse(

        Long id,
        String title,

        String companyName,

        String locationName,

        Integer salaryMin,
        Integer salaryMax,

        JobType jobType,
        Integer experienceLevel,

        String source

) {}
