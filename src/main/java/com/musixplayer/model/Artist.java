package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Entity
public class Artist extends Person {

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "mbID", referencedColumnName = "mbId")
    @Getter @Setter ArtistData artistData;

    @OneToMany(mappedBy="artist")
    @JsonIgnore
    @Getter
    @Setter
    Collection<ArtistReview> artistReviews;
}
