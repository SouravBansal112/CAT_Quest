package com.ren.catquest.job.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;

public record SalaryRangeRequest(
        @Min(0) Long min,
        @Min(0) Long max
) {
    @AssertTrue(message = "max salary must be >= min salary")
    public boolean isValid() {
        if (min == null || max == null) return true;
        return max >= min;
    }
}