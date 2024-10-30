package com.expert.beaute.mobile_backend.prestation.reservationexpert;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.expert.update.UpdateExpertService;
import com.expert.beaute.mobile_backend.prestation.Prestation;
import com.expert.beaute.mobile_backend.prestation.PrestationResponse;
import com.expert.beaute.mobile_backend.prestation.PrestationService;
import com.expert.beaute.mobile_backend.prestation.reservationclient.ReservationClient;
import com.expert.beaute.mobile_backend.prestation.reservationclient.ReservationClientResponse;
import com.expert.beaute.mobile_backend.prestation.reservationclient.StatutReservation;
import com.expert.beaute.mobile_backend.prestation.reservationexpert.StatutReservationE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationExpertService {
    @Autowired
    private ReservationExpertRepository reservationRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private UpdateExpertService expertClientService;

    @Autowired
    private UpdateExpertService expertService;

    @Autowired
    private PrestationService prestationService;

    @Transactional
    public ReservationExpert createReservation(String expertClientId, String expertId, List<Long> prestationIds, LocalDateTime dateReservation, LocalDateTime dateService) {
        Expert expertClient = expertClientService.getExpertClientById(expertClientId);
        Expert expert = expertService.getExpertReservationId(expertId);

        List<Prestation> prestations = prestationService.getPrestationsById(prestationIds);

        if (prestations.isEmpty()) {
            throw new RuntimeException("Aucune prestation sélectionnée");
        }


        BigDecimal coutTotal = calculateTotalCost(expert, prestations);

        ReservationExpert reservation = new ReservationExpert();
        reservation.setExpertClient(expertClient);
        reservation.setExpert(expert);
        reservation.setDateService(dateService);
        reservation.setPrestations(prestations);
        reservation.setDateReservation(dateReservation);
        reservation.setStatut(StatutReservationE.EN_ATTENTE);
        reservation.setCoutTotal(coutTotal);

        return reservationRepository.save(reservation);
    }

    private BigDecimal calculateTotalCost(Expert expert, List<Prestation> prestations) {
        return prestations.stream()
                .map(prestation -> expert.getServicePrices().getOrDefault(prestation, prestation.getPrixBase()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ReservationExpertResponse> getReservationsForExpertClient(String clientId) {
        List<ReservationExpert> reservations =  reservationRepository.findByExpertClientId(clientId);

        // Récupérer l'expert une seule fois pour optimiser les performances
        Expert expert = expertRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + clientId));

        return reservations.stream()
                .map(reservation -> mapToReservationResponse(reservation, expert))
                .collect(Collectors.toList());
    }

    public List<ReservationExpertResponse> getReservationsForExpert(String expertId) {
        List<ReservationExpert> reservations = reservationRepository.findByExpertId(expertId);

        // Récupérer l'expert une seule fois pour optimiser les performances
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + expertId));

        return reservations.stream()
                .map(reservation -> mapToReservationResponse(reservation, expert))
                .collect(Collectors.toList());
    }

    private ReservationExpertResponse mapToReservationResponse(ReservationExpert reservation, Expert expert) {
        return ReservationExpertResponse.builder()
                .reservationId(reservation.getId())
                .expertClientId(reservation.getExpertClientId())
                .expertPhone(reservation.getExpertClient().getPhoneNumber())
                .expertPicture(reservation.getExpertClient().getProfilePhoto())
                .expertId(reservation.getExpertId())
                .dateReservation(reservation.getDateReservation())
                .dateService(reservation.getDateService())
                .prestations(mapToPrestationResponses(reservation.getPrestations(), expert))
                .coutTotal(reservation.getCoutTotal())
                .status(reservation.getStatut().toString())
                .build();
    }

    private List<PrestationResponse> mapToPrestationResponses(List<Prestation> prestations, Expert expert) {
        return prestations.stream()
                .map(prestation -> mapToPrestationResponse(prestation, expert))
                .collect(Collectors.toList());
    }

    private PrestationResponse mapToPrestationResponse(Prestation prestation, Expert expert) {
        return PrestationResponse.builder()
                .id(prestation.getId())
                .nom(prestation.getNom())
                .prix(expert.getServicePrices().get(prestation))
                .build();
    }

    @Transactional
    public ReservationExpert updateReservationStatus(Long reservationId, StatutReservationE newStatus) {
        ReservationExpert reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setStatut(newStatus);
        return reservationRepository.save(reservation);
    }
}
