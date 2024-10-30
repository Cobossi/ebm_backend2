package com.expert.beaute.mobile_backend.notification;

import com.expert.beaute.mobile_backend.expert.TokenExpert;
import com.expert.beaute.mobile_backend.notification.client.NotificationClient;
import com.expert.beaute.mobile_backend.notification.expert.NotificationExpert;
import com.expert.beaute.mobile_backend.notification.expert.TokenExpertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.createAndSendNotification(
                request.getContent(),
                request.getType(),
                request.getExpertId(),
                request.getTitle()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token")
    public ResponseEntity<Void> saveToken(@RequestBody TokenExpertRequest request) {
        notificationService.saveUserToken(request.getExpertId(), request.getToken());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread/{clientId}")
    public ResponseEntity<List<NotificationExpert>> getUnreadNotifications(
            @PathVariable String clientId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(clientId));
    }

    @PostMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
