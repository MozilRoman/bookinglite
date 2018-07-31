package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired private AddressRepository addressRepository;
	
	public Address getAddressById(Long id) {
		return addressRepository.getOne(id);
	}
	
	public List<Address> getAllAddreses(){
		return addressRepository.findAll();
	}
}
