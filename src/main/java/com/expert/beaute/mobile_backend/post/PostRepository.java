package com.expert.beaute.mobile_backend.post;

import com.expert.beaute.mobile_backend.prestation.reservationclient.ReservationClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByExpertId(String expertId);
    @Query("SELECT p FROM Post p WHERE p.expert.Id_expert = :expertId ORDER BY p.postDate DESC")
    List<Post> findByExpertIdOrderByCreatedAtDesc(String expertId);
}
