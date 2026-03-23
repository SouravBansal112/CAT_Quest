package com.ren.catquest.job.entity;

import com.ren.catquest.recruiter.entity.RecruiterProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;

@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE jobs SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruiter_id")
    private RecruiterProfile postedBy;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_max")
    private Integer salaryMax;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(name = "openings")
    private Integer openings;

    @Column(name="job_url")
    private String jobUrl;

    @Column(nullable=false)
    private String source = "Cat Quest";

    @Setter
    @Column
    private Integer experienceYears;

    @Setter
    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable=false)
    private Instant updatedAt;

    @Column
    private Instant deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
        deletedAt = null;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    public void closeJob() {
        this.active = false;
    }

    public void reopenJob() {

        if(expiresAt != null && expiresAt.isBefore(Instant.now()))
            throw new IllegalStateException("cannot reopen expired job");

        this.active = true;
    }

    public void updateSalary(Integer min, Integer max) {
        if(min != null && max != null && min > max)
            throw new IllegalArgumentException("Invalid salary range");

        this.salaryMin = min;
        this.salaryMax = max;
    }

    public void updateOpenings(Integer openings) {

        if(openings != null && openings < 0)
            throw new IllegalArgumentException("openings cannot be negative");

        this.openings = openings;
    }

    public void extendDeadline(Instant newExpiry) {

        if(newExpiry == null)
            throw new IllegalArgumentException("expiry cannot be null");

        if(expiresAt != null && newExpiry.isBefore(expiresAt))
            throw new IllegalArgumentException("cannot shorten deadline");

        this.expiresAt = newExpiry;
    }

    public static Job createJob(
            String title,
            String description,
            Company company,
            Location location,
            JobType type,
            Integer salaryMin,
            Integer salaryMax
    ) {
        Job job = new Job();

        job.title=title;
        job.description = description;
        job.company = company;
        job.location = location;
        job.type = type;
        job.salaryMin = salaryMin;
        job.salaryMax = salaryMax;

        return job;
    }

}
