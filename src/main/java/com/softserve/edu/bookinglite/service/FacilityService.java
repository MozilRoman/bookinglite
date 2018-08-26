package com.softserve.edu.bookinglite.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.repository.FacilityRepository;

@Service
public class FacilityService {

	private final FacilityRepository facilityRepository;

	@Autowired
	public FacilityService(FacilityRepository facilityRepository) {
		this.facilityRepository = facilityRepository;
	}

	@Transactional
	public Facility getFacilityById(Long facilityId) {
		return facilityRepository.findById(facilityId).get();
	}
	
	public List<Facility> getAllFacilities(){
		return facilityRepository.findAll();
	}
	
}
