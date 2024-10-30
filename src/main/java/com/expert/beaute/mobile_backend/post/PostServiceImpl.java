package com.expert.beaute.mobile_backend.post;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.file.FileStorageServices;
import com.expert.beaute.mobile_backend.file.FirebaseStorageService;
import com.expert.beaute.mobile_backend.post.clientlike.LikeClient;
import com.expert.beaute.mobile_backend.post.clientlike.LikeClientRepository;
import com.expert.beaute.mobile_backend.post.clientshare.ShareClient;
import com.expert.beaute.mobile_backend.post.clientshare.ShareClientRepository;
import com.expert.beaute.mobile_backend.post.expertlike.LikeExpert;
import com.expert.beaute.mobile_backend.post.expertlike.LikeExpertRepository;
import com.expert.beaute.mobile_backend.post.expertshare.ShareExpert;
import com.expert.beaute.mobile_backend.post.expertshare.ShareExpertRepository;
import com.expert.beaute.mobile_backend.prestation.Prestation;
import com.expert.beaute.mobile_backend.prestation.PrestationRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    private final ExpertRepository expertRepository;

    private final ClientRepository clientRepository;

    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    private LikeExpertRepository likeExpertRepository;

    @Autowired
    private ShareExpertRepository shareExpertRepository;

    @Autowired
    private LikeClientRepository likeClientRepository;

    @Autowired
    private ShareClientRepository shareClientRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           ExpertRepository expertRepository,
                           FirebaseStorageService firebaseStorageServices,
                           ClientRepository clientRepository
                          ) {
        this.postRepository = postRepository;
        this.expertRepository = expertRepository;
        this.clientRepository=clientRepository;
        this.firebaseStorageService = firebaseStorageServices;

    }

    @Transactional
    public Post savePost(Post post, MultipartFile image) throws IOException {
        // Vérifier si l'expert existe
        if (post.getExpertId() != null) {
            Expert expert = expertRepository.findById(post.getExpertId())
                    .orElseThrow(() -> new RuntimeException("Expert not found with id: " + post.getExpertId()));
            post.setExpert(expert);
        }

        // Vérifier si une image a été fournie
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        // Date de création du post
        post.setPostDate(new Date());

        // Stocker l'image dans Firebase Storage
        String imagePath = firebaseStorageService.storeFile(image, "posts/" + UUID.randomUUID().toString());
        post.setImage(imagePath);

        // Sauvegarder le post dans la base de données
        return postRepository.save(post);
    }


    public void expertlikePost(String postId, String clientId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Expert expert = expertRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<LikeExpert> existingLike = likeExpertRepository.findByPostAndExpert(post, expert);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("User has already liked this post");
        }

        LikeExpert likeExpert = new LikeExpert();
        likeExpert.setPost(post);
        likeExpert.setExpert(expert);
        likeExpert.setLikedAt(LocalDateTime.now());
        likeExpertRepository.save(likeExpert);
    }

    public void expertunlikePost(String postId, String expertId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LikeExpert likeExpert = likeExpertRepository.findByPostAndExpert(post, expert)
                .orElseThrow(() -> new RuntimeException("LikeExpert not found"));
        likeExpertRepository.delete(likeExpert);
    }

    public void expertsharePost(String postId, String expertId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
       Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShareExpert shareExpert = new ShareExpert();
        shareExpert.setPost(post);
        shareExpert.setExpert(expert);
        shareExpert.setSharedAt(LocalDateTime.now());
        shareExpertRepository.save(shareExpert);
    }

    public void clientlikePost(String postId, String clientId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<LikeClient> existingLike = likeClientRepository.findByPostAndClient(post, client);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("User has already liked this post");
        }

        LikeClient likeClient = new LikeClient();
        likeClient.setPost(post);
        likeClient.setClient(client);
        likeClient.setLikedAt(LocalDateTime.now());
        likeClientRepository.save(likeClient);
    }

    public void clientunlikePost(String postId, String expertId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Client client = clientRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LikeClient likeClient = likeClientRepository.findByPostAndClient(post, client)
                .orElseThrow(() -> new RuntimeException("LikeExpert not found"));
        likeClientRepository.delete(likeClient);
    }

    public void clientsharePost(String postId, String expertId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Client client = clientRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShareClient shareClient = new ShareClient();
        shareClient.setPost(post);
        shareClient.setClient(client);
        shareClient.setSharedAt(LocalDateTime.now());
        shareClientRepository.save(shareClient);
    }

    public List<PostResponse> getAllPosts(String expertId) {
        // Récupérer les posts triés par date de création
        List<Post> posts = postRepository.findByExpertIdOrderByCreatedAtDesc(expertId);

        // Convertir chaque post en DTO avec l'URL signée de l'image
        return posts.stream()
                .map(post -> {
                    PostResponse dto = convertToDTO(post);
                    // Générer l'URL signée pour l'image si elle existe
                    if (post.getImage() != null && !post.getImage().isEmpty()) {
                        String signedUrl = firebaseStorageService.generateSignedUrl(post.getImage());
                        dto