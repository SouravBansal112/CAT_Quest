package com.ren.CatQuest.scraper;

import com.ren.CatQuest.model.Job;
import com.ren.CatQuest.service.JobService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobScraper {

    private final Generator generator;
    private final JobService jobService;

    public JobScraper(Generator generator, JobService jobService) {
        this.generator = generator;
        this.jobService = jobService;
    }

    public void scrapeAndSave() {
        List<Job> jobs = generator.generateJobs(10);  // Generate or scrape
        jobService.saveJobs(jobs);                   // Save via service layer
    }
}

