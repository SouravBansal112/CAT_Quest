package com.ren.catquest.job.dto.request;

import jakarta.validation.constraints.NotBlank;

public record JobSortRequest(

        @NotBlank
        String field,        // salaryMin, salaryMax, createdAt, title

        @NotBlank
        String direction     // ASC or DESC
) {}