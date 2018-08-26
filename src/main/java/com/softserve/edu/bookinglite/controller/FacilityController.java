package com.softserve.edu.bookinglite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.service.FacilityService;

@RestController
@RequestMapping("/api")
public class FacilityController {

	private final FacilityService facilityService; 
	
	@Autowired
	public FacilityController(FacilityService facilityService) {
		this.facilityService = facilityService;
	}

	@GetMapping("/facilities")
	public List<Facility> getAllFacilities() {
		return facilityService.getAllFacilities();
	}
}
