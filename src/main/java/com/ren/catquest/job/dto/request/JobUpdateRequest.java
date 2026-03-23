package com.ren.catquest.job.dto.request;

import com.ren.catquest.job.entity.JobType;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.time.LocalDate;

public record JobUpdateRequest(

        @NotNull
        Long id,

        Integer salaryMin,
        Integer salaryMax,

        JobType jobType,
        Integer experienceLevel,
        Integer openings,

        @FutureOrPresent
        Instant deadline,

        Boolean active
) {}