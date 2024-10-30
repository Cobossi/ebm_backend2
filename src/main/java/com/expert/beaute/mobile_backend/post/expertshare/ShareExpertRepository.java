package com.expert.beaute.mobile_backend.post.expertshare;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareExpertRepository extends JpaRepository<ShareExpert, Long> {
    List<ShareExpert> findByPostAndExpert(Post post, Expert expert);

}
