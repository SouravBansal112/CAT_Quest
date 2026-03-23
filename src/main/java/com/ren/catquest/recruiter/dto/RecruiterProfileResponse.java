package com.ren.catquest.recruiter.dto;

import java.time.Instant;

public record RecruiterProfileResponse(
        Long id,
        Long userId,
        Long companyId,
        String companyName,
        Long cityId,
        String cityName,
        String stateName,
        String countryName,
        String designation,
        String industry,
        String companyDescription,
        boolean verified,
        Instant createdAt,
        Instant updatedAt
) {}