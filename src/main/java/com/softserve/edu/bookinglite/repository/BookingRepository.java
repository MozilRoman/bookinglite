package com.softserve.edu.bookinglite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.softserve.edu.bookinglite.entity.Booking;

import java.util.Date;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("Select b from Booking b where user.id= ?1 and b.id= ?2 ")
    public Booking findBookingById(Long idUser, Long bookingId);
    
    @Query("Select b from Booking b where user.id= ?1 ORDER BY b.checkIn desc")
    public Page<Booking> getPageAllBookingsByUserId(Long idUser, Pageable pageable);
    
    @Query("Select b from Booking b where user.id= ?1 and b.checkOut >?2 ORDER BY b.checkIn desc")
    public Page<Booking> getPageActualBookingsByUserId(Long idUser, Date nowDate, Pageable pageable);
    
    @Query("Select b from Booking b where user.id= ?1 and b.checkOut <?2 ORDER BY b.checkIn desc")
    public Page<Booking> getPageArchieveBookingsByUserId(Long idUser, Date nowDate, Pageable pageable);
    
    //if booking exist it will return true
    @Query("select CASE WHEN COUNT(b.id) > 0 THEN TRUE ELSE FALSE END  from Booking b " +
            "where  b.apartment.id=?1 and b.bookingStatus.name<>'Canceled' " +
            "and (?2 between b.checkIn and b.checkOut or ?3 between b.checkIn and b.checkOut " +
            "or b.checkIn between ?2 and ?3 or b.checkOut between ?2 and ?3)")
    public boolean isApartmentBookedWithinDateRange(Long apartment_id, Date inDate, Date outDate);

    @Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
            + "join Property p on p.id=a.property.id "
            + "where p.user.id=?2 and p.id=?1 and b.bookingStatus.name<>'Canceled'" +
            " and ?3 between b.checkIn and b.checkOut order by b.checkOut desc ")
    public Page<Booking> getActiveBookingsByPropertyAndOwnerId(Long propertyId, Long idOwnerUser,Date actualDate,Pageable pageable);

    @Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
            + "join Property p on p.id=a.property.id "
            + "where p.user.id=?2 and p.id=?1 and b.bookingStatus.name<>'Canceled'" +
            " and ?3 > b.checkOut  order by b.checkOut desc ")
    public Page<Booking> getPastBookingsByPropertyAndOwnerId(Long propertyId, Long idOwnerUser,Date actualDate,Pageable pageable);

    @Query("SELECT b FROM Booking b " + "join Apartment a on a.id=b.apartment.id "
            + "join Property p on p.id=a.property.id "
            + "where p.user.id=?2 and p.id=?1 and b.bookingStatus.name<>'Canceled'" +
            " and ?3 < b.checkIn  order by b.checkOut desc ")
    public Page<Booking> getFutureBookingsByPropertyAndOwnerId(Long propertyId, Long idOwnerUser,Date actualDate,Pageable pageable);
}

	
