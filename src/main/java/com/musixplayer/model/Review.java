package com.musixplayer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "rating")
    @Getter
    @Setter
    private Integer rating;

    @Column(name = "review", length = 10000)
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
    @Getter
    @Setter
    Song song;

    @ManyToOne
    @Getter
    @Setter
    Person reviewer;

    @ManyToMany(mappedBy = "likedReviews")
    @Getter
    @Setter
    private Collection<Person> likedBy;


}
