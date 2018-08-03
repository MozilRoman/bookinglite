package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
@Query("Select b from Booking b where user.id= ?1 ORDER BY b.check_in")
    List<Booking> getAllByUserIdOrderByCheck_inAsc(Long id_user);
}
