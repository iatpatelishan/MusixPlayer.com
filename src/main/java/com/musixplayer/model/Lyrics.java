package com.musixplayer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Lyrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lyrics_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "source")
    @Getter
    @Setter
    private String source;

    @Column(name = "lyrics", length = 10000)
    @Getter
    @Setter
    private String lyrics;

    @ManyToOne
    @Getter
    @Setter
    private Song song;


}
