package com.expert.beaute.mobile_backend.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String image;        // Chemin Firebase original
    private String imageUrl;     // URL signée pour l'accès à l'image
    private String description;
    private String postId;
    private Long like;
    private Long share;
    private Date postDate;
}