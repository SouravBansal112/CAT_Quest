package com.ren.CatQuest.repository;

import com.ren.CatQuest.model.Company;
import com.ren.CatQuest.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameAndWebsite(String name, String website);

}
