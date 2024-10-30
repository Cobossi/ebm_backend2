package com.expert.beaute.mobile_backend.notification.client;


import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.notification.Notification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notification_client")
public class NotificationClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id",columnDefinition = "VARCHAR(45)")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private boolean isRead;
    // Getters et setters
}
