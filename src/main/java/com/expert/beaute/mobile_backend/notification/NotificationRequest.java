package com.expert.beaute.mobile_backend.notification;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationRequest {

    //private Long id;
    private String content;
    private String type;
    private LocalDateTime createdAt;
    private String title;
    private String expertId;
}
