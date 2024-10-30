package com.expert.beaute.mobile_backend.post.clientshare;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareClientRepository extends JpaRepository<ShareClient, Long> {

    List<ShareClient> findByPostAndClient(Post post, Client client);
}
