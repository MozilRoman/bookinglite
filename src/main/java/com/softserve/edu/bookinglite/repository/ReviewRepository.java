package com.softserve.edu.bookinglite.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.softserve.edu.bookinglite.entity.Review;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.booking.id=?1")
    Review findByBookingId(Long bookingId);

    @Query("select r from Review r join r.booking b join b.apartment a join a.property p where p.id=?1")
    Page<Review> findPageAllReviewsByIdProperty(Long propertyId, Pageable pageable);

    @Query("select count(r) from Review r join r.booking b join b.apartment a join a.property p where p.id=?1")
    int countAllReviewsByIdProperty(Long propertyId);

    @Query("select r from Review r join r.booking b join b.apartment a join a.property p where p.id=?1")
    List<Review> findAllReviewsByIdProperty(Long propertyId);

    @Query("select avg (r.rating)from Review r join r.booking b join b.apartment a join a.property p where p.id=?1")
    float findAvgRankingByIdProperty(Long propertyId);
}
