package com.ren.CatQuest.scraper;

import com.ren.CatQuest.job.entity.Job;
import com.ren.CatQuest.job.service.JobService;
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
        List<Job> jobs = generator.generateJobs(100);  // Generate or scrape
        jobService.saveJobs(jobs);                   // Save via service layer
    }
}

//package com.ren.CatQuest.scraper;
//
//import com.ren.CatQuest.job.entity.Job;
//import com.ren.CatQuest.scraper.impl.*;
//import com.ren.CatQuest.job.service.JobService;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class JobScraper {
//
//    private final Generator generator;
//    private final JobService jobService;
//    private final NaukriScraper naukriScraper;
////    private final InternshalaS craper internshalaS craper;
////    private final LinkedInScraper linkedInScraper;
//
//    public JobScraper(Generator generator, JobService jobService,
//                      NaukriScraper naukriScraper) {
//        this.generator = generator;
//        this.jobService = jobService;
//        this.naukriScraper = naukriScraper;
//    }
//
//    // Keep for backward compatibility
//    public void scrapeAndSave() {
//        List<Job> jobs = generator.generateJobs(10);
//        jobService.saveJobs(jobs);
//    }
//
//    // NEW: Real scraping from all sources
//    public void scrapeAllSources(String keyword, String location, int maxPages) {
//        List<Job> allJobs = new ArrayList<>();
//
//        System.out.println("Starting scrape for: " + keyword + " in " + location);
//
//        // Naukri
//        try {
//            List<Job> naukriJobs = naukriScraper.scrapeJobs(keyword, location, maxPages);
//            allJobs.addAll(naukriJobs);
//            System.out.println("Scraped " + naukriJobs.size() + " jobs from Naukri");
//        } catch (Exception e) {
//            System.err.println("Naukri scraping failed: " + e.getMessage());
//        }
//
//
//
//        // Save all jobs
//        jobService.saveJobs(allJobs);
//        System.out.println("Total jobs saved: " + allJobs.size());
//    }
//
//    // Scrape from single source
//    public void scrapeSingleSource(String source, String keyword, String location, int maxPages) {
//        List<Job> jobs = new ArrayList<>();
//
//        switch (source.toLowerCase()) {
//            case "naukri":
//                jobs = naukriScraper.scrapeJobs(keyword, location, maxPages);
//                break;
//            default:
//                System.err.println("Unknown source: " + source);
//                return;
//        }
//
//        jobService.saveJobs(jobs);
//        System.out.println("Scraped and saved " + jobs.size() + " jobs from " + source);
//    }
//}


