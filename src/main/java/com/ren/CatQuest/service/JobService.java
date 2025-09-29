package com.ren.CatQuest.service;

import com.ren.CatQuest.model.Company;
import com.ren.CatQuest.model.Job;
import com.ren.CatQuest.model.Location;
import com.ren.CatQuest.repository.CompanyRepository;
import com.ren.CatQuest.repository.JobRepository;
import com.ren.CatQuest.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


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
