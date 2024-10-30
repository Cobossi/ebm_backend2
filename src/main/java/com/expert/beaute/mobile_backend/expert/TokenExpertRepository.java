package com.expert.beaute.mobile_backend.expert;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenExpertRepository extends JpaRepository<TokenExpert,Integer> {

    Optional<TokenExpert> findByToken(String token);
    void deleteAllByExpert(Expert expert);
}
