package com.musixplayer.apiDto.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

public class youtube {
    public static void main(String[] args) throws IOException {
        String uri = "https://www.googleapis.com/youtube/v3/search?q=Believer&maxResults=5&part=snippet&key=AIzaSyBvxuIQWp4wQkY0S9X80G9J9wmhVhYRWvg";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
    }
}
