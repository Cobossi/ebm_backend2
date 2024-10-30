package com.expert.beaute.mobile_backend.notification.client;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.notification.Notification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "token_notification_client")
public class TokenNotificationClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id",columnDefinition = "VARCHAR(45)")
    private Client client;

    @Column(unique = true)
    private String token;


}
