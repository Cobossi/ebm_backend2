package com.expert.beaute.mobile_backend.prestation;

import java.math.BigDecimal;

public class PrestationRequest {
    private Long id;
    private String nom;
    private String description;
    private BigDecimal prixBase; // Prix de base du service
    private BigDecimal price;    // Prix associé à l'expert

    // Constructeur par défaut
    public PrestationRequest() {}

    // Constructeur paramétré
    public PrestationRequest(Long id, String nom, String description, BigDecimal prixBase, BigDecimal price) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prixBase = prixBase;
        this.price = price;
    }
    public PrestationRequest(Long id, String nom, String description, BigDecimal prixBase) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prixBase = prixBase;

    }


    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(BigDecimal prixBase) {
        this.prixBase = prixBase;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
