package com.musixplayer.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User extends Person {

    @OneToMany(mappedBy="user")
    @Getter
    @Setter
    private Collection<ArtistReview> artistReviewed;
}
