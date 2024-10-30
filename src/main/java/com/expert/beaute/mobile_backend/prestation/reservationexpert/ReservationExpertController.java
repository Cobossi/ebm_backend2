package com.expert.beaute.mobile_backend.prestation.reservationexpert;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservations")
public class ReservationExpertController {

    @Autowired
    private ReservationExpertService reservationService;

    @PostMapping("/ajouter")
    public ResponseEntity<?> createReservation(@RequestBody ReservationExpertRequest request) {
        ReservationExpert reservation = reservationService.createReservation(
                request.getExpertClientId(),
                request.getExpertId(),
                request.getPrestationIds(),
                request.getDateReservation(),
                request.getDateService()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/expertclient/{expertClientId}")
    public ResponseEntity<List<ReservationExpertResponse>> getClientReservations(@PathVariable String expertClientId) {
        List<ReservationExpertResponse> reservations = reservationService.getReservationsForExpertClient(expertClientId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/experts/{expertId}")
    public ResponseEntity<List<ReservationExpertResponse>> getExpertReservations(@PathVariable String expertId) {
        List<ReservationExpertResponse> reservations = reservationService.getReservationsForExpert(expertId);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{reservationId}/status-change")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestBody StatusUpdateRequestE request) {
        reservationService.updateReservationStatus(reservationId, request.getNewStatus());
        return ResponseEntity.ok().build();
    }
}
