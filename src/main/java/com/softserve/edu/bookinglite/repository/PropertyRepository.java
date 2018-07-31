package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.bookinglite.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	
	public Property findByName(String name);
	
//	@Query("Select p from Property p where  ")
//	public List<Property> findByCountryId(Long id){
		
//	}
}
