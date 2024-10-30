package com.expert.beaute.mobile_backend.post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post savePost(Post post);

   // Post getPostById(Long postId);

    void expertlikePost(Long postId, String expertId);

    void expertsharePost(Long postId, String expertId);

    void expertunlikePost(Long postId, String expertId);

    void clientlikePost(Long postId, String clientId);

    void clientsharePost(Long postId, String clientId);

    void clientunlikePost(Long postId, String clientId);

    List<PostResponse> getAllPosts(String expertId);

    List<Long> getExpertLikedPostIds(String expertId);

    void deletePost(String expertId, Long postId);

}
