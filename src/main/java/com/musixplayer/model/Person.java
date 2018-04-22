package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.UniqueElements;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "email", unique=true)
    @Getter
    @Setter
    private String email;

    @Column(name = "emailhash", unique=true)
    @Getter
    @Setter
    private String emailHash;

    @Column(name = "password")
    @JsonIgnore
    @Getter
    @Setter
    private String password;

    @Column(name = "username", unique=true)
    @Getter
    @Setter
    private String username;

    @Column(name = "firstName")
    @NotEmpty(message = "Please provide your first name")
    @Getter
    @Setter
    private String firstName;

    @Column(name = "lastName")
    @NotEmpty(message = "Please provide your last name")
    @Getter
    @Setter
    private String lastName;

    @Column(name = "enabled")
    @Getter
    @Setter
    private Boolean enabled;

    @Column(name = "confirmation_token")
    @Getter
    @Setter
    private String confirmationToken;

    @Column(name = "api_token")
    @Getter
    @Setter
    private String apiToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    @Getter
    @Setter
    private Role role;

    @ManyToMany
    @JoinTable(name = "following", joinColumns = {
            @JoinColumn(name = "follower", referencedColumnName = "person_id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "following", referencedColumnName = "person_id", nullable = false)})
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Person> following;

    @ManyToMany(mappedBy = "following")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Person> followedBy;


    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Playlist> playlists;

    @OneToMany(mappedBy = "reviewer",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Review> reviews;

    @OneToMany(mappedBy="artistReviewer",cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Collection<ArtistReview> artistReviewed;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "person_likes_review", joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "person_id"), inverseJoinColumns = @JoinColumn(name = "review_id", referencedColumnName = "review_id"))
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Review> likedReviews;


}