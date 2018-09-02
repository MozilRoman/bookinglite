package com.softserve.edu.bookinglite.repository;

import java.math.BigDecimal;
import java.util.Date;
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

    @Query("SELECT p FROM Property p JOIN p.address ad JOIN ad.city c WHERE c.id = ?1")
    public List<Property> getAllPropertyByCityId(Long id);

	@Query("SELECT p FROM Property p JOIN p.address "
			+ "a JOIN a.city c JOIN c.country ct WHERE lower(ct.name) = :name")
	public List<Property> getAllPropertyByCountryName(@Param("name") String name);
	
	
	
	@Query("SELECT DISTINCT p  FROM Property p "
			+ "INNER JOIN Apartment ap ON ap.property.id = p.id "
			+ "INNER JOIN Address addr ON p.address.id = addr.id "
			+ "INNER JOIN City ct ON addr.city.id = ct.id "
			+ "INNER JOIN Country cn ON ct.country.id = cn.id "
			+ "WHERE ap.numberOfGuests >= ?1 "
			+ "AND ((ct.id = ?2 AND cn.id = ?3)) "
			+ "AND ap.id NOT IN ("
			+ "SELECT DISTINCT book FROM Booking book WHERE NOT ( "
			+ "(?4 < book.checkIn AND ?5 < book.checkIn) OR"
			+ "(?4 > book.checkOut AND ?5 > book.checkOut)))")
	public List<Property> searchProperties(int numOfGuests,Long cityId, Long countryId, Date checkIn, Date checkOut);
	
	
	
	@Query("SELECT DISTINCT p  FROM Property p "
			+ "INNER JOIN Apartment ap ON ap.property.id = p.id "
			+ "INNER JOIN Address addr ON p.address.id = addr.id "
			+ "INNER JOIN City ct ON addr.city.id = ct.id "
			+ "INNER JOIN Country cn ON ct.country.id = cn.id "
			+ "INNER JOIN Amenity amen on ap.id = amen.id "
			+ "INNER JOIN Facility fac on p.id = fac.id "
			+ "WHERE ap.numberOfGuests >= ?1 "
			+ "AND (amen.id IN (?6)) "
			+ "AND (fac.id IN (?7))"
			+ "AND (ap.price <= ?8)"
			+ "AND ((ct.id = ?2 AND cn.id = ?3)) "
			+ "AND ap.id NOT IN ("
			+ "SELECT DISTINCT book FROM Booking book WHERE NOT ( "
			+ "(?4 < book.checkIn AND ?5 < book.checkIn) OR"
			+ "(?4 > book.checkOut AND ?5 > book.checkOut)))")
	public List<Property> advanceSearchProperties(int numOfGuests, 
			Long cityId, Long countryId, Date checkIn, Date checkOut,
			List<Long> amenityIds, List<Long> facilityIds, BigDecimal price);

	
	List<Property> getAllByUserId(Long idOwnerUser);
}
