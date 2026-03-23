package com.ren.catquest.job.repository;

import com.ren.catquest.job.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCityAndStateAndCountry(String city, String state, String country);

    @Query("SELECT l.id FROM Location l WHERE l.city IN :names")
    List<Long> findIdsByNameIn(@Param("names") Collection<String> names);
}