package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "song_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "song_mbId")
    @Getter
    @Setter
    private String mbId;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column(name = "description", length = 1000)
    @Getter
    @Setter
    private String description;

    @Column(name = "duration")
    @Getter
    @Setter
    private Integer duration;

    @Column(name = "lastfm_url")
    @Getter
    @Setter
    private String lastfmUrl;

    @Column(name = "youtube_url")
    @Getter
    @Setter
    private String youtubeUrl;

    @Column(name = "image_url")
    @Getter
    @Setter
    private String imageUrl;

    @Column(name = "views")
    @Getter
    @Setter
    private Integer views;



    @Column(name = "created", insertable = false, updatable = false)
    @Getter
    @Setter
    private Date created;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "song_artistdata", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "artistData_id"))
    @JsonIgnore
    @Getter
    @Setter
    private Collection<ArtistData> artists;

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Playlist> playlists;

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Top500Songs> top500SongsList;

    @OneToMany(mappedBy = "song")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Lyrics> lyrics;

    @OneToMany(mappedBy = "song")
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Review> reviews;

    // constructor


    public Song() {
    }


    public Song(String name, String description, Integer duration, String lastfmUrl, String youtubeUrl, String imageUrl) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.lastfmUrl = lastfmUrl;
        this.youtubeUrl = youtubeUrl;
        this.imageUrl = imageUrl;
    }

    public Song(String mbId, String name, String description, Integer duration, String lastfmUrl, String imageUrl, Collection<ArtistData> artists) {
        this.mbId = mbId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.lastfmUrl = lastfmUrl;
        this.imageUrl = imageUrl;
        this.artists = artists;
    }
}
