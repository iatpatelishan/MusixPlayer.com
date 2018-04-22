package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Top500Songs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "country")
    @Getter
    @Setter
    private String country;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "topSongs", joinColumns = @JoinColumn(name = "song_Id"), inverseJoinColumns = @JoinColumn(name = "id"))
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Song> songs;

    // constructors
    public Top500Songs() {
    }

    public Top500Songs(String country) {
        this.country = country;
    }
}
