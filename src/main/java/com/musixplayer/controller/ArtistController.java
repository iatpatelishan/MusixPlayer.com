package com.musixplayer.controller;

import com.musixplayer.model.Artist;
import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Person;
import com.musixplayer.service.ArtistDataService;
import com.musixplayer.service.ArtistService;
import com.musixplayer.service.PersonService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/artist")
public class ArtistController {

    private ArtistDataService artistDataService;
    private ArtistService artistService;
    private PersonService personService;

    @Autowired
    public ArtistController(ArtistDataService artistDataService, PersonService personService, ArtistService artistService) {
        this.artistDataService = artistDataService;
        this.artistService = artistService;
        this.personService = personService;
    }

    @GetMapping("/{mbid}")
    public ModelAndView getArtist(ModelAndView modelAndView, @PathVariable("mbid") String mbid, Principal principal) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser= personService.findByUsername(principal.getName()).orElse(null);
            if(mxuser!=null){
                modelAndView.addObject("mxuser", mxuser);
            }
        }

        ArtistData artist = artistDataService.findByMbid(mbid).orElse(artistDataService.fetchArtistDataandAdd(mbid));
        if (artist == null) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }

        String artistPersonUsername=null;
        if (artist != null) {
            List<Artist> artistPersonList = artistService.findAll();
            for(Artist artistPerson : artistPersonList) {
                if (artistPerson.getArtistData().getId().equals(artist.getId())) {
                    artistPersonUsername = artistPerson.getUsername();
                }
            }
        }
        if(artistPersonUsername!=null){
            modelAndView.addObject("artistPersonUsername", artistPersonUsername);
        }

        modelAndView.addObject("artistMbid", mbid);
        modelAndView.addObject("artistName", artist.getName());
        modelAndView.addObject("artistBeginDate", artist.getBegin());
        modelAndView.addObject("artistLastfmUrl", artist.getLastfmUrl());
        modelAndView.addObject("listOfSongs", artist.getSongs());

        if (artist.getImage() == null) {
            modelAndView.addObject("artistImageUrl", "http://www.liquidmoon.co.uk/wp-content/uploads/2014/08/musix-logo-1000x700.jpg");
        } else {
            modelAndView.addObject("artistImageUrl", artist.getImage());
        }
        if (artist.getGender() == null) {
            modelAndView.addObject("artistGender", "Unknown");
        } else {
            modelAndView.addObject("artistGender", artist.getGender());
        }
        if (artist.getCountry() == null) {
            modelAndView.addObject("artistCountry", "Unknown");
        } else {
            modelAndView.addObject("artistCountry", artist.getCountry());
        }
        if (artist.getDescription() == null) {
            modelAndView.addObject("artistDescription", "No description found for this song");
        } else {
            modelAndView.addObject("artistDescription", artist.getDescription());
        }

        if (artist.getArtistReviews() != null) {
            modelAndView.addObject("artistReviews", artist.getArtistReviews());
        }

        modelAndView.setViewName("artist");
        return modelAndView;

    }

}
