package com.expert.beaute.mobile_backend.prestation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Long>{
}
