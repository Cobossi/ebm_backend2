package com.expert.beaute.mobile_backend.notification.expert;

import com.expert.beaute.mobile_backend.expert.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationExpertRepository extends JpaRepository<NotificationExpert,Long> {

    List<NotificationExpert> findByExpertAndIsReadFalse(Expert expert);
}
