package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public List<ApartmentDto> getAllPropertiesApartment(@PathVariable("id") Long propertyId) {
		return apartmentService.findAllApartmentsByPropertyId(propertyId);
	}

	@GetMapping("/apartment/{id}")
	public ApartmentDto getApartment(@PathVariable("id") Long apartmentId) {
		return apartmentService.findApartmentDtoById(apartmentId);
	}

	@PostMapping("/property/{id}/apartment")
	public ResponseEntity<Void> saveApartment(@Valid @RequestBody ApartmentDto apartmentDto,
			@PathVariable("id") Long propertyId, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		if (apartmentService.saveApartment(apartmentDto, propertyId, userId)) {
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/apartment/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ApartmentDto apartmentDto,
			@PathVariable("id") Long apartmentId, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		if (apartmentService.updateApartment(apartmentDto, apartmentId, userId)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

}