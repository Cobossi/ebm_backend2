package com.expert.beaute.mobile_backend.expert;

import com.expert.beaute.mobile_backend.abonnement.Abonnement;
import com.expert.beaute.mobile_backend.prestation.Prestation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expert")
@EntityListeners(AuditingEntityListener.class)
public class Expert implements UserDetails, Principal {

    @Id
    @GeneratedValue(generator = "custom-id")
    @Column(columnDefinition = "VARCHAR(45)")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.expert.beaute.mobile_backend.config.ConfigurableIdGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "expert")
            }
    )
    private String Id_expert;

    @Column(name = "id_expert", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;
    private String nom;
    private String prenom;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String expertname;

    @Column(length = 64,unique = true)
    private String email;
    @Column(length = 64, nullable = false,unique = true)
    private String password;
    @Column(length = 64,unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String actus;

    private Double etoile;

    @Column(length = 256)
    private String profilePhoto;
    private boolean accountLocked;
    private boolean enabled;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "activation_deadline")
    private LocalDateTime activationDeadline;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.activationDeadline = LocalDateTime.now().plusHours(24); // Par exemple, 24h pour activer

    }

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;



    @ManyToMany
    @JoinTable(
            name = "expert_prestation",
            joinColumns = @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Prestation> prestation = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "expert_prestation_prix", joinColumns = @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)"))
    @MapKeyJoinColumn(name = "prestation_id")
    @Column(name = "prix")
    private Map<Prestation, BigDecimal> servicePrices = new HashMap<>();


    @OneToMany(mappedBy = "expert")
    private Set<Abonnement> abonnes = new HashSet<>();

    // Constructeurs, getters et setters


    // Getters et setters pour tous les champs

    public void addService(Prestation services, BigDecimal price) {
        this.prestation.add(services);
        this.servicePrices.put(services, price);
    }

    public void removeService(Prestation services) {
        this.prestation.remove(services);
        this.servicePrices.remove(services);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expert expert = (Expert) o;
        return Objects.equals(Id_expert, expert.Id_expert) && Objects.equals(email, expert.email);
    }
    public String getFullName() {
        return nom + " " + prenom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id_expert, email);
    }

    public String fullName() {
        return getNom() + " " + getPrenom();
    }
}
