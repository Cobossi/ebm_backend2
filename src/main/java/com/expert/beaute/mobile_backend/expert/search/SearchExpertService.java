package com.expert.beaute.mobile_backend.expert.search;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertMapper;

import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import com.expert.beaute.mobile_backend.prestation.PrestationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SearchExpertService {


    private final ExpertRepository expertRepository;
    private  ExpertMapper expertMapper;

    @Autowired
    public SearchExpertService(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;

    }

    public List<ExpertResponse> searchExperts(String nom, String prenom, String serviceName,
                                              BigDecimal minPrice, BigDecimal maxPrice, Double minRating) {


        List <Expert> experts =expertRepository.searchExperts(nom, prenom, serviceName, minPrice, maxPrice, minRating);

        return experts.stream()
                .map(this::mapToExpertResponse)
                .collect(Collectors.toList());
    }

    private ExpertResponse mapToExpertResponse(Expert expert) {


        // Obtenir les prestations avec leurs prix
        List<PrestationResponse> prestations = expert.getServicePrices().entrySet().stream()
                .map(entry -> PrestationResponse.builder()
                        .id(entry.getKey().getId())
                        .nom(entry.getKey().getNom())
                        .prix(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return ExpertResponse.builder()
                .id_expert(expert.getId_expert())
                .nom(expert.getNom())
                .prenom(expert.getPrenom())
                .email(expert.getEmail())
                .phoneNumber(expert.getPhoneNumber())
                .prestation(prestations)
                .etoile(expert.getEtoile())

                .build();
    }
}
