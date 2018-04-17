package com.musixplayer.restservice;

import com.musixplayer.repository.AdminRepository;
import com.musixplayer.repository.RoleRepository;
import com.musixplayer.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class ProxyController {

    private final ProxyService proxyService;

    @Autowired
    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/api/search")
    public Object searchSongLastfm(@RequestParam(name="search", required=false) String searchword, @RequestParam(name="type", required=false) String searchtype) {
        return proxyService.searchSongLastfm(searchword,searchtype);
    }

    @GetMapping(value = "/api/search/youtube", produces = "application/json; charset=UTF-8")
    public Object searchSongYoutube(@RequestParam(name="search", required=false) String searchword) {
       return proxyService.searchSongYoutube(searchword);
    }

}
