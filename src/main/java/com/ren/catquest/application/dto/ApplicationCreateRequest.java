package com.ren.catquest.application.dto;

import jakarta.validation.constraints.*;

public record ApplicationCreateRequest(

        @NotNull
        Long jobId,

        @NotNull
        Long userId,

        @NotBlank
        @Size(max = 100)
        String applicantName,

        @NotBlank
        @Email
        @Size(max = 150)
        String applicantEmail,

        @NotBlank
        @Size(max = 20)
        String applicantPhone,

        @Size(max = 2000)
        String coverLetter
) {}