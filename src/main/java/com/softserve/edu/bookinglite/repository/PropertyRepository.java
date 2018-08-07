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

	@Query("SELECT p from Property p where p.name = :name")
	public List<Property> getPropertyByName(@Param("name") String name);

	@Query("SELECT p FROM Property p JOIN p.address a JOIN a.city c "
			+ "WHERE lower(c.name) = :name")
	public List<Property> getAllPropertyByCityName(@Param("name") String name);

	@Query("SELECT p FROM Property p JOIN p.address "
			+ "a JOIN a.city c JOIN c.country ct WHERE lower(ct.name) = :name")
	public List<Property> getAllPropertyByCountryName(@Param("name") String name);

}
