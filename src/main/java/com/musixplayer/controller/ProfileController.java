package com.musixplayer.controller;


import com.musixplayer.model.Person;
import com.musixplayer.model.Review;
import com.musixplayer.service.PersonService;
import com.musixplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private PersonService personService;

    @Autowired
    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{username}/")
    public ModelAndView getProfileDetails(ModelAndView modelAndView, @PathVariable("username") String username, Principal principal) {
        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser != null) {
                if (mxuser.getFollowing() == null) {
                    mxuser.setFollowing(new ArrayList<>());
                }
                if (mxuser.getFollowing().contains(profile)) {
                    modelAndView.addObject("alreadyfollowed", true);
                } else {
                    modelAndView.addObject("alreadyfollowed", false);
                }
                modelAndView.addObject("mxuser", mxuser);
            }
        }

        modelAndView.setViewName("profile");
        return modelAndView;
    }

    @GetMapping("/{username}/edit")
    public ModelAndView getEditProfileDetails(ModelAndView modelAndView, @PathVariable("username") String username, Principal principal, RedirectAttributes redir) {
        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser!=null) {

                modelAndView.addObject("mxuser", mxuser);
                modelAndView.setViewName("editprofile");
                return modelAndView;
            }
        }

        modelAndView.setViewName("redirect: /");
        return modelAndView;
    }

    @GetMapping("/{username}/createplaylist")
    public ModelAndView getCreatePlaylist(ModelAndView modelAndView, @PathVariable("username") String username, Principal principal) {
        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser!=null) {
                if(mxuser.getFollowing()==null){
                    mxuser.setFollowing(new ArrayList<>());
                }
                if(mxuser.getFollowing().contains(profile)){
                    modelAndView.addObject("alreadyfollowed", true);
                }else{
                    modelAndView.addObject("alreadyfollowed", false);
                }
                modelAndView.addObject("mxuser", mxuser);
            }
        }

        modelAndView.setViewName("profile");
        return modelAndView;
    }



    @PostMapping("/followunfollow")
    public ModelAndView followProfile(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams, Principal principal) {
        String followPerson = (String) requestParams.get("followperson");
        String requestURI = "/profile/" + followPerson + "/";

        Person follower = personService.findByUsername(principal.getName()).orElse(null);
        Person following = personService.findByUsername(followPerson).orElse(null);


        if (follower != null && following != null && !(follower.getUsername().equals(following.getUsername()))) {
            if (follower.getFollowing() == null) {
                follower.getFollowing().add(following);
            } else {
                if (follower.getFollowing().contains(following)) {
                    follower.getFollowing().remove(following);
                } else {
                    follower.getFollowing().add(following);
                }
            }
            personService.create(follower);
        }

        modelAndView.setViewName("redirect:" + requestURI);
        return modelAndView;
    }

    @PostMapping("/{username}/delete")
    public void deleteProfile(@PathVariable("username") String username) {
        personService.deletePerson(username);
    }
}
