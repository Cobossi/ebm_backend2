package com.expert.beaute.mobile_backend.prestation.reservationexpert;


import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.prestation.Prestation;
import com.expert.beaute.mobile_backend.prestation.reservationclient.StatutReservation;
import jakarta.persistence.*;
import org.hibernate.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "reservation_expert")
public class ReservationExpert {

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
    @JoinColumn(name = "expert_client_id",columnDefinition = "VARCHAR(45)")
    private Expert expertClient;

    @Column(name = "expert_client_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertClientId;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @Column(name = "expert_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;

    private LocalDateTime dateReservation;

    private LocalDateTime dateService;

    @ManyToMany
    @JoinTable(
            name = "reservation_prestation_expert",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"reservation_expert_id", "prestation_id"})
    )
    private List<Prestation> prestations;

    private BigDecimal coutTotal;

    @Enumerated(EnumType.STRING)
    private StatutReservationE statut;

}
