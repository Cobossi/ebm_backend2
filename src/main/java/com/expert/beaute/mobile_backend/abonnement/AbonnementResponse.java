package com.expert.beaute.mobile_backend.abonnement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AbonnementResponse {

    private String Id_client;
    private String Id_expert;
}
