package com.ren.CatQuest.job.repository;

import com.ren.CatQuest.job.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameAndWebsite(String name, String website);

}
