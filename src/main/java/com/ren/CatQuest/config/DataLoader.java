package com.ren.CatQuest.config;

import com.ren.CatQuest.scraper.JobScraper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final JobScraper jobScraper;

    public DataLoader(JobScraper jobScraper) {
        this.jobScraper = jobScraper;
    }

    @Override
    public void run(String... args) throws Exception {
        // This will generate and save dummy jobs at startup
        jobScraper.scrapeAndSave();

        System.out.println("Jobs have been generated and saved to DB!");
    }

}
