package com.musixplayer.service;

import com.musixplayer.model.ArtistReview;
import com.musixplayer.model.Review;
import com.musixplayer.repository.ArtistReviewRepository;
import com.musixplayer.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ArtistReviewService {

    private final ArtistReviewRepository artistReviewRepository;
    private final ArtistDataService artistDataService;

    @Autowired
    public ArtistReviewService(ArtistReviewRepository artistReviewRepository, ArtistDataService artistDataService) {
        this.artistReviewRepository = artistReviewRepository;
        this.artistDataService = artistDataService;
    }


    public ArtistReview create(ArtistReview artistReview) {
        return artistReviewRepository.save(artistReview);

    }

    public Collection<ArtistReview> findAll() {
        return artistReviewRepository.findAll();
    }

    public Optional<ArtistReview> findReviewById(Long id) {
        if(id==null){
            return null;
        }
        return artistReviewRepository.findById(id);
    }

    public void deleteReview(ArtistReview artistReview){

        Long reviewId = artistReview.getId();

        if(reviewId != null){
            artistReviewRepository.deleteById(reviewId);
        }
    }

}