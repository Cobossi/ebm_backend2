package com.expert.beaute.mobile_backend.prestation.reservationclient;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReservationClientRequest {
    private String clientId;
    private String expertId;
    private List<Long> prestationIds;
    private LocalDateTime dateReservation;
    private LocalDateTime dateService;

    // Getters and setters
}
