package com.musixplayer.controller;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.Song;
import com.musixplayer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private Environment env;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private PersonService personService;
    private EmailService emailService;
    private RoleService roleService;
    private UserService userService;
    private AdminService adminService;
    private EditorService editorService;
    private ArtistService artistService;
    private ArtistDataService artistDataService;
    private SongService songService;
    private Top500SongsService top500SongsService;



    @Autowired
    public AdminController(Environment env, BCryptPasswordEncoder bCryptPasswordEncoder, PersonService personService, EmailService emailService, RoleService roleService, ArtistDataService artistDataService, UserService userService, ArtistService artistService, EditorService editorService, AdminService adminService, SongService songService, Top500SongsService top500SongsService) {
        this.env = env;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.personService = personService;
        this.emailService = emailService;
        this.roleService = roleService;
        this.userService = userService;
        this.editorService = editorService;
        this.adminService = adminService;
        this.artistService = artistService;
        this.artistDataService = artistDataService;
        this.songService = songService;
        this.top500SongsService = top500SongsService;

    }



    @GetMapping("/")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView){
        modelAndView.addObject("listOfUsers", adminService.findAllPerson());
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PostMapping("/fetchtop500songs")
    public ModelAndView fetchTop500Song(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams){
        String country =  (String) requestParams.get("country");
        String uri = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country="+country+"&format=json&api_key=ecc8416e699592148ea368f3a381a819";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        try {
            JsonNode songsNode = new ObjectMapper().readTree(result).get("tracks").get("track");

            Iterator<JsonNode> iterator = songsNode.elements();
            top500SongsService.deleteAllByCountry(country);

            while(iterator.hasNext()) {

                JsonNode jsonsong = iterator.next();
                String songmbId = jsonsong.get("mbid").textValue();
                String artistmbId = jsonsong.get("artist").get("mbid").textValue();

                if(songmbId.length()> 0 && artistmbId.length()>0) {
                    Song song = songService.fetchSongandAdd(songmbId);
                    top500SongsService.create(song,country);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        redir.addFlashAttribute("successMessage", "Successfully Fetched "+result.length());
        modelAndView.setViewName("redirect:/admin/");
        return modelAndView;
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}