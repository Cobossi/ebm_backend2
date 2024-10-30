package com.expert.beaute.mobile_backend.notification.client;

import com.expert.beaute.mobile_backend.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationClientRepository extends JpaRepository<NotificationClient,Long> {

    List<NotificationClient> findByClientAndIsReadFalse(Client client);
}
