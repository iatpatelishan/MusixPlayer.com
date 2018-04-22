package com.musixplayer.controller;


import com.musixplayer.model.*;
import com.musixplayer.service.ArtistService;
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
import javax.servlet.http.HttpServletResponse;
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
    private PlaylistService playlistService;
    private SongService songService;

    @Autowired
    public ProfileController(PersonService personService, ArtistService artistService, PlaylistService playlistService, SongService songService) {
        this.personService = personService;
        this.artistService = artistService;
        this.playlistService = playlistService;
        this.songService = songService;
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
    public ModelAndView getEditProfileDetails(ModelAndView modelAndView, @PathVariable("username") String username, Principal principal, RedirectAttributes redir, HttpServletResponse response) {
        String requestURI = "/profile/" + username + "/edit";
        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser!=null && (mxuser.getRole().getName().equals("ADMIN") || profile.getUsername().equals(mxuser.getUsername()))) {
                modelAndView.addObject("mxuser", mxuser);
                modelAndView.setViewName("editprofile");
                return modelAndView;
            }
            else {
                modelAndView.setViewName("error/403");
                return modelAndView;
            }
        }

        modelAndView.setViewName("redirect: "+requestURI);
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

    @GetMapping("/{username}/playlist/create")
    public ModelAndView getCreatePlaylist(ModelAndView modelAndView, @PathVariable("username") String username, Principal principal) {
        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser!=null && (mxuser.getRole().getName().equals("ADMIN") || profile.getUsername().equals(mxuser.getUsername()))) {
                modelAndView.addObject("mxuser", mxuser);
                modelAndView.setViewName("createplaylist");
                return modelAndView;
            }
            else {
                modelAndView.setViewName("error/403");
                return modelAndView;
            }
        }

        modelAndView.setViewName("profile");
        return modelAndView;
    }


    @PostMapping("/{username}/playlist/create")
    public ModelAndView createPlaylist(ModelAndView modelAndView, RedirectAttributes redir, @PathVariable("username") String username, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal) {
        String requestURI = "/profile/" + username + "/";
        String name = (String) requestParams.get("playlistname");

        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser != null && name != null && (mxuser.getRole().getName().equals("ADMIN") || profile.getUsername().equals(mxuser.getUsername()))) {
                Playlist playlist = new Playlist();
                playlist.setName(name);
                playlist.setCreatedBy(profile);
                playlist=playlistService.create(playlist);
            } else {
                modelAndView.setViewName("error/403");
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:"+requestURI);
        return modelAndView;

    }


    @PostMapping("/{username}/playlist/addsong")
    public ModelAndView addSongToPlaylist(ModelAndView modelAndView, RedirectAttributes redir, @PathVariable("username") String username, @RequestParam Map requestParams, BindingResult bindingResult, Principal principal) {
        String playlistReqId = (String) requestParams.get("playlistid");
        if(playlistReqId==null){
            modelAndView.setViewName("error/403");
            return modelAndView;
        }
        Long playlistId = Long.parseLong((String) requestParams.get("playlistid"));
        String songMbid = (String) requestParams.get("mbid");
        String requestURI = "/playlist/" + playlistId + "/";

        Person profile = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("profile", profile);

        Playlist playlist = playlistService.findById(playlistId).orElse(null);

        Song song = songService.findSongByMbid(songMbid).orElse(null);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Person mxuser = personService.findByUsername(principal.getName()).orElse(null);
            if (mxuser != null && playlist != null && song!=null && profile.getUsername().equals(mxuser.getUsername())) {
                if(playlist.getSongs()==null){
                    playlist.setSongs(new ArrayList<Song>());
                }
                if(!playlist.getSongs().contains(song)){
                    playlist.getSongs().add(song);
                }
                playlist=playlistService.create(playlist);
            } else {
                modelAndView.setViewName("error/403");
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:"+requestURI);
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
