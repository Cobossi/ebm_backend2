package com.expert.beaute.mobile_backend.notification;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.notification.expert.NotificationExpert;
import com.expert.beaute.mobile_backend.notification.expert.NotificationExpertRepository;
import com.expert.beaute.mobile_backend.notification.expert.TokenNotificationExpert;
import com.expert.beaute.mobile_backend.notification.expert.TokenNotificationExpertRepository;
import com.google.firebase.messaging.*;
import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.notification.client.NotificationClient;
import com.expert.beaute.mobile_backend.notification.client.NotificationClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationExpertRepository notificationExpertRepository;

    @Autowired
    private TokenNotificationExpertRepository tokenNotificationRepository;

    @Transactional
    public void createAndSendNotification(String content, String type, String expertId, String title) {
        // 1. Créer la notification dans la base de données
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setContent(content);
        notification.setType(type);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        NotificationExpert notificationExpert = new NotificationExpert();
        notific