package com.expert.beaute.mobile_backend.prestation.reservationexpert;

import com.expert.beaute.mobile_backend.prestation.reservationclient.ReservationClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationExpertRepository extends JpaRepository<ReservationExpert,Long> {
    List<ReservationExpert> findByExpertClientId(String clientId);
    List<ReservationExpert> findByExpertId(String expertId);
}
