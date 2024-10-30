package com.expert.beaute.mobile_backend.prestation.reservationclient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationClientRepository extends JpaRepository<ReservationClient,String > {
    List<ReservationClient> findByClientId(String clientId);
    List<ReservationClient> findByExpertId(String expertId);
}
