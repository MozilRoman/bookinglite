package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.bookinglite.entity.Country;
import com.softserve.edu.bookinglite.repository.CountryRepository;

@Service
public class CountryService {

	private final CountryRepository countryRepository;

	@Autowired
	public CountryService(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}
	
	@Transactional
	public List<Country> getCountry(){
		return countryRepository.findAll();
	}

	public Country getCountryByid(Long id) {
		return countryRepository.findById(id).get();
	}
	
}
