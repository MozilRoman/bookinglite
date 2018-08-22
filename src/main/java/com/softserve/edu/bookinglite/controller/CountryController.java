package com.softserve.edu.bookinglite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.entity.Country;
import com.softserve.edu.bookinglite.service.CountryService;

@RestController
@RequestMapping("/api")
public class CountryController {

	private final CountryService countryService; 
	
	@Autowired
	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}

	@GetMapping("/countries")
	public List<Country> getAllCountry() {
		return countryService.getCountry();
	}
}
