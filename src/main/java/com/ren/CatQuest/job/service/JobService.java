//package com.ren.CatQuest.service;
//
//import com.ren.CatQuest.job.entity.Company;
//import com.ren.CatQuest.job.entity.Job;
//import com.ren.CatQuest.job.entity.Location;
//import com.ren.CatQuest.job.repository.CompanyRepository;
//import com.ren.CatQuest.job.repository.JobRepository;
//import com.ren.CatQuest.job.repository.LocationRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class JobService {
//    private final JobRepository jobRepository;
//    private final CompanyRepository companyRepository;
//    private final LocationRepository locationRepository;
//
//    public JobService(JobRepository jobRepository, CompanyRepository companyRepository, LocationRepository locationRepository) {
//        this.jobRepository = jobRepository;
//        this.companyRepository = companyRepository;
//        this.locationRepository = locationRepository;
//    }
//
//    public List<Job> getJobs() {
//        return jobRepository.findAll();
//    }
//
//
//    @Transactional
//    public void saveJobs(List<Job> jobs) {
//        for (Job job : jobs) {
//            // --- Handle Company ---
//            Company company = job.getCompany();
//            if (company != null) {
//                final Company tempCompany = company; // effectively final
//                company = companyRepository.findByNameAndWebsite(tempCompany.getName(), tempCompany.getWebsite())
//                        .orElseGet(() -> companyRepository.save(tempCompany));
//            }
//
//            // --- Handle Location ---
//            Location location = job.getLocation();
//            if (location != null) {
//                final Location tempLocation = location; // effectively final
//                location = locationRepository.findByCityAndStateAndCountry(
//                                tempLocation.getCity(), tempLocation.getState(), tempLocation.getCountry())
//                        .orElseGet(() -> locationRepository.save(tempLocation));
//            }
//
//            // --- Upsert Job ---
//            jobRepository.upsertJob(
//                    job.getTitle(),
//                    company != null ? company.getId() : null,
//                    location != null ? location.getId() : null,
//                    job.getSalary(),
//                    job.getType().toString(),
//                    job.getJobUrl(),
//                    job.getSource()
//            );
//        }
//    }
//
//
//    @Transactional
//    public void saveJob(Job job) {
//        saveJobs(List.of(job));
//    }
//
//}

package com.ren.CatQuest.job.service;

import com.ren.CatQuest.job.entity.Company;
import com.ren.CatQuest.job.entity.Job;
import com.ren.CatQuest.job.entity.Location;
import com.ren.CatQuest.job.repository.CompanyRepository;
import com.ren.CatQuest.job.repository.JobRepository;
import com.ren.CatQuest.job.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository, LocationRepository locationRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.locationRepository = locationRepository;
    }

    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    public List<Job> searchJobs(String keyword, String location, List<String> types, String sort) {
        List<Job> allJobs = jobRepository.findAll();

        // 1. Filter Logic
        List<Job> filteredJobs = allJobs.stream()
                .filter(job -> {
                    boolean matches = true;

                    // Filter by keyword (case-insensitive)
                    if (keyword != null && !keyword.isEmpty()) {
                        String jobText = job.getTitle() + " " + job.getCompany().getName();
                        if (!jobText.toLowerCase().contains(keyword.toLowerCase())) {
                            matches = false;
                        }
                    }

                    // Filter by location
                    if (location != null && !location.isEmpty()) {
                        String jobLocation = job.getLocation().getCity() + ", " + job.getLocation().getState();
                        if (!jobLocation.toLowerCase().contains(location.toLowerCase())) {
                            matches = false;
                        }
                    }

                    // Filter by job type
                    if (types != null && !types.isEmpty()) {
                        if (!types.contains(job.getType().toString().toUpperCase())) {
                            matches = false;
                        }
                    }

                    // Assuming 'remote' field exists on your Job model
                    // if (remote != null) {
                    //     if (remote && !job.isRemote()) {
                    //         matches = false;
                    //     }
                    // }

                    return matches;
                })
                .collect(Collectors.toList());

        // 2. Sorting Logic
        if ("relevance".equalsIgnoreCase(sort)) {
            // A simple relevance sort. You can enhance this.
            filteredJobs.sort(Comparator.comparing(Job::getTitle, Comparator.nullsLast(String::compareTo))
                    .reversed());
        } else { // Default to recent
            // Your Job model doesn't have a posted_at field. We'll sort by ID as a proxy for recency.
            filteredJobs.sort(Comparator.comparing(Job::getId, Comparator.nullsLast(Long::compareTo)).reversed());
        }

        return filteredJobs;
    }


    @Transactional
    public void saveJobs(List<Job> jobs) {
        for (Job job : jobs) {
            // --- Handle Company ---
            Company company = job.getCompany();
            if (company != null) {
                final Company tempCompany = company; // effectively final
                company = companyRepository.findByNameAndWebsite(tempCompany.getName(), tempCompany.getWebsite())
                        .orElseGet(() -> companyRepository.save(tempCompany));
            }

            // --- Handle Location ---
            Location location = job.getLocation();
            if (location != null) {
                final Location tempLocation = location; // effectively final
                location = locationRepository.findByCityAndStateAndCountry(
                                tempLocation.getCity(), tempLocation.getState(), tempLocation.getCountry())
                        .orElseGet(() -> locationRepository.save(tempLocation));
            }

            // --- Upsert Job ---
            jobRepository.upsertJob(
                    job.getTitle(),
                    company != null ? company.getId() : null,
                    location != null ? location.getId() : null,
                    job.getSalary(),
                    job.getType().toString(),
                    job.getJobUrl(),
                    job.getSource()
            );
        }
    }


    @Transactional
    public void saveJob(Job job) {
        saveJobs(List.of(job));
    }

}
