package com.softserve.edu.bookinglite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	
	public Property findByName(String name);

	
	@Query("SELECT p FROM Property p inner join p.address")
	public List<Property> getAllPropertyByCityName(@Param("") String name);
	

}
