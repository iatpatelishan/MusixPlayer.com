package com.musixplayer.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.*;
import com.musixplayer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailService emailService;
    private RoleService roleService;
    private PersonService personService;
    private UserService userService;
    private AdminService adminService;
    private EditorService editorService;
    private ArtistService artistService;
    private ArtistDataService artistDataService;
    private SongService songService;
    private Top500SongsService top500SongsService;


    @Autowired
    public AdminController(BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, RoleService roleService, PersonService personService, ArtistDataService artistDataService, UserService userService, ArtistService artistService, EditorService editorService, AdminService adminService, SongService songService, Top500SongsService top500SongsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.roleService = roleService;
        this.personService = personService;
        this.userService = userService;
        this.editorService = editorService;
        this.adminService = adminService;
        this.artistService = artistService;
        this.artistDataService = artistDataService;
        this.songService = songService;
        this.top500SongsService = top500SongsService;
    }


    @GetMapping("/")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView) {
        modelAndView.addObject("listOfUsers", personService.findAll());
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/createuser")
    public ModelAndView showCreateUserPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/createuser");
        return modelAndView;
    }

    @PostMapping("/createuser")
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid Person person, BindingResult
            bindingResult, HttpServletRequest request, @RequestParam Map requestParams, RedirectAttributes redir) {
        System.out.println("tryingTOREGISTER");


        String firstName = (String) requestParams.get("firstName");
        String lastName = (String) requestParams.get("lastName");
        String email = (String) requestParams.get("email");
        String emailHash;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(email.getBytes(), 0, email.length());
            emailHash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            emailHash = "";
        }

        String username= (String) requestParams.get("username");
        String password = (String) requestParams.get("password");
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        String confirmationToken = UUID.randomUUID().toString();
        String usertype = (String) requestParams.get("usertype");

        Person emailExists = personService.findByEmail(email).orElse(null);

        if (emailExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  This email has been used already!");
            modelAndView.setViewName("admin/createuser");
            bindingResult.reject("email");
            return modelAndView;
        }

        Person usernameExists = personService.findByUsername(username).orElse(null);

        if (usernameExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Username already Exists.");
            modelAndView.setViewName("admin/createuser");
            bindingResult.reject("username");
            return modelAndView;
        }

        if (usertype.equals("user")) {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setEmailHash(emailHash);
            user.setConfirmationToken(confirmationToken);
            user.setEnabled(true);
            user.setUsername(username);
            user.setPassword(encryptedPassword);
            user.setRole(roleService.findByRoleName("USER"));
            userService.create(user);
        } else if (usertype.equals("artist")) {
            String mbid = (String) requestParams.get("mbid");
            ArtistData artistData = artistDataService.fetchArtistDataandAdd(mbid);
            if (artistData == null) {
                modelAndView.addObject("wrongmbidmessage", "Oops!  We cannot find this Mbid. More Info at https://musicbrainz.org/doc/MusicBrainz_Identifier");
                modelAndView.setViewName("admin/createuser");
                bindingResult.reject("mbid");
                return modelAndView;
            }
            Artist artist = new Artist();
            artist.setFirstName(firstName);
            artist.setLastName(lastName);
            artist.setEmail(email);
            artist.setEmailHash(emailHash);
            artist.setConfirmationToken(confirmationToken);
            artist.setEnabled(true);
            artist.setUsername(username);
            artist.setPassword(encryptedPassword);
            artist.setArtistData(artistData);
            artist.setRole(roleService.findByRoleName("ARTIST"));
            artistService.create(artist);
        } else if (usertype.equals("editor")) {
            Editor editor = new Editor();
            editor.setFirstName(firstName);
            editor.setLastName(lastName);
            editor.setEmail(email);
            editor.setEmailHash(emailHash);
            editor.setConfirmationToken(confirmationToken);
            editor.setEnabled(true);
            editor.setUsername(username);
            editor.setPassword(encryptedPassword);
            editor.setRole(roleService.findByRoleName("EDITOR"));
            editorService.create(editor);
        } else if (usertype.equals("admin")) {
            Admin admin = new Admin();
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(email);
            admin.setEmailHash(emailHash);
            admin.setConfirmationToken(confirmationToken);
            admin.setEnabled(true);
            admin.setUsername(username);
            admin.setPassword(encryptedPassword);
            admin.setRole(roleService.findByRoleName("ADMIN"));
            adminService.create(admin);
        } else {
            modelAndView.addObject("badPersonType", "Oops!  You cannot register account of this type!");
            modelAndView.setViewName("admin/createuser");
            bindingResult.reject("usertype");
            return modelAndView;
        }


        redir.addFlashAttribute("successMessage", "Successfully created user ");
        modelAndView.setViewName("redirect:/admin/");
        return modelAndView;
    }

    @PostMapping("/fetchtop500songs")
    public ModelAndView fetchTop500Song(ModelAndView modelAndView, RedirectAttributes redir, @RequestParam Map requestParams) {
        String country = (String) requestParams.get("country");
        String uri = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=" + country + "&format=json&api_key=ecc8416e699592148ea368f3a381a819";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        try {
            JsonNode songsNode = new ObjectMapper().readTree(result).get("tracks").get("track");

            Iterator<JsonNode> iterator = songsNode.elements();

            top500SongsService.emptyAllSongsByCountry(country);

            while (iterator.hasNext()) {

                JsonNode jsonsong = iterator.next();
                String songmbId = jsonsong.get("mbid").textValue();
                String artistmbId = jsonsong.get("artist").get("mbid").textValue();

                if (songmbId.length() > 0 && artistmbId.length() > 0) {
                    Song song = songService.fetchSongandAdd(songmbId);
                    top500SongsService.create(song, country);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        redir.addFlashAttribute("successMessage", "Successfully Fetched " + result.length());
        modelAndView.setViewName("redirect:/admin/");
        return modelAndView;
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}