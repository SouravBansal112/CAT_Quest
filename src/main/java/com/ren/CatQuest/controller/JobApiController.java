package com.ren.CatQuest.controller;

import com.ren.CatQuest.model.Job;
import com.ren.CatQuest.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class JobApiController {
    private final JobService jobService;

    public JobApiController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public List<Job> getJobs() {
        List<Job> jobs = jobService.getJobs();
        return jobs;
    }
}

