package com.softserve.edu.bookinglite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.entity.City;
import com.softserve.edu.bookinglite.service.CityService;

@RestController
@RequestMapping("/api")
public class CityController {

	private final CityService cityService;

	@Autowired
	public CityController(CityService cityService) {
		this.cityService = cityService;
	}
	
	@GetMapping("/cities")
	public List<City> getAllCountry() {
		return cityService.getCity();
	}
}
