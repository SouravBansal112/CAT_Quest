package com.ren.catquest.job.repository;

import com.ren.catquest.job.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameAndWebsite(String name, String website);

    @Query("SELECT c.id FROM Company c WHERE c.name IN :names")
    List<Long> findIdsByNameIn(Collection<String> names);
}
