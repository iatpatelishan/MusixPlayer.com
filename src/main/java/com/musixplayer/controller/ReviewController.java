package com.musixplayer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.Person;
import com.musixplayer.model.Review;
import com.musixplayer.model.Song;
import com.musixplayer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;


@Controller
@RequestMapping("/review")
public class ReviewController {

    private SongService songService;
    private ReviewService reviewService;
    private PersonService personService;

    @Autowired
    public ReviewController(SongService songService, ReviewService reviewService, PersonService personService) {
        this.songService = songService;
        this.reviewService = reviewService;
        this.personService = personService;
    }

    @PostMapping("/add")
    public ModelAndView addReview(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal, HttpSession session){


        String songMbid = (String) requestParams.get("mbid");
        String requestURI = "/song/"+songMbid;
        String reviewtext = (String) requestParams.get("reviewtext");
        Integer ratings = Integer.parseInt((String) requestParams.get("ratings"));

        Song song = songService.findSongByMbid(songMbid).orElse(null);
        if(song==null){
            modelAndView.setViewName("redirect:"+requestURI+"?wrongmbid");
        }

        Person reviewer = personService.findByUsername(principal.getName()).orElse(null);
        if(reviewer==null){
            modelAndView.setViewName("redirect:"+requestURI+"?wrongreviewer");
        }

        if(reviewer.getReviews() != null ){
            for(Review r : reviewer.getReviews()){
                if(r.getSong().getMbId().equals(songMbid)){
                    r.setRating(ratings);
                    r.setReview(reviewtext);
                    r.setFlagged(false);
                    reviewService.create(r);
                    modelAndView.setViewName("redirect:"+requestURI);
                    return modelAndView;
                }
            }
        }

        Review review = new Review();
        review.setRating(ratings);
        review.setReview(reviewtext);
        review.setReviewer(reviewer);
        review.setFlagged(false);
        review.setSong(song);
        reviewService.create(review);

        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editReview(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal, HttpSession session){
        String songMbid = (String) requestParams.get("mbid");
        String requestURI = "/song/"+songMbid;
        String reviewtext = (String) requestParams.get("reviewtext");
        Integer ratings = Integer.parseInt((String) requestParams.get("ratings"));

        Long reviewId = Long.parseLong((String) requestParams.get("reviewid"));

        Person reviewer = personService.findByUsername(principal.getName()).orElse(null);
        if(reviewer==null){
            modelAndView.setViewName("redirect:"+requestURI+"?wrongreviewer");
        }


        Review review = reviewService.findReviewById(reviewId).orElse(null);
        if(review != null){
            if(review.getReviewer().getUsername()==principal.getName() || reviewer.getRole().getName().equals("EDITOR") || reviewer.getRole().getName().equals("ADMIN")){
                review.setRating(ratings);
                review.setReview(reviewtext);
                review.setFlagged(true);
                reviewService.create(review);
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
        String songMbid = (String) requestParams.get("mbid");
        String requestURI = "/song/"+songMbid;

        Long reviewId = Long.parseLong((String) requestParams.get("reviewid"));

        Review review = reviewService.findReviewById(reviewId).orElse(null);
        if(review != null){
            review.setFlagged(true);
            reviewService.create(review);
        }
        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;
    }
}