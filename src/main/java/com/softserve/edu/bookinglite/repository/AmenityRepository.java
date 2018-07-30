package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Amenity;
@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

}
