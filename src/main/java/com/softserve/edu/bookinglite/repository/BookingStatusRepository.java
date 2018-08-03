package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.BookingStatus;
@Repository
public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long>{

	BookingStatus findByName(String name);
}
