package com.ren.catquest.job.dto.request;

import java.time.LocalDate;
import java.util.List;

public record JobSearchRequest(

        String keyword,

        List<String> companyNames,
        List<String> locationNames,

        SalaryRangeRequest salary,

        List<String> jobTypes,
        List<String> experienceLevels,
        List<String> sources,

        Boolean active,

        LocalDate postedAfter,
        LocalDate postedBefore,

        Integer page,
        Integer size,

        List<JobSortRequest> sorts
) {}