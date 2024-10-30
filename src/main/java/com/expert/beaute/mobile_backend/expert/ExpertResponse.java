package com.expert.beaute.mobile_backend.expert;


import com.expert.beaute.mobile_backend.prestation.PrestationResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertResponse {


    private String id_expert;
    private String nom;
    private String prenom;
    private String expertname;
    private String email;
    private String phoneNumber;
    private String actus;
    private Double etoile;
    private String profilePhoto;
    private String profilePhotoUrl;   // URL signée pour l'accès à la photo
    private boolean accountLocked;
    private boolean enabled;
    private LocalDateTime createDate;
    private LocalDateTime activationDeadline;
    private LocalDateTime lastModifiedDate;
    private List<PrestationResponse> prestation;
    private Map<Long, BigDecimal> servicePrices;
    private Set<String> abonneIds;
}
