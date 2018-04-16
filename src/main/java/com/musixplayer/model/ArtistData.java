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

    @Column(name = "mbId")
    @Getter
    @Setter
    String mbId;

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

    @ManyToMany(mappedBy = "artists")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Song> songs;

    @OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,mappedBy = "artistData")
    @JsonIgnore
    private Artist artist;


}
