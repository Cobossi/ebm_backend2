package com.expert.beaute.mobile_backend.post;

import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.file.FileStorageServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageServices fileStorageServices;

    private ExpertRepository expertRepository;


    @GetMapping("/expert/postlike/{expertId}")
    public  ResponseEntity<Map<String, Object>> getPostById(@PathVariable String expertId){
        List<String> likedPostIds = postService.getExpertLikedPostIds(expertId);
        Map<String, Object> response = new HashMap<>();

        response.put("likedPostIds", likedPostIds);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/expert/posts")
    public Post createPost(
            @RequestPart("post") PostDTO postData,
            @RequestPart("image") MultipartFile image) throws IOException {
        Post post = new Post();
        post.setExpertId(postData.getExpertId());
        post.setDescription(postData.getDescription());
        // mapper d'autres champs si nécessaire

        return postService.savePost(post, image);
    }



    @DeleteMapping("/expert/{expertId}/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePost(
            @PathVariable  String postId,
            @PathVariable  String expertId) {
        postService.deletePost(expertId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expert/posts/{expertId}")
    public ResponseEntity<List<PostResponse>> getExpertPosts(@PathVariable String expertId) {
        List<PostResponse> posts = postService.getAllPosts(expertId);
        return ResponseEntity.ok(posts);
    }
    /*@GetMapping("/expert/posts/{expertId}")
    public ResponseEntity<List<PostDTO>> getAllPosts(@PathVariable String expertId) {
        try {
            List<PostResponse> posts = postService.getAllPosts(expertId);
            List<PostDTO> postsWithUrls = posts.stream()
                    .map(post -> new PostDTO(
                            post,
                            fileStorageServices.getFileUrl(post.getImage())
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(postsWithUrls);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
   /* @GetMapping("/posts/{expertId}")
    public ResponseEntity<List<PostResponse>> getAllPost(@PathVariable String expertId) {
        try {
            List<PostResponse> posts = postService.getAllPosts(expertId);
            List<Map<String, Object>> postsWithImageUrls = posts.stream().map(post -> {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("post", post);
                postMap.put("imageUrl", fileStorageServices.getFileUrl(post.getImage()));
                return postMap;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

/*    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getPostById(postId);

            Map<String, Object> postWithImageUrl = new HashMap<>();
            postWithImageUrl.put("post", post);
            postWithImageUrl.put("imageUrl", fileStorageServices.getFileUrl(post.getImage()));

            return ResponseEntity.ok(postWithImageUrl);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }*/

    @PostMapping("/expert/post/{postId}/like")
    public ResponseEntity<?> expertlikePost(@PathVariable String postId, @RequestParam String expertId) {
        try {
            postService.expertlikePost(postId, expertId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/expert/post/{postId}/like")
    public ResponseEntity<?> expertunlikePost(@PathVariable String postId, @RequestParam String expertId) {
        try {
            postService.expertunlikePost(postId, expertId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/expert/post/{postId}/share")
    public ResponseEntity<?> expertsharePost(@PathVariable String postId, @RequestParam String expertId) {
        try {
            postService.expertsharePost(postId, expertId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/client/post/{postId}/like")
    public ResponseEntity<?> clientlikePost(@PathVariable String postId, @RequestParam String clientId) {
        try {
            postService.clientlikePost(postId, clientId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/client/post/{postId}/like")
    public ResponseEntity<?> clientunlikePost(@PathVariable String postId, @RequestParam String clientId) {
        try {
            postService.clientunlikePost(postId, clientId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/client/post/{postId}/share")
    public ResponseEntity<?> clientsharePost(@PathVariable String postId, @RequestParam String clientId) {
        try {
            postService.clientsharePost(postId, clientId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 .build();
        }
    }
}
