package com.expert.beaute.mobile_backend.abonnement;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, String> {
    Optional<Abonnement> findByClientIdAndExpertId (String clientId, String expertId);
    List<Abonnement> findByClientId (String expertId);
    List<Abonnement> findByExpertId (String expertId);

    @Query("SELECT COUNT(a) FROM Abonnement a WHERE a.expert.Id_expert = :expertId")
    long countAbonnesByExpertId(@Param("expertId") String expertId);

    @Query("SELECT a.client FROM Abonnement a WHERE a.expert.Id_expert = :expertId")
    List<Client> findAbonnesByExpertId(@Param("expertId") String expertId);

    @Query("SELECT a.expert FROM Abonnement a WHERE a.client.Id_client = :clientId")
    List<Expert> findExpertsSuivisByClientId(@Param("clientId") String clientId);
}
