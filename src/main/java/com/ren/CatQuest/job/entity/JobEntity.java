package com.ren.CatQuest.job.entity;

public class JobEntity {
    private Long id;
    private String title;
    private Long companyId;
    private Long locationId;
    private String description;
    private Integer salary;
    private JobType type;
    private String jobUrl;
    private String source;

    public JobEntity(Long id, String title, Long companyId, Long locationId,
                     String description, Integer salary, JobType type,
                     String jobUrl, String source) {
        this.id = id;
        this.title = title;
        this.companyId = companyId;
        this.locationId = locationId;
        this.description = description;
        this.salary = salary;
        this.type = type;
        this.jobUrl = jobUrl;
        this.source = source;
    }
}