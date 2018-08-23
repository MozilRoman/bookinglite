package com.softserve.edu.bookinglite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	@Query("SELECT c from City c where c.country.id = :id")
	List<City> getAllCitiesByCountryId(@Param("id") Long id);
}
