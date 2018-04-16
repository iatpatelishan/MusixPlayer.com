package com.musixplayer.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping("/{userid}")
    public ModelAndView getProfileDetails(ModelAndView modelAndView, @PathVariable("userId") String userid) {
        modelAndView.setViewName("profile");
        return modelAndView;
    }

}
