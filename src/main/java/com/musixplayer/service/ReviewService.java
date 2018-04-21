package com.musixplayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Lyrics;
import com.musixplayer.model.Review;
import com.musixplayer.model.Song;
import com.musixplayer.repository.ReviewRepository;
import com.musixplayer.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SongService songService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, SongService songService) {
        this.reviewRepository = reviewRepository;
        this.songService = songService;
    }


    public Review create(Review review) {
        return reviewRepository.save(review);

    }

    public Collection<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findReviewById(Long id) {
        if(id==null){
            return null;
        }
        return reviewRepository.findById(id);
    }


    public void deleteReview(Review review){

        Long reviewId = review.getId();

        if(reviewId != null){
            reviewRepository.deleteById(reviewId);
        }
    }

}