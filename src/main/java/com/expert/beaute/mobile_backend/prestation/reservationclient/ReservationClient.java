package com.expert.beaute.mobile_backend.prestation.reservationclient;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import com.expert.beaute.mobile_backend.prestation.Prestation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "reservation_client")
public class ReservationClient {
    @Id
    @GeneratedValue(generator = "custom-id")
    @Column(columnDefinition = "VARCHAR(45)")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.expert.beaute.mobile_backend.config.ConfigurableIdGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "reservation")
            }
    )
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id",columnDefinition = "VARCHAR(45)")
    private Client client;

    @Column(name = "client_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String clientId;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @Column(name = "expert_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;

    private LocalDateTime dateReservation;

    private LocalDateTime dateService;

    @ManyToMany
    @JoinTable(
            name = "reservation_prestation",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"reservation_id", "prestation_id"})
    )
    private List<Prestation> prestations;

    private BigDecimal coutTotal;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // Getters, setters, constructeurs
}
