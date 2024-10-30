package com.expert.beaute.mobile_backend.expert;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, String> {

    Optional<Expert> findByEmail(String email);
    Optional<Expert> findByPhoneNumber(String phoneNumber);

    List<Expert> findAllBy();

    List<Expert> findByExpertIdNot(String idExpert);
    boolean existsByEmailAndEnabled(String email, boolean enabled);
    boolean existsByPhoneNumberAndEnabled(String phoneNumber, boolean enabled);

    void deleteAllByEnabledFalseAndActivationDeadlineBefore(LocalDateTime deadline);

    List<Expert> findAllByEmailAndEnabledFalse(String email);
    List<Expert> findAllByPhoneNumberAndEnabledFalse(String phoneNumber);

    @Query("SELECT DISTINCT e FROM Expert e " +
            "LEFT JOIN e.prestation p " +
            "LEFT JOIN e.servicePrices sp " +
            "WHERE (:nom IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
            "AND (:prenom IS NULL OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) " +
            "AND (:serviceName IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :serviceName, '%'))) " +
            "AND (:minPrice IS NULL OR KEY(sp).prixBase >= :minPrice) " +
            "AND (:maxPrice IS NULL OR KEY(sp).prixBase <= :maxPrice) " +
            "AND (:minRating IS NULL OR e.etoile >= :minRating)")
    List<Expert> searchExperts(@Param("nom") String nom,
                               @Param("prenom") String prenom,
                               @Param("serviceName") String serviceName,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("minRating") Double minRating);
}
