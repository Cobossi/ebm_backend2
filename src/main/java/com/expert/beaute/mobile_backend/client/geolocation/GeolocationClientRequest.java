package com.expert.beaute.mobile_backend.client.geolocation;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeolocationClientRequest {

    private Double latitude;
    private Double longitude;
    private String pays;
    private String ville;
    private String quartier;
    private String clientId;
}
