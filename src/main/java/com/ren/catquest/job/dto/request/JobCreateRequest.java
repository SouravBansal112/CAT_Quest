package com.ren.catquest.job.dto.request;

import com.ren.catquest.job.entity.Company;
import com.ren.catquest.job.entity.JobType;
import com.ren.catquest.job.entity.Location;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.time.LocalDate;

public record JobCreateRequest(

        @NotBlank
        @Size(max = 150)
        String title,

        @NotBlank
        @Size(max = 8000)
        String description,

        @NotNull
        Long locationId,


        Integer salaryMin,
        Integer salaryMax,

        @NotBlank
        JobType type,

        Integer openings,

        Integer experienceLevel,

        Instant deadline
) {}