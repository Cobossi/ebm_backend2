package com.expert.beaute.mobile_backend.prestation.reservationexpert;

import com.expert.beaute.mobile_backend.prestation.PrestationResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReservationExpertResponse {

    private String expertClientId;
    private String reservationId;
    private String expertId;
    private List<PrestationResponse> prestations;
    private BigDecimal coutTotal;
    private String status;
    private LocalDateTime dateReservation;
    private LocalDateTime dateService;
    private String expertPhone;
    private String expertPicture;
}
