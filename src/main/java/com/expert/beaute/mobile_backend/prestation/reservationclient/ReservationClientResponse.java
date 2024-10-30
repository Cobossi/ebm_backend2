package com.expert.beaute.mobile_backend.prestation.reservationclient;


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
public class ReservationClientResponse {

    private String clientId;
    private String expertId;
    private String reservationId;
    private List<PrestationResponse> prestations;
    private BigDecimal coutTotal;
    private String status;
    private LocalDateTime dateReservation;
    private LocalDateTime dateService;
    private String clientPhone;
    private String clientPicture;
    private String clientPictureUrl;
}
