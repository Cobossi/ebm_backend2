package com.expert.beaute.mobile_backend.expert.geolocation;

import com.expert.beaute.mobile_backend.expert.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeolocationExpertRepository extends JpaRepository<GeolocationExpert,String> {

    Optional<GeolocationExpert> findByExpertId(String expertId);
    @Query("SELECT ge FROM GeolocationExpert ge WHERE " +
            "abs(ge.latitude - :lat) <= :latDelta AND abs(ge.longitude - :lon) <= :lonDelta")
    List<GeolocationExpert> findExpertsInRadius(@Param("lat") double latitude,
                                                @Param("lon") double longitude,
                                                @Param("latDelta") double latitudeDelta,
                                                @Param("lonDelta") double longitudeDelta);
}
