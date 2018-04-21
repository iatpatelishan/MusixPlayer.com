package com.musixplayer.controller;

import com.musixplayer.model.*;
import com.musixplayer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;


@Controller
@RequestMapping("/artistreview")
public class ArtistReviewController {

    private ArtistDataService artistDataService;
    private ArtistReviewService artistReviewService;
    private PersonService personService;

    @Autowired
    public ArtistReviewController(ArtistDataService artistDataService, ArtistReviewService artistReviewService, PersonService personService) {
        this.artistDataService = artistDataService;
        this.artistReviewService = artistReviewService;
        this.personService = personService;
    }

    @PostMapping("/add")
    public ModelAndView addReview(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal, HttpSession session){


        String artistMbid = (String) requestParams.get("mbid");
        String requestURI = "/artist/"+artistMbid;
        String reviewtext = (String) requestParams.get("reviewtext");
        Integer ratings = Integer.parseInt((String) requestParams.get("ratings"));

        ArtistData artist = artistDataService.findByMbid(artistMbid).orElse(null);
        if(artist==null){
            modelAndView.setViewName("redirect:"+requestURI+"?wrongmbid");
        }

        Person reviewer = personService.findByUsername(principal.getName()).orElse(null);
        if(reviewer==null){
            modelAndView.setViewName("redirect:"+requestURI+"?wrongreviewer");
        }

        if(reviewer.getArtistReviewed() != null&& (reviewer.getUsername().equals(principal.getName()))){
            for(ArtistReview r : reviewer.getArtistReviewed()){
                if(r.getArtist().getMbid().equals(artistMbid)){
                    r.setRating(ratings);
                    r.setReview(reviewtext);
                    r.setFlagged(false);
                    artistReviewService.create(r);
                    modelAndView.setViewName("redirect:"+requestURI);
                    return modelAndView;
                }
            }
        }

        ArtistReview artistReview = new ArtistReview();
        artistReview.setRating(ratings);
        artistReview.setReview(reviewtext);
        artistReview.setArtistReviewer(reviewer);
        artistReview.setFlagged(false);
        artistReview.setArtist(artist);
        artistReviewService.create(artistReview);

        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editReview(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal, HttpSession session){
        String artistMbid = (String) requestParams.get("mbid");
        String requestURI = "/artist/"+artistMbid;
        String reviewtext = (String) requestParams.get("reviewtext");
        Integer ratings = Integer.parseInt((String) requestParams.get("ratings"));

        Long reviewId = Long.parseLong((String) requestParams.get("artistreviewid"));

        Person reviewer = personService.findByUsername(principal.getName()).orElse(null);
        if(reviewer==null){
            modelAndView.setViewName("redirect:"+requestURI+"?wrongreviewer");
        }


        ArtistReview artistReview = artistReviewService.findReviewById(reviewId).orElse(null);
        if(artistReview != null){
            String currentUserRole = personService.findByUsername(principal.getName()).get().getRole().getName();
            if(artistReview.getArtistReviewer().getUsername().equals(principal.getName()) || currentUserRole.equals("EDITOR") || currentUserRole.equals("ADMIN")){
                artistReview.setRating(ratings);
                artistReview.setReview(reviewtext);
                artistReview.setFlagged(false);
                artistReviewService.create(artistReview);
            }
            else{
                modelAndView.setViewName("redirect:"+requestURI+"?NotAuthorized");
            }

        }

        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;
    }

    @PostMapping("/flag")
    public ModelAndView flagReview(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams) {
        String artistMbid = (String) requestParams.get("mbid");
        String requestURI = "/artist/"+artistMbid;

        Long reviewId = Long.parseLong((String) requestParams.get("artistreviewid"));

        ArtistReview artistReview = artistReviewService.findReviewById(reviewId).orElse(null);
        if(artistReview != null){
            artistReview.setFlagged(true);
            artistReviewService.create(artistReview);
        }
        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteReview(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams,BindingResult bindingResult, Principal principal, HttpSession session) {
        String artistMbid = (String) requestParams.get("mbid");
        String requestURI = "/artist/"+artistMbid;

        Long reviewId = Long.parseLong((String) requestParams.get("artistreviewid"));

        ArtistReview artistReview = artistReviewService.findReviewById(reviewId).orElse(null);

        String currentUserRole = personService.findByUsername(principal.getName()).get().getRole().getName();

        if(artistReview != null && (artistReview.getArtistReviewer().getUsername().equals(principal.getName()) || currentUserRole.equals("EDITOR") || currentUserRole.equals("ADMIN"))){
            artistReviewService.deleteReview(artistReview);
        }
        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;
    }
}