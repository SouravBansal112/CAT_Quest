package com.ren.catquest.job.mapper;

import com.ren.catquest.job.dto.response.JobDetailResponse;
import com.ren.catquest.job.dto.response.JobSummaryResponse;
import com.ren.catquest.job.entity.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobSummaryResponse toSummary(Job job) {
        return new JobSummaryResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany().getName(),
                job.getLocation().getCity(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getType(),
                job.getExperienceYears(),
                job.getSource()
        );
    }

    public JobDetailResponse toDetail(Job job) {
        return new JobDetailResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompany().getName(),
                job.getLocation().getCity(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getType(),
                job.getExperienceYears(),
                job.getSource(),
                job.isActive(),
                job.getExpiresAt(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}
