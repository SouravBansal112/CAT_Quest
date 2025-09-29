package com.ren.CatQuest.repository;

import com.ren.CatQuest.model.Job;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO jobs (title, company_id, location_id, salary, type, job_url, source) " +
                    "VALUES (:title, :companyId, :locationId, :salary, :type, :jobUrl, :source) " +
                    "ON CONFLICT (job_url, source) DO NOTHING",
            nativeQuery = true
    )
    void upsertJob(String title, Long companyId, Long locationId, int salary, String type, String jobUrl, String source);

}
