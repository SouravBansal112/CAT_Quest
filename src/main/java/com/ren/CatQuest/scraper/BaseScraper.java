package com.ren.CatQuest.scraper;

import com.ren.CatQuest.model.Job;
import java.util.List;

public interface BaseScraper {
    List<Job> scrapeJobs(String keyword, String location, int maxPages);
    String getSource();
}