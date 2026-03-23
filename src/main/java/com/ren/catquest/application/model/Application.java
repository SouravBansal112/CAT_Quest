package com.ren.catquest.application.model;

import com.ren.catquest.job.entity.Job;
import com.ren.catquest.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_user_job",
                        columnNames = {"user_id", "job_id"}
                )
        },
        indexes = {
                @Index(name = "idx_application_job", columnList = "job_id"),
                @Index(name = "idx_application_user", columnList = "user_id"),
                @Index(name = "idx_application_status", columnList = "status"),
                @Index(name = "idx_application_applied_at", columnList = "applied_at")
        }
)
@Getter
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User applicant;

    @Column(name = "applicant_name", nullable = false, length = 100)
    private String applicantName;

    @Column(name = "applicant_email", nullable = false, length = 150)
    private String applicantEmail;

    @Column(name = "applicant_phone", nullable = false, length = 20)
    private String applicantPhone;

    @Column(name = "resume_url", nullable = false, length = 500)
    private String resumeUrl;

    @Column(name = "resume_file_name", length = 255)
    private String resumeFileName;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    // STATUS

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status;


    // AUDIT FIELDS

    @Column(name = "applied_at", nullable = false, updatable = false)
    private Instant appliedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // LIFECYCLE HOOKS

    @PrePersist
    protected void onCreate() {
        this.appliedAt = Instant.now();
        this.status = ApplicationStatus.APPLIED;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public static Application create(
            Job job,
            User applicant,
            String applicantName,
            String applicantEmail,
            String applicantPhone,
            String resumeUrl,
            String resumeFileName,
            String coverLetter
    ) {
        Application application = new Application();

        application.job = job;
        application.applicant = applicant;

        application.applicantName = applicantName;
        application.applicantEmail = applicantEmail;
        application.applicantPhone = applicantPhone;

        application.resumeUrl = resumeUrl;
        application.resumeFileName = resumeFileName;

        application.coverLetter = coverLetter;

        return application;
    }

}