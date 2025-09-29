package com.ren.CatQuest.scraper;

import com.ren.CatQuest.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Generator {

    private static final String[] TITLES = {
            "Software Engineer", "Backend Developer", "Frontend Developer",
            "Data Analyst", "DevOps Engineer", "Fullstack Developer"
    };

    private static final String[] COMPANIES = {
            "TechCorp", "InnoSoft", "CodeWorks", "DataHive", "CloudNine"
    };

    private static final String[] WEBSITES = {
            "techcorp.com", "innosoft.com", "codeworks.com", "datahive.com", "cloudnine.com"
    };

    private static final String[] CITIES = {"New York", "San Francisco", "Chicago", "Austin", "Seattle"};
    private static final String[] STATES = {"NY", "CA", "IL", "TX", "WA"};
    private static final String[] COUNTRIES = {"USA"};

    private static final JobType[] TYPES = JobType.values();

    private final Random random = new Random();

    public List<Job> generateJobs(int count) {
        List<Job> jobs = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            // Generate company
            int companyIndex = random.nextInt(COMPANIES.length);
            Company company = new Company();
            company.setName(COMPANIES[companyIndex]);
            company.setWebsite(WEBSITES[companyIndex]);

            // Generate location
            int locIndex = random.nextInt(CITIES.length);
            Location location = new Location();
            location.setCity(CITIES[locIndex]);
            location.setState(STATES[locIndex]);
            location.setCountry(COUNTRIES[0]);

            // Generate job
            Job job = new Job();
            job.setTitle(TITLES[random.nextInt(TITLES.length)]);
            job.setCompany(company);
            job.setLocation(location);
            job.setSalary(50000 + random.nextInt(100000)); // 50k–150k
            job.setType(TYPES[random.nextInt(TYPES.length)]);
            job.setJobUrl("https://example.com/jobs/" + i);
            job.setSource("generator");

            jobs.add(job);
        }

        return jobs;
    }
}

