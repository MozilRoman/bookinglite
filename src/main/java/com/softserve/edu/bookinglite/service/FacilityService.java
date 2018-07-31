package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.repository.FacilityRepository;

@Service
public class FacilityService {

	@Autowired private FacilityRepository facilityRepository;
	
	public List<Facility> getAllFacilities(){
		return facilityRepository.findAll();
	}
}
