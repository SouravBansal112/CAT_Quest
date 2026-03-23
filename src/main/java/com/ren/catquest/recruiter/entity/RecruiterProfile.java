package com.ren.catquest.recruiter.entity;

import com.ren.catquest.job.entity.Company;
import com.ren.catquest.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;

@Entity
@Table(name = "recruiter_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruiterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1:1 with User
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(length = 100)
    private String designation;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String location;

    @Column(length = 1000)
    private String companyDescription;

    // verification
    @Column(nullable = false)
    private boolean verified = false;

    // audit
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    /* DOMAIN METHODS */

    public static RecruiterProfile create(User user,
                                          Company company,
                                          String designation,
                                          String industry,
                                          String location,
                                          String companyDescription) {

        if (user == null) throw new IllegalArgumentException("User required");

        RecruiterProfile rp = new RecruiterProfile();
        rp.user = user;
        rp.company = company;
        rp.designation = normalize(designation);
        rp.industry = normalize(industry);
        rp.location = normalize(location);
        rp.companyDescription = normalize(companyDescription);
        rp.verified = false;

        return rp;
    }

    public void verify() {
        this.verified = true;
    }

    public void updateCompanyDetails(Company company,
                                     String industry,
                                     String location,
                                     String description) {

        this.company = company;
        this.industry = normalize(industry);
        this.location = normalize(location);
        this.companyDescription = normalize(description);
    }

    private static String normalize(String val) {
        return (val == null || val.isBlank()) ? null : val.trim();
    }
}