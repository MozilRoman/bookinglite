package com.softserve.edu.bookinglite.repository;

import com.softserve.edu.bookinglite.entity.Apartment;


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
    List<Booking> getAllByUserIdOrderByCheckInAsc(Long idUser);
	
	@Query("Select b from Booking b where user.id= ?1 ORDER BY b.checkIn desc")
    Page<Booking> getPageAllByUserIdOrderByCheckInAsc(Long idUser, Pageable pageable);

    @Query("select b.apartment from Booking b where b.apartment.id=?1 and b.bookingStatus.name<>'Canceled' and b.checkIn <= ?2 and b.checkOut>= ?2 or  b.checkIn <= ?3 and b.checkOut>= ?3 ")
    Apartment getBookingByCheck(Long apartment_id, Date in,Date out);
    @Query("select b.apartment from Booking b where b.apartment.id=?1 and b.bookingStatus.name<>'Canceled' and b.checkIn  between ?2 and ?3 " +
            "or b.bookingStatus.id<3 and b.checkOut  between ?2 and ?3")
    Apartment checkBookingsExistsByDateInAndDateOut(Long apartmentid, Date in,Date out);



	@Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
			+ "join Property p on p.id=a.property.id "
			+ "where p.user.id=?1 and b.bookingStatus.name<>'Canceled' order by b.checkIn desc ")
	List<Booking> getAllBookingsByOwnerId(Long idOwnerUser);

	@Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
			+ "join Property p on p.id=a.property.id "
			+ "where p.user.id=?1 and b.bookingStatus.name<>'Canceled' order by b.checkIn desc ")
	Page<Booking> getPageAllBookingsByOwnerId(Long idOwnerUser, Pageable pageable);
}

	
