package com.softserve.edu.bookinglite.service.dto;

import java.util.Objects;

public class CreateReviewDto {
    private String message;
    private Float rating;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateReviewDto that = (CreateReviewDto) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, rating);
    }
}
