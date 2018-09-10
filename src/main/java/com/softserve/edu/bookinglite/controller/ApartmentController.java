package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import com.softserve.edu.bookinglite.entity.Amenity;
import com.softserve.edu.bookinglite.entity.ApartmentType;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.ApartmentUpdateException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.service.dto.CreateApartmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softserve.edu.bookinglite.service.ApartmentService;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;

@RestController
@RequestMapping("/api")
public class ApartmentController {

	private final ApartmentService apartmentService;

	@Autowired
	public ApartmentController(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@GetMapping("/property/{id}/apartment")
	public List<ApartmentDto> getAllPropertiesApartment(@PathVariable("id") Long propertyId)
			throws PropertyNotFoundException {
		return apartmentService.findAllApartmentsByPropertyId(propertyId);
	}

	@GetMapping("/property/{id}/apartment/{id}")
	public ApartmentDto getApartment(@PathVariable("id") Long apartmentId) throws ApartmentNotFoundException {
		return apartmentService.findApartmentDtoById(apartmentId);
	}

	@PostMapping("/property/{id}/apartment")
	public ResponseEntity<CreateApartmentDto> saveApartment(@Valid @RequestBody CreateApartmentDto createApartmentDto,
			@PathVariable("id") Long propertyId, Principal principal) throws PropertyNotFoundException {
		Long userId = Long.parseLong(principal.getName());
		if (apartmentService.save(createApartmentDto, propertyId, userId)) {
			return new ResponseEntity<CreateApartmentDto>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<CreateApartmentDto>(HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/apartment/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CreateApartmentDto createApartmentDto,
			@PathVariable("id") Long apartmentId, Principal principal)
			throws ApartmentNotFoundException, ApartmentUpdateException {
		Long userId = Long.parseLong(principal.getName());
		if (apartmentService.updateApartment(createApartmentDto, apartmentId, userId)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/create-apartment/apartment-type")
	public List<ApartmentType> getApartmentTypes() {
		return apartmentService.findApartmentTypes();
	}

	@GetMapping("/create-apartment/amenities")
	public List<Amenity> getAmenities() {
		return apartmentService.findAmenities();
	}
}