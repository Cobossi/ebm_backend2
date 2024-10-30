package com.expert.beaute.mobile_backend.abonnement;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import jakarta.persistence.*;
import org.hibernate.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "abonnement")
public class Abonnement {

    @Id
    @GeneratedValue(generator = "custom-id")
    @Column(columnDefinition = "VARCHAR(45)")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.expert.beaute.mobile_backend.config.ConfigurableIdGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "abonnement")
            }
    )
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id",columnDefinition = "VARCHAR(45)")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @Column(name = "client_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String clientId;

    @Column(name = "expert_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;

    @Column(name = "date_abonnement")
    private LocalDateTime dateAbonnement;

    // Constructeurs, getters et setters
}
