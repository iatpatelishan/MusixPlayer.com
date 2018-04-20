package com.musixplayer.controller;

import com.musixplayer.model.*;
import com.musixplayer.repository.UserRepository;
import com.musixplayer.service.*;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.security.*;
import java.math.*;

@Controller
public class RegisterController {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private PersonService personService;
    private EmailService emailService;
    private RoleService roleService;
    private ArtistDataService artistDataService;
    private UserService userService;
    private ArtistService artistService;
    private AdminService adminService;
    private EditorService editorService;

    @Autowired
    public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder, PersonService personService, EmailService emailService, RoleService roleService, ArtistDataService artistDataService, UserService userService, ArtistService artistService, EditorService editorService, AdminService adminService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.personService = personService;
        this.emailService = emailService;
        this.roleService = roleService;
        this.artistDataService = artistDataService;
        this.userService = userService;
        this.artistService = artistService;
        this.editorService = editorService;
        this.adminService = adminService;
    }


    @GetMapping("/login")
    public ModelAndView showLoginPage(ModelAndView modelAndView, Person person){
        modelAndView.addObject("user", person);
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @GetMapping("/register")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, Person person){
        modelAndView.addObject("user", person);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid Person person, BindingResult bindingResult, HttpServletRequest request, @RequestParam Map requestParams) {
        System.out.println("tryingTOREGISTER");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }



        String usertype = (String) requestParams.get("usertype");

        String email = (String) requestParams.get("email");
        String emailHash;
        try {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(email.getBytes(),0,email.length());
            emailHash = new BigInteger(1,m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            emailHash="";
        }
        String firstName = (String) requestParams.get("firstName");
        String lastName = (String) requestParams.get("lastName");

        String confirmationToken = UUID.randomUUID().toString();

        Person personExists = personService.findByEmail(email).orElse(null);

        if (personExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  This email has been used already!");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
            return modelAndView;
        }

        if(usertype.equals("user")){
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setEmailHash(emailHash);
            user.setConfirmationToken(confirmationToken);
            user.setEnabled(false);
            user.setRole(roleService.findByRoleName("USER"));
            userService.create(user);
        } else if (usertype.equals("artist")) {
            String mbid = (String) requestParams.get("mbid");
            ArtistData artistData = artistDataService.fetchArtistDataandAdd(mbid);
            if (artistData==null) {
                modelAndView.addObject("wrongmbidmessage", "Oops!  We cannot find this Mbid. More Info at https://musicbrainz.org/doc/MusicBrainz_Identifier");
                modelAndView.setViewName("register");
                bindingResult.reject("mbid");
                return modelAndView;
            }
            Artist artist = new Artist();
            artist.setFirstName(firstName);
            artist.setLastName(lastName);
            artist.setEmail(email);
            artist.setEmailHash(emailHash);
            artist.setConfirmationToken(confirmationToken);
            artist.setEnabled(false);
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
            editor.setEnabled(false);
            editor.setRole(roleService.findByRoleName("EDITOR"));
            editorService.create(editor);
        } else if (usertype.equals("admin")){
            Admin admin = new Admin();
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(email);
            admin.setEmailHash(emailHash);
            admin.setConfirmationToken(confirmationToken);
            admin.setEnabled(false);
            admin.setRole(roleService.findByRoleName("ADMIN"));
            adminService.create(admin);
        }
        else {
            modelAndView.addObject("badPersonType", "Oops!  You cannot register account of this type!");
            modelAndView.setViewName("register");
            bindingResult.reject("usertype");
            return modelAndView;
        }



        String appUrl = request.getScheme() + "://" + request.getServerName();
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(person.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                + appUrl + "/confirm?token=" + confirmationToken);
        registrationEmail.setFrom("no-reply@musixplayer.com");
        emailService.sendEmail(registrationEmail);

        modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + person.getEmail());
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping("/confirm")
    public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

        Person person = personService.findByConfirmationToken(token);
        if (person == null) { // No token found in DB
            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            modelAndView.addObject("confirmationToken", person.getConfirmationToken());
        }

        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    @PostMapping(value="/confirm")
    public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes redir) {
        modelAndView.setViewName("confirm");
        String username= (String) requestParams.get("username");
        Person personExists = personService.findByUsername(username).orElse(null);

        if (personExists != null) {
            bindingResult.reject("username");
            redir.addFlashAttribute("errorMessage", "Username already Exists.");
            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return modelAndView;
        }

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure((String) requestParams.get("password"));
        if (strength.getScore() < 3) {
            bindingResult.reject("password");

            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");
            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return modelAndView;
        }

        Person person = personService.findByConfirmationToken((String) requestParams.get("token"));

        // Set new password

        String password = (String) requestParams.get("password");
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        System.out.println(password);
        System.out.println(encryptedPassword);


        person.setEnabled(true);
        person.setUsername(username);
        person.setPassword(encryptedPassword);
        // UNCOMMENT BELOW LINE FOR PRODUCTION
        //person.setConfirmationToken(null);
        personService.create(person);

        modelAndView.addObject("successMessage", "Your password has been set!");
        return modelAndView;
    }
}
