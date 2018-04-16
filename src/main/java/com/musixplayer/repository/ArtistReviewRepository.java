package com.musixplayer.repository;

import com.musixplayer.model.ArtistReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistReviewRepository extends JpaRepository<ArtistReview, Long> {
}
