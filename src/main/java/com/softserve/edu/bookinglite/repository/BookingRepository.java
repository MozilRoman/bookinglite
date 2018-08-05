package com.softserve.edu.bookinglite.repository;

import com.softserve.edu.bookinglite.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Booking;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
@Query("Select b from Booking b where user.id= ?1 ORDER BY b.check_in")
    List<Booking> getAllByUserIdOrderByCheck_inAsc(Long id_user);

    @Query("select b.apartment from Booking b where b.apartment.id=?1 and b.check_in <= ?2 and b.check_out>= ?2")
    Apartment getBookingByCheck(Long apartment_id, Date checkDate);

}
