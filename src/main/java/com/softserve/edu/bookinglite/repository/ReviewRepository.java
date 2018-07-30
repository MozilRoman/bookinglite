package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Review;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
