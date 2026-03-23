package com.ren.catquest.job.repository;

import com.ren.catquest.job.entity.Job;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    @EntityGraph(attributePaths = {"company", "location"})
    Page<Job> findAllByPostedById(Long recruiterId, Pageable pageable);

    List<Job> findByPostedById(Long userId);

    @Modifying
    @Query("UPDATE Job j SET j.deletedAt = CURRENT_TIMESTAMP WHERE j.postedBy.id = :recruiterId")
    void softDeleteByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Modifying
    @Query("UPDATE Job j SET j.postedBy = null WHERE j.postedBy.id = :recruiterId")
    void updatePostedByToNullByRecruiterId(Long recruiterId);
}
