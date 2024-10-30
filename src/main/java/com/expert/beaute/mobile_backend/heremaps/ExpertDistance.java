package com.expert.beaute.mobile_backend.heremaps;

import com.expert.beaute.mobile_backend.expert.geolocation.GeolocationExpert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExpertDistance {

    private final GeolocationExpert expert;
    private final double distance;
}
