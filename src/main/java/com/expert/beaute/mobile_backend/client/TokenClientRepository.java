package com.expert.beaute.mobile_backend.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenClientRepository extends JpaRepository<TokenClient ,Integer> {

    Optional<TokenClient> findByToken(String token);
    void deleteAllByClient(Client client);

}
