package com.expert.beaute.mobile_backend.post.expertlike;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post_likes_expert")
public class LikeExpert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @Column(name = "expert_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;


    private LocalDateTime likedAt;

    // Getters et setters
}
