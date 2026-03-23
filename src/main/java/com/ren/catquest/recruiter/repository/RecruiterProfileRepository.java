package com.ren.catquest.recruiter.repository;

import com.ren.catquest.job.entity.Company;
import com.ren.catquest.recruiter.entity.RecruiterProfile;
import com.ren.catquest.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {

    Optional<RecruiterProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);


    @EntityGraph( attributePaths = {"company"})
    Optional<RecruiterProfile> findWithCompanyByUserId(@Param("userId") Long userId);
}
