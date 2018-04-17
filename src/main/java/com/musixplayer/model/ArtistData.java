package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class ArtistData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "artistData_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "mbid")
    @Getter
    @Setter
    String mbid;

    @Column(name = "country")
    @Getter
    @Setter
    String country;

    @Column(name = "name")
    @Getter
    @Setter
    String name;

    @Column(name = "begin")
    @Getter
    @Setter
    Date begin;

    @Column(name = "end")
    @Getter
    @Setter
    Date end;

    @Column(name = "gender")
    @Getter
    @Setter
    String gender;

    @Column(name = "image")
    @Getter
    @Setter
    String image;

    @Column(name = "lastfm_url")
    @Getter
    @Setter
    String lastfmUrl;

    @Column(name = "description", length = 10000)
    @Getter
    @Setter
    private String description;

    @ManyToMany(mappedBy = "artists")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Song> songs;
}
