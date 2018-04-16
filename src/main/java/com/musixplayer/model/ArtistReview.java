package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ArtistReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "rating")
    @Getter
    @Setter
    private Integer rating;

    @Column(name = "review")
    @Getter
    @Setter
    private String review;

    @Column(name = "flag")
    @Getter
    @Setter
    Boolean flagged;

    @Column(name = "created")
    @Getter
    @Setter
    Date created;

    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    Artist artist;

    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    User user;
}
