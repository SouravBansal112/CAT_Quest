package com.ren.CatQuest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "jobs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_url", "source"})
})
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable=false)
    private Location location;

    @Column(nullable = false)
    private int salary;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(name="job_url", nullable=false)
    private String jobUrl;

    @Column(nullable=false)
    private String source;

}
