package com.expert.beaute.mobile_backend.client;

import com.expert.beaute.mobile_backend.abonnement.Abonnement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
@EntityListeners(AuditingEntityListener.class)
public class Client implements UserDetails, Principal{


    @Id
    @GeneratedValue(generator = "custom-id")
    @Column(columnDefinition = "VARCHAR(45)")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.expert.beaute.mobile_backend.config.ConfigurableIdGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "client")
            }
    )
    private String Id_client;
    private String nom;
    private String prenom;

    @Column(length = 64,unique = true)
    private String email;
    @Column(length = 64, nullable = false,unique = true)
    private String password;
    @Column(length = 64,unique = true)
    private String phoneNumber;

    @Column(length = 256)
    private String profilePhoto;
    private boolean accountLocked;
    private boolean enabled;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "activation_deadline")
    private LocalDateTime activationDeadline;

    @OneToMany(mappedBy = "client")
    private Set<Abonnement> abonnements = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.activationDeadline = LocalDateTime.now().plusHours(24); // Par exemple, 24h pour activer

    }

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


    @Override
    public String getName() {
        return this.email;
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
        return this.email;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(Id_client, client.Id_client) && Objects.equals(email, client.email);
    }
    public String getFullName() {
        return nom + " " + prenom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id_client, email);
    }

    public String fullName() {
        return getNom() + " " + getPrenom();
    }


}
