package com.expert.beaute.mobile_backend.expert;

import com.expert.beaute.mobile_backend.abonnement.*;
import com.expert.beaute.mobile_backend.prestation.*;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpertMapper {

    public  ExpertResponse toDTO(Expert expert) {
        if (expert == null) {
            return null;
        }

        ExpertResponse dto = new ExpertResponse();
        dto.setId_expert(expert.getId_expert());
        dto.setNom(expert.getNom());
        dto.setPrenom(expert.getPrenom());
        dto.setExpertname(expert.getExpertname());
        dto.setEmail(expert.getEmail());
        dto.setPhoneNumber(expert.getPhoneNumber());
        dto.setActus(expert.getActus());
        dto.setEtoile(expert.getEtoile());
        dto.setProfilePhoto(expert.getProfilePhoto());
        dto.setAccountLocked(expert.isAccountLocked());
        dto.setEnabled(expert.isEnabled());
        dto.setCreateDate(expert.getCreateDate());
        dto.setActivationDeadline(expert.getActivationDeadline());
        dto.setLastModifiedDate(expert.getLastModifiedDate());

        // Conversion des prestations en IDs
        dto.setPrestation(expert.getPrestation().stream()
                .map(prestation -> {
                    PrestationResponse response = new PrestationResponse();
                    response.setId(prestation.getId());
                    response.setNom(prestation.getNom());
                    // autres mappings nécessaires
                    return response;
                })
                .collect(Collectors.toList()));

        // Conversion des prix de service
        Map<Prestation, BigDecimal> servicePrices = expert.getServicePrices();
        if (servicePrices != null) {
            Map<Long, BigDecimal> convertedPrices = new HashMap<>();
            for (Map.Entry<Prestation, BigDecimal> entry : servicePrices.entrySet()) {
                convertedPrices.put(entry.getKey().getId(), entry.getValue());
            }
            dto.setServicePrices(convertedPrices);
        }
        // Conversion des abonnés en IDs
        dto.setAbonneIds(expert.getAbonnes().stream()
                .map(Abonnement::getId)
                .collect(Collectors.toSet()));

        return dto;
    }

    public  Expert toEntity(ExpertResponse dto) {
        if (dto == null) {
            return null;
        }

        Expert expert = new Expert();
        expert.setId_expert(dto.getId_expert());
        expert.setNom(dto.getNom());
        expert.setPrenom(dto.getPrenom());
        expert.setExpertname(dto.getExpertname());
        expert.setEmail(dto.getEmail());
        expert.setPhoneNumber(dto.getPhoneNumber());
        expert.setActus(dto.getActus());
        expert.setEtoile(dto.getEtoile());
        expert.setProfilePhoto(dto.getProfilePhoto());
        expert.setAccountLocked(dto.isAccountLocked());
        expert.setEnabled(dto.isEnabled());
        expert.setCreateDate(dto.getCreateDate());
        expert.setActivationDeadline(dto.getActivationDeadline());
        expert.setLastModifiedDate(dto.getLastModifiedDate());

        // Note: Les conversions inverses pour prestation, servicePrices et abonnes
        // nécessiteraient des recherches en base de données ou des services supplémentaires

        return expert;
    }
}
