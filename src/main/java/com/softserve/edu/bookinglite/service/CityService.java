package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.bookinglite.entity.City;
import com.softserve.edu.bookinglite.repository.CityRepository;

@Service
public class CityService {

	private final CityRepository cityRepository;

	@Autowired
	public CityService(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}
	
	@Transactional
	public List<City> getCity(){
		return cityRepository.findAll();
	}
}
