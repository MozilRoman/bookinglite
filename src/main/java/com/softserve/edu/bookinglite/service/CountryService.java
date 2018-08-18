package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.bookinglite.entity.Country;
import com.softserve.edu.bookinglite.repository.CountryRepository;

@Service
public class CountryService {

	private CountryRepository countryRepositor;

	@Autowired
	public CountryService(CountryRepository countryRepositor) {
		this.countryRepositor = countryRepositor;
	}
	
	@Transactional
	public List<Country> getCountry(){
		return countryRepositor.findAll();
	}
	
}
