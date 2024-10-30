package com.expert.beaute.mobile_backend.notification.expert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenNotificationExpertRepository extends JpaRepository<TokenNotificationExpert,Long> {

    List<TokenNotificationExpert> findByExpertId(String expertId);
    void deleteByToken(String token);
    boolean existsByToken(String token);
}
