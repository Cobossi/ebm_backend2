package com.expert.beaute.mobile_backend.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findById(String Id_client);

    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndEnabled(String email, boolean enabled);
    boolean existsByPhoneNumberAndEnabled(String phoneNumber, boolean enabled);

    void deleteAllByEnabledFalseAndActivationDeadlineBefore(LocalDateTime deadline);

    List<Client> findAllByEmailAndEnabledFalse(String email);
    List<Client> findAllByPhoneNumberAndEnabledFalse(String phoneNumber);
}
