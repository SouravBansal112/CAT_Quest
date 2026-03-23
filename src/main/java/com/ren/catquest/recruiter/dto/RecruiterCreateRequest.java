package com.ren.catquest.recruiter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecruiterCreateRequest(
        @NotNull
        Long companyId,

        @NotNull
        Long cityId,

        @NotBlank
        @Size(max = 100)
        String designation,

        @NotBlank
        @Size(max = 100)
        String industry,

        @NotBlank
        @Size(max = 1000)
        String companyDescription
) {}
