package com.expert.beaute.mobile_backend.post.clientlike;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeClientRepository extends JpaRepository<LikeClient, Long> {

    Optional<LikeClient> findByPostAndClient(Post post, Client client);
}
