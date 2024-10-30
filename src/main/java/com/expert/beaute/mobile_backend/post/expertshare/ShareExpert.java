package com.expert.beaute.mobile_backend.post.expertshare;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post_shares_expert")
public class ShareExpert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "expert_id",columnDefinition = "VARCHAR(45)")
    private Expert expert;

    private LocalDateTime sharedAt;

    // Getters et setters
}