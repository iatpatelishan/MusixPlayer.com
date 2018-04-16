package com.musixplayer.apiDto.lastfm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class lastfmSong {
    String name;
    String duration;
    String mbid;
    String lastfmUrl;
    Object streamable;
    Object artist;
    Object image;

}
