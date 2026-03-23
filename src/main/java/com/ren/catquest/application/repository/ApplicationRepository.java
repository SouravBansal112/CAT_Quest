package com.ren.catquest.application.repository;

import com.ren.catquest.application.model.Application;
import com.ren.catquest.application.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    //EXISTENCE CHECK
    boolean existsByApplicantIdAndJobId(Long applicantId, Long jobId);

    // FETCH JOIN OPTIMIZATION (avoid N+1 problem)

    @EntityGraph(
            attributePaths = {"job", "job.company"}
    )
    Page<Application> findWithJobByApplicantId(Long applicantId, Pageable pageable);

    @EntityGraph(
            attributePaths = {"applicant", "job"}
    )
    Page<Application> findWithApplicantByJobId(Long jobId, Pageable pageable);

    // USER + STATUS
    @EntityGraph(attributePaths = {"job", "job.company"})
    Page<Application> findWithJobByApplicantIdAndStatus(
            Long applicantId,
            ApplicationStatus status,
            Pageable pageable
    );


    // RECRUITER + STATUS
    @EntityGraph(attributePaths = {"applicant", "job"})
    Page<Application> findWithApplicantByJobIdAndStatus(
            Long jobId,
            ApplicationStatus status,
            Pageable pageable
    );


    // DETAIL FETCH
    @EntityGraph(attributePaths = {"job", "job.company", "applicant"})
    Optional<Application> findWithAllByIdAndApplicantId(Long id, Long applicantId);

    @EntityGraph(attributePaths = {"job", "job.company", "applicant"})
    Optional<Application> findWithAllById(Long id);

    @Modifying
    @Query("UPDATE Application a SET a.status = :status WHERE a.id IN :ids AND a.job.postedBy.id = :recruiterId")
    int updateStatusBulk(List<Long> ids, ApplicationStatus status, Long recruiterId);
}
