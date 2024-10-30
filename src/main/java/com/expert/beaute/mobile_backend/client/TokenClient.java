package com.expert.beaute.mobile_backend.client;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TokenClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_token_client;

    @Column(unique = true)
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "Client_id", nullable = false,columnDefinition = "VARCHAR(45)")
    private Client client;
}
