package com.expert.beaute.mobile_backend.client.geolocation;


import com.expert.beaute.mobile_backend.expert.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("geolocation")
public class GeolocationClientController {
    @Autowired
    private GeolocationClientService geolocationService;

    @PostMapping("/client/update")
    public ResponseEntity<?> updateClientLocation(@RequestBody GeolocationClientRequest request) {
        geolocationService.updateClientLocation(request.getClientId(), request.getLatitude(), request.getLongitude(),
                request.getPays(), request.getVille(), request.getQuartier());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{clientId}/nearby-experts")
    public ResponseEntity<List<Expert>> getNearbyExperts(@PathVariable String clientId,
                                                         @RequestParam double radius) {
        List<Expert> nearbyExperts = geolocationService.findNearbyExperts(clientId, radius);
        return ResponseEntity.ok(nearbyExperts);
    }
}
