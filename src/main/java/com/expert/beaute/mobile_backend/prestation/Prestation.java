package com.expert.beaute.mobile_backend.prestation;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.prestation.reservationclient.ReservationClient;
import com.expert.beaute.mobile_backend.prestation.reservationexpert.ReservationExpert;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "prestation")
public class Prestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal prixBase;

    @Column(nullable = false)
    private Duration duree;

    @Column(nullable = false)
    private String categorie;

    @ManyToMany(mappedBy = "prestations")
    private List<ReservationClient> reservationClient;

    @ManyToMany(mappedBy = "prestations")
    private List<ReservationExpert> reservationExpert;

    @ManyToMany(mappedBy = "prestation")
    private Set<Expert> expert= new HashSet<>();

    // Constructeurs, getters et setters

    public Prestation() {}

    public Prestation(String nom, String description, BigDecimal prixBase, Duration duree, String categorie) {
        this.nom = nom;
        this.description = description;
        this.prixBase = prixBase;
        this.duree = duree;
        this.categorie = categorie;
    }

    // Getters et setters pour tous les champs
}
