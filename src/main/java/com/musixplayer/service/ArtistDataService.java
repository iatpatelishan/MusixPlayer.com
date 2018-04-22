package com.musixplayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.ArtistData;
import com.musixplayer.repository.ArtistDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Service
public class ArtistDataService {

    private final ArtistDataRepository artistDataRepository;
    private final ProxyService proxyService;

    @Autowired
    public ArtistDataService(ArtistDataRepository artistDataRepository, ProxyService proxyService) {
        this.artistDataRepository = artistDataRepository;
        this.proxyService = proxyService;
    }

    public Optional<ArtistData> findByMbid(String mbid) {
        return artistDataRepository.findByMbId(mbid);
    }

    public Collection<ArtistData> findAll() {
        return artistDataRepository.findAll();
    }

    public ArtistData create(ArtistData artistData) {
        return artistDataRepository.save(artistData);
    }


    /*public ArtistData fetchArtistDataandAdd(ArtistData artistData) {
        String mbid = artistData.getMbId();
        Optional<ArtistData> checkartistdata = artistDataRepository.findByMbId(mbid);
        if(checkartistdata.isPresent()){
            return checkartistdata.get();
        } else{
            artistData = artistDataRepository.save(artistData);
            return artistData;
        }
    }*/

    public ArtistData fetchArtistDataandAdd(String mbid) {
        if(mbid==null){
            return null;
        }
        if(mbid.length()!=36){
            return null;
        }
        // if exists, then return existing
        Optional<ArtistData> checkartistdata = findByMbid(mbid);
        if (checkartistdata.isPresent()) {
            return checkartistdata.get();
        }

        String result = (String) proxyService.searchArtistDataInLastfmByMbId(mbid);
        ArtistData artistData = new ArtistData();

        try {
            JsonNode jsonartist = new ObjectMapper().readTree(result);
            if(jsonartist.has("error")){
                return null;
            }
            jsonartist =  jsonartist.get("artist");
            String name = jsonartist.get("name").textValue();
            String url = jsonartist.get("url").textValue();
            String imageUrl = null;
            Iterator<JsonNode> images = jsonartist.get("image").elements();
            if (jsonartist.has("image")) {
                while (images.hasNext()) {
                    JsonNode imagenode = images.next();
                    imageUrl = imagenode.get("#text").textValue();
                }
            }
            String description = "";
            if (jsonartist.has("bio") && jsonartist.get("bio").has("summary")) {
                description = jsonartist.get("bio").get("summary").textValue();
            }

            artistData.setMbid(mbid);
            artistData.setName(name);
            artistData.setLastfmUrl(url);
            artistData.setImage(imageUrl);
            artistData.setDescription(description);

            return create(artistData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Optional<ArtistData> findArtistDataById(Long id){

        return artistDataRepository.findById(id);
    }
}
