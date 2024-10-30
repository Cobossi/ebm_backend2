package com.expert.beaute.mobile_backend.notification.expert;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.notification.Notification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notification_expert")
public class NotificationExpert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private boolean isRead;
    // Getters et setters
}
