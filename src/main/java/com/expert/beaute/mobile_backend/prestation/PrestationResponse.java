package com.expert.beaute.mobile_backend.prestation;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
public class PrestationResponse {

    private Long id;
    private String nom;
    private BigDecimal prix;

    public PrestationResponse() {

    }
}
