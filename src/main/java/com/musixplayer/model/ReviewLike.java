/*
package com.musixplayer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReviewLike implements Serializable {
    @Column(name = "review_Id")
    @Getter @Setter
    private Long reviewId;

    @Column(name = "person_Id")
    @Getter @Setter
    private Long personId;

    public ReviewLike(Long reviewId, Long personId) {
        this.reviewId = reviewId;
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ReviewLike that = (ReviewLike) o;
        return Objects.equals(reviewId, that.reviewId) &&
                Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, personId);
    }
}
*/
