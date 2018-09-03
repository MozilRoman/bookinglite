package com.softserve.edu.bookinglite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Booking;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("Select b from Booking b where user.id= ?1 and b.id= ?2 ")
    Booking findBookingById(Long idUser, Long bookingId);
    
    @Query("Select b from Booking b where user.id= ?1 ORDER BY b.checkIn desc")
    Page<Booking> getPageAllBookingsByUserId(Long idUser, Pageable pageable);
    
    @Query("Select b from Booking b where user.id= ?1 and b.checkOut >?2 ORDER BY b.checkIn desc")
    Page<Booking> getPageActualBookingsByUserId(Long idUser, Date nowDate, Pageable pageable);
    
    @Query("Select b from Booking b where user.id= ?1 and b.checkOut <?2 ORDER BY b.checkIn desc")
    Page<Booking> getPageArchieveBookingsByUserId(Long idUser, Date nowDate, Pageable pageable);
    
    //if booking exist it will return true
    @Query("select CASE WHEN COUNT(b.id) > 0 THEN TRUE ELSE FALSE END  from Booking b " +
            "where  b.apartment.id=?1 and b.bookingStatus.name<>'Canceled' " +
            "and (?2 between b.checkIn and b.checkOut or ?3 between b.checkIn and b.checkOut " +
            "or b.checkIn between ?2 and ?3 or b.checkOut between ?2 and ?3)")
    boolean getBookingByCheck(Long apartment_id, Date inDate, Date outDate);


    @Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
            + "join Property p on p.id=a.property.id "
            + "where p.user.id=?1 and b.bookingStatus.name<>'Canceled' order by b.checkIn desc ")
    List<Booking> getAllBookingsByOwnerId(Long idOwnerUser);

    @Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
            + "join Property p on p.id=a.property.id "
            + "where p.user.id=?1 and b.bookingStatus.name<>'Canceled' order by b.checkIn desc ")
    Page<Booking> getPageAllBookingsByOwnerId(Long idOwnerUser, Pageable pageable);
}

	
