package com.expert.beaute.mobile_backend.expert.geolocation;

import com.expert.beaute.mobile_backend.expert.Expert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeolocationExpertRequest {

    private Double latitude;
    private Double longitude;
    private String pays;
    private String ville;
    private String quartier;
    private String expertId;
}
