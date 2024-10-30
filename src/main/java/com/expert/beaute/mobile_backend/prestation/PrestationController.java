package com.expert.beaute.mobile_backend.prestation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("expert")
public class PrestationController {
    @Autowired
    private PrestationService expertServices;

    @GetMapping("/prestation")
    public ResponseEntity<List<PrestationRequest>> getAllServices() {
        List<PrestationRequest> services = expertServices.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{expertId}/prestation")
    public ResponseEntity<List<PrestationRequest>> getExpertServices(@PathVariable String expertId) {
        Map<Prestation, BigDecimal> servicePrices = expertServices.getExpertServicePrices(expertId);
        List<PrestationRequest> serviceDTOs = servicePrices.entrySet().stream()
                .map(entry -> convert2ToDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceDTOs);
    }


    @PostMapping("/{expertId}/prestation/{serviceId}")
    public ResponseEntity<Void> addServiceToExpert(
            @PathVariable String expertId,
            @PathVariable Long serviceId,
            @RequestParam BigDecimal price) {
        expertServices.addServiceToExpert(expertId, serviceId, price);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{expertId}/prestation/{serviceId}")
    public ResponseEntity<Void> updateServiceToExpert(
            @PathVariable String expertId,
            @PathVariable Long serviceId,
            @RequestParam BigDecimal price) {
        expertServices.updatePrice(expertId, serviceId, price);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{expertId}/prestation/{serviceId}")
    public ResponseEntity<Void> removeServiceFromExpert(
            @PathVariable String expertId,
            @PathVariable Long serviceId) {
        expertServices.removeServiceFromExpert(expertId, serviceId);
        return ResponseEntity.ok().build();
    }


    private PrestationRequest convert2ToDTO(Prestation service, BigDecimal price) {
        return new PrestationRequest(
                service.getId(),
                service.getNom(),
                service.getDescription(),
                service.getPrixBase(),
                price
        );
    }
}