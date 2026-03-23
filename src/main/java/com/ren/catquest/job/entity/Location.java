package com.ren.catquest.job.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name= "locations", uniqueConstraints ={
        @UniqueConstraint(columnNames = {"city", "state", "country"})
})
public class Location{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String state;

    @Column(nullable = false)
    String country;

}
