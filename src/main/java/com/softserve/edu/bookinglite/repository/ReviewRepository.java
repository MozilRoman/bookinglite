package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.booking.id=?1")
    Review findByBookingId(Long bookingId);

    @Query("select r from Review r join r.booking b join b.apartment a join a.property p where p.id=?1")
    List<Review> findAllReviewsByIdProperty (Long propertyId);

}
