package com.expert.beaute.mobile_backend.client.geolocation;



import com.expert.beaute.mobile_backend.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Parameter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "geolocation_client")
public class GeolocationClient {

    @Id
    @GeneratedValue(generator = "custom-id")
    @Column(columnDefinition = "VARCHAR(45)")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.expert.beaute.mobile_backend.config.ConfigurableIdGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "geolocation")
            }
    )
    private String id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "pays")
    private String pays;

    @Column(name = "ville")
    private String ville;

    @Column(name = "quartier")
    private String quartier;

    @Column(name = "last_location_update")
    private LocalDateTime lastLocationUpdate;

    @OneToOne
    @JoinColumn(name = "client_id",columnDefinition = "VARCHAR(45)")
    private Client client;

    @Column(name = "client_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String clientId;
}
