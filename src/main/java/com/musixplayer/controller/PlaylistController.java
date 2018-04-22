package com.musixplayer.controller;

import com.musixplayer.model.Artist;
import com.musixplayer.model.Person;
import com.musixplayer.model.Playlist;
import com.musixplayer.model.Song;
import com.musixplayer.service.PersonService;
import com.musixplayer.service.PlaylistService;
import com.musixplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {

    private PlaylistService playlistService;
    private PersonService personService;

    @Autowired
    public PlaylistController(PersonService personService, PlaylistService playlistService) {
        this.playlistService = playlistService;
        this.personService = personService;
    }

    @GetMapping("/{id}/")
    public ModelAndView getPlaylistDetails(ModelAndView modelAndView, @PathVariable("id") Long id, Principal principal) {
        Playlist playlist = playlistService.findById(id).orElse(null);
        if(playlist==null){
            modelAndView.setViewName("error/404");
            return modelAndView;
        }
        if(playlist.getSongs()==null){
            playlist.setSongs(new ArrayList<Song>());
        }
        modelAndView.addObject("playlist", playlist);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser != null) {
                modelAndView.addObject("mxuser", mxuser);
            }
        }

        modelAndView.setViewName("playlist");
        return modelAndView;
    }


}
