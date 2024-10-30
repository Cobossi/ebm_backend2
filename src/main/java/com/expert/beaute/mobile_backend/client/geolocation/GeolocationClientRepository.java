package com.expert.beaute.mobile_backend.client.geolocation;


import com.expert.beaute.mobile_backend.expert.geolocation.GeolocationExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeolocationClientRepository extends JpaRepository<GeolocationClient,String> {

    Optional<GeolocationClient> findByClientId(String clientId);
}
