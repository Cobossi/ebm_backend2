package com.expert.beaute.mobile_backend.post;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.post.expertlike.LikeExpert;
import com.expert.beaute.mobile_backend.post.expertshare.ShareExpert;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id_post;
    @Column(length = 5000)
    private String Description;

    private Date postDate;

    private String image;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<LikeExpert> likeExperts = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<ShareExpert> shareExperts = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "expert_id",nullable = false,columnDefinition = "VARCHAR(45)")
    private Expert expert;

    @Column(name = "image", insertable = false, updatable = false)
    private String url;


    @Column(name = "expert_id", insertable = false, updatable = false,columnDefinition = "VARCHAR(45)")
    private String expertId;

}
