package com.expert.beaute.mobile_backend.prestation.reservationclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("reservations")
public class ReservationClientController {

    @Autowired
    private ReservationClientService reservationService;

    @PostMapping("/add")
    public ResponseEntity<?> createReservation(@RequestBody ReservationClientRequest request) {
        ReservationClient reservation = reservationService.createReservation(
                request.getClientId(),
                request.getExpertId(),
                request.getPrestationIds(),
                request.getDateReservation(),
                request.getDateService()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReservationClientResponse>> getClientReservations(@PathVariable String clientId) {
        List<ReservationClientResponse> reservations = reservationService.getReservationsForClient(clientId);
        return ResponseEntity.ok(reservations);
    }

  /*  @GetMapping("/expert/{expertId}")
    public ResponseEntity<List<ReservationClientResponse>> getExpertReservations(@PathVariable String expertId) {
        List<ReservationClientResponse> reservations = reservationService.getReservationsForExpert(expertId);
        return ResponseEntity.ok(reservations);
    }*/

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable String reservationId,
            @RequestBody StatusUpdateRequest request) {
        reservationService.updateReservationStatus(reservationId, request.getNewStatus());
        return ResponseEntity.ok().build();
    }

}

