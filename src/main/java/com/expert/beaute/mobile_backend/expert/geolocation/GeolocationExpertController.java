package com.expert.beaute.mobile_backend.expert.geolocation;


import com.expert.beaute.mobile_backend.heremaps.ExpertDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("geolocation")
public class GeolocationExpertController {

    @Autowired
    private GeolocationExpertService geolocationService;

    @GetMapping("/experts/nearby/{expertId}")
    public List<ExpertDistance> getNearbyExperts(
            @PathVariable String expertId,
            @RequestParam(defaultValue = "10.0") double maxDistance) {
        return geolocationService.findNearbyExperts(expertId, maxDistance);
    }






}
