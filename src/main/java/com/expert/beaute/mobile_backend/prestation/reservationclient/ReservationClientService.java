package com.expert.beaute.mobile_backend.prestation.reservationclient;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.client.update.UpdateClientService;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertMapper;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import com.expert.beaute.mobile_backend.expert.update.UpdateExpertService;
import com.expert.beaute.mobile_backend.file.FirebaseStorageService;
import com.expert.beaute.mobile_backend.prestation.Prestation;
import com.expert.beaute.mobile_backend.prestation.PrestationResponse;
import com.expert.beaute.mobile_backend.prestation.PrestationService;
import com.expert.beaute.mobile_backend.prestation.reservationexpert.ReservationExpert;
import com.expert.beaute.mobile_backend.prestation.reservationexpert.ReservationExpertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationClientService {


    @Autowired
    private ReservationClientRepository reservationRepository;

    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UpdateClientService clientService;

    private final FirebaseStorageService firebaseStorageService;
    @Autowired
    private UpdateExpertService expertService;

    @Autowired
    private PrestationService prestationService;

    private Expert convertExpertResponseToExpert(ExpertResponse expertResponse) {
        ExpertResponse expertDTO = new ExpertResponse();
        // Remplir expertDTO avec les données de expertResponse
        expertDTO.setId_expert(expertResponse.getId_expert());
        expertDTO.setNom(expertResponse.getNom());
        expertDTO.setPrenom(expertResponse.getPrenom());
        // ... autres champs ...

        // Utiliser ExpertMapper pour convertir DTO en entité
        ExpertMapper mapper = new ExpertMapper();
        return mapper.toEntity(expertDTO);
    }

    @Transactional
    public ReservationClient createReservation(String clientId, String expertId, List<Long> prestationIds, LocalDateTime dateReservation,LocalDateTime dateService) {
        Client client = clientService.getsClientById(clientId);
        Expert expert = expertService.getExpertReservationId(expertId);

        List<Prestation> prestations = prestationService.getPrestationsById(prestationIds);

        if (prestations.isEmpty()) {
            throw new RuntimeException("Aucune prestation sélectionnée");
        }


        BigDecimal coutTotal = calculateTotalCost(expert, prestations);

        ReservationClient reservation = new ReservationClient();
        reservation.setClient(client);
        reservation.setExpert(expert);
        reservation.setDateService(dateService);
        reservation.setPrestations(prestations);
        reservation.setDateReservation(dateReservation);
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        reservation.setCoutTotal(coutTotal);

        return reservationRepository.save(reservation);
    }

    private BigDecimal calculateTotalCost(Expert expert, List<Prestation> prestations) {
        return prestations.stream()
                .map(prestation -> expert.getServicePrices().getOrDefault(prestation, prestation.getPrixBase()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ReservationClientResponse> getReservationsForClient(String clientId) {
        // Vérifier d'abord si le client existe
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        List<ReservationClient> reservations = reservationRepository.findByClientId(clientId);

        return reservations.stream()
                .map(reservation -> {
                    Expert expert = expertRepository.findById(reservation.getExpertId())
                            .orElseThrow(() -> new RuntimeException("Expert not found with id: " + reservation.getExpertId()));
                    return mapToReservationResponse(reservation, client, expert);
                })
                .collect(Collectors.toList());}

    /*public List<ReservationClientResponse> getReservationsForExpert(String expertId) {
        List<ReservationClient> reservations = reservationRepository.findByExpertId(expertId);

        // Récupérer l'expert une seule fois pour optimiser les performances
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + expertId));

        return reservations.stream()
                .map(reservation -> mapToReservationResponse(reservation, expert))
                .collect(Collectors.toList());
    }*/

    private ReservationClientResponse mapToReservationResponse(ReservationClient reservation,Client client, Expert expert) {


         ReservationClientResponse.ReservationClientResponseBuilder responseBuilder= ReservationClientResponse.builder()
                .clientId(reservation.getClientId())
                 .reservationId(reservation.getId())
                .clientPhone(reservation.getClient().getPhoneNumber())
                .clientPicture(reservation.getExpert().getProfilePhoto())
                .expertId(reservation.getExpertId())
                .dateReservation(reservation.getDateReservation())
                .dateService(reservation.getDateService())
                .prestations(mapToPrestationResponses(reservation.getPrestations(), expert))
                .coutTotal(reservation.getCoutTotal())
                .status(reservation.getStatut().toString());

        // Gérer la photo de profil
        if (expert.getProfilePhoto() != null && !expert.getProfilePhoto().isEmpty()) {
            responseBuilder.clientPicture(expert.getProfilePhoto());  // Chemin Firebase original
            String signedUrl = firebaseStorageService.generateSignedUrl(expert.getProfilePhoto());
            responseBuilder.clientPictureUrl(signedUrl);  // URL signée
        }
         return responseBuilder.build();
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
    public ReservationClient updateReservationStatus(String reservationId, StatutReservation newStatus) {
        ReservationClient reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setStatut(newStatus);
        return reservationRepository.save(reservation);
    }
}
