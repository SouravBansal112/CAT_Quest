package com.ren.CatQuest.repository;

import com.ren.CatQuest.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByCityAndStateAndCountry(String city, String state, String country);
}