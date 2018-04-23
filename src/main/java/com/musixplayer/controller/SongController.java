package com.musixplayer.controller;

import com.musixplayer.model.Artist;
import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Person;
import com.musixplayer.model.Song;
import com.musixplayer.service.ArtistService;
import com.musixplayer.service.PersonService;
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

import javax.swing.plaf.synth.SynthColorChooserUI;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/song")
public class SongController {

    private SongService songService;
    private PersonService personService;
    private ArtistService artistService;

    @Autowired
    public SongController(SongService songService, PersonService personService, ArtistService artistService) {
        this.songService = songService;
        this.personService = personService;
        this.artistService = artistService;
    }

    @GetMapping("/{mbid}")
    public ModelAndView getSong(ModelAndView modelAndView, @PathVariable("mbid") String mbId, Principal principal) {
        Song song = songService.findSongByMbid(mbId).orElse(songService.fetchSongandAdd(mbId));
        if (song == null) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser!=null) {
                modelAndView.addObject("mxuser", mxuser);
                String currentUserRole = mxuser.getRole().getName();
                boolean songowner= false;
                if(currentUserRole.equals("ARTIST")){
                    Artist mxartist = artistService.findByUsername(principal.getName()).orElse(null);
                    if(mxartist.getArtistData().getSongs().contains(song)){
                        songowner=true;
                        modelAndView.addObject("mxuserIsOwner", songowner);
                    }
                }
            }
        }



        // on every click update view count by 1
        int views;
        if (song.getViews() == null)
            views = 0;
        else views = song.getViews();
        song = songService.updateSongViews(views + 1, song.getId());

        // for song duration
        int seconds, minutes;
        if (song.getDuration() == null) {
            seconds = 0;
            minutes = 0;
        } else {
            minutes = (song.getDuration()) / (1000 * 60);
            seconds = (song.getDuration() / 1000) % 60;
        }

        modelAndView.addObject("songMbid", mbId);
        modelAndView.addObject("songName", song.getName());
        modelAndView.addObject("songLastfmUrl", song.getLastfmUrl());
        modelAndView.addObject("listOfArtists", song.getArtists());
        modelAndView.addObject("songViews", song.getViews());
        modelAndView.addObject("songCreated", song.getCreated());
        modelAndView.addObject("songMinuteDuration", minutes);
        modelAndView.addObject("songSecondsDuration", seconds);

        if (song.getYoutubeUrl() != null) {
            modelAndView.addObject("songLyrics", song.getLyrics());
            modelAndView.addObject("newLineChar", '\n');
        }

        if (song.getImageUrl() == null) {
            modelAndView.addObject("songImageUrl", "https://i.imgur.com/fhQ16yL.jpg");
        } else {
            modelAndView.addObject("songImageUrl", song.getImageUrl());
        }
        if (song.getYoutubeUrl() != null) {
            modelAndView.addObject("songYoutubeLink", song.getYoutubeUrl());
        }
        if (song.getDescription() == null) {
            modelAndView.addObject("songDescription", "No description found for this song");
        } else {
            modelAndView.addObject("songDescription", song.getDescription());
        }

        if (song.getReviews() != null) {
            modelAndView.addObject("songReviews", song.getReviews());
        }

        modelAndView.setViewName("song");
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteSong(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams, Principal principal) {

        String songMbid = (String) requestParams.get("mbid");
        String requestURI = "/";
        Song song = songService.findSongByMbid(songMbid).orElse(null);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser!=null && song != null) {
                String currentUserRole = mxuser.getRole().getName();
                boolean songowner= false;
                if(currentUserRole.equals("ARTIST")){
                    Artist mxartist = artistService.findByUsername(principal.getName()).orElse(null);
                    if(mxartist.getArtistData().getSongs().contains(song)){
                        songowner=true;
                    }
                }

                if(currentUserRole.equals("ADMIN") || currentUserRole.equals("EDITOR") || songowner){
                    songService.deleteSongByMbid(songMbid);
                }

            }
        }

        modelAndView.setViewName("redirect:" + requestURI);
        return modelAndView;
    }

}
