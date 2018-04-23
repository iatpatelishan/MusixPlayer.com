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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {

    private PlaylistService playlistService;
    private PersonService personService;
    private SongService songService;

    @Autowired
    public PlaylistController(PersonService personService, PlaylistService playlistService, SongService songService) {
        this.playlistService = playlistService;
        this.personService = personService;
        this.songService = songService;
    }

    @GetMapping("/{id}/")
    public ModelAndView getPlaylistDetails(ModelAndView modelAndView, @PathVariable("id") Long id, Principal principal) {
        Playlist playlist = playlistService.findById(id).orElse(null);
        if (playlist == null) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }
        if (playlist.getSongs() == null) {
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

    @PostMapping("/{id}/delete")
    public ModelAndView deleteProfile(ModelAndView modelAndView, BindingResult bindingResult, @PathVariable("id") Long id, Principal principal, HttpSession session, HttpServletRequest request) {

        String requestURI = "/playlist/" + id + "/";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser != null) {
                Person person = personService.findByUsername(principal.getName()).orElse(null);
                String currentUserRole = person.getRole().getName();

                Playlist playlist = playlistService.findById(id).orElse(null);
                if (playlist != null) {
                    if (playlist.getCreatedBy().getUsername().equals(principal.getName()) || currentUserRole.equals("ADMIN")) {
                        playlistService.delete(playlist);
                    }
                }
            }
        }

        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @PostMapping("/{id}/deletesong")
    public ModelAndView addSongToPlaylist(ModelAndView modelAndView, RedirectAttributes redir, @PathVariable("id") Long playlistId, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal) {
        String songMbid = (String) requestParams.get("mbid");
        String requestURI = "/playlist/" + playlistId + "/";

        Playlist playlist = playlistService.findById(playlistId).orElse(null);

        Song song = songService.findSongByMbid(songMbid).orElse(null);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            String currentUserRole = mxuser.getRole().getName();
            if (mxuser != null && playlist != null && song != null && playlist.getCreatedBy().getUsername().equals(mxuser.getUsername()) || currentUserRole.equals("ADMIN")) {
                playlist.getSongs().remove(song);
            }
            playlist = playlistService.create(playlist);
        } else {
            modelAndView.setViewName("error/403");
            return modelAndView;
        }

        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;

}


}
