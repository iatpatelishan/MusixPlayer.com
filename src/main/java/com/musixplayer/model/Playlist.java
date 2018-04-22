package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    Date created;

    @Getter
    @Setter
    Date modified;

    @ManyToOne
    @Getter
    @Setter
    private Person createdBy;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "playlistSongs", joinColumns = @JoinColumn(name = "song_Id"), inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Song> songs;


}
