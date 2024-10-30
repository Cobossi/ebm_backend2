package com.expert.beaute.mobile_backend.client;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {

    private String id_client;
    private String nom;
    private String prenom;
    private String expertname;
    private String email;
    private String phoneNumber;
    private String actus;
    private Double etoile;
    private String profilePhoto;
    private boolean accountLocked;
    private boolean enabled;
    private String profilePhotoUrl;   // URL signée pour l'accès à la photo
    private LocalDateTime createDate;
    private LocalDateTime activationDeadline;
    private LocalDateTime lastModifiedDate;
}
