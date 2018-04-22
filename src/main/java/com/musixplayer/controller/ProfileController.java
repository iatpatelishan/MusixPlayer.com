package com.musixplayer.controller;


import com.musixplayer.model.Artist;
import com.musixplayer.model.Person;
import com.musixplayer.model.Review;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
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
    private ArtistService artistService;

    @Autowired
    public ProfileController(PersonService personService, ArtistService artistService) {
        this.personService = personService;
        this.artistService = artistService;
    }

    @GetMapping("/{username}/")
    public ModelAndView getProfileDetails(ModelAndView modelAndView, @PathVariable("username") String username, Principal principal) {
        Person profile = personService.findByUsername(username).orElse(null);
        if(profile.getRole().getName().equals("ARTIST")){
            Artist profileartist = artistService.findByUsername(username).orElse(null);
            System.out.println("ARTIST PROFILE REQUESTED");
            modelAndView.addObject("profile", profileartist);
        }
        else{
            modelAndView.addObject("profile", profile);
        }


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

    @PostMapping("/{username}/edit")
    public ModelAndView editProfileDetails(ModelAndView modelAndView, RedirectAttributes redir, @PathVariable("username") String username, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal) {
        String requestURI = "/profile/" + username + "/edit";

        String email = (String) requestParams.get("email");
        String firstName = (String) requestParams.get("firstname");
        String lastName = (String) requestParams.get("lastname");

        Person loggedinPerson = personService.findByUsername(principal.getName()).orElse(null);
        Person toEditPerson = personService.findByUsername(username).orElse(null);

        if(loggedinPerson.getUsername().equals(toEditPerson.getUsername())  || loggedinPerson.getRole().equals("ADMIN")){
            if(email!=null){
                Person emailExists = personService.findByEmail(email).orElse(null);
                if (emailExists != null && !emailExists.getUsername().equals(toEditPerson.getUsername())) {
                    modelAndView.addObject("alreadyRegisteredMessage", "Oops!  This email has been used already!");
                    modelAndView.addObject("profile", toEditPerson);
                    modelAndView.addObject("mxuser", loggedinPerson);
                    modelAndView.setViewName("editprofile");
                    bindingResult.reject("email");
                    return modelAndView;
                }
                toEditPerson.setEmail(email);
                String emailHash;
                try {
                    MessageDigest m=MessageDigest.getInstance("MD5");
                    m.update(email.getBytes(),0,email.length());
                    emailHash = new BigInteger(1,m.digest()).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    emailHash="";
                }
                toEditPerson.setEmailHash(emailHash);
            }
            if(firstName!=null){
                toEditPerson.setFirstName(firstName);
            }
            if(lastName!=null){
                toEditPerson.setLastName(lastName);
            }
            personService.create(toEditPerson);
        }

        modelAndView.setViewName("redirect:" + requestURI);
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
    public ModelAndView deleteProfile(@PathVariable("username") String username, ModelAndView modelAndView, BindingResult bindingResult, Principal principal, HttpSession session, HttpServletRequest request) {

        String requestURI="/";
        Person person = personService.findByUsername(principal.getName()).orElse(null);
        String currentUserRole = person.getRole().getName();

        if (username.equals(principal.getName())||currentUserRole.equals("ADMIN")){

            if(username.equals(principal.getName())){

                request.getSession().invalidate();
            }

            personService.deletePerson(username);
            modelAndView.setViewName("redirect:"+requestURI);
        }

        else
        {
            modelAndView.setViewName("redirect:"+requestURI+"?wronguser");
        }

        return modelAndView;
    }
}
