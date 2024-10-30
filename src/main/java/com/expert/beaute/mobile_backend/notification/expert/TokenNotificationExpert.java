package com.expert.beaute.mobile_backend.notification.expert;

import com.expert.beaute.mobile_backend.expert.Expert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "token_notification_expert")
public class TokenNotificationExpert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @Column(name = "expert_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;

    @Column(unique = true)
    private String token;
}
