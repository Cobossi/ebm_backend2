package com.expert.beaute.mobile_backend.post.expertlike;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeExpertRepository extends JpaRepository<LikeExpert, Long> {
    Optional<LikeExpert> findByPostAndExpert(Post post, Expert expert);
    @Query("SELECT l.post.Id_post FROM LikeExpert l WHERE l.expertId = :expertId")
    List<Long> findPostIdsByExpertId(@Param("expertId") String expertId);

}
