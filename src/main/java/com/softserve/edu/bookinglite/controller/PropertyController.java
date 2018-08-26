package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.exception.PropertyConfirmOwnerException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.dto.CreatePropertyDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.SearchDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;

@RestController
@RequestMapping("/api")
public class PropertyController {
	
	private final PropertyService propertyService;

	@Autowired
	public PropertyController(PropertyService propertyService) {
		this.propertyService = propertyService;
	}
	
	@GetMapping("/property")
	public List<PropertyDto> getAllProperties() {
		return propertyService.getAllPropertyDtos();
	}
	
	@GetMapping("/property/{propertyId}")
	public PropertyDto getPropertyById(@PathVariable("propertyId") Long id) throws PropertyNotFoundException {
			return propertyService.getPropertyDtoById(id);
	}

	@GetMapping("/property/city/{cityName}")
	public List<PropertyDto> getPropertiesByCityName(@PathVariable("cityName") String cityName) {
		return propertyService.getPropertyDtosByCityName(cityName);
	}

	@GetMapping("/property/country/{countryName}")
	public List<PropertyDto> getPropertiesByCountryName(@PathVariable("countryName") String countryName) {
		return propertyService.getPropertyDtosByCountryName(countryName);
	}

	@PostMapping("/property")
	public ResponseEntity<PropertyDto> createProperty(@Valid @RequestBody PropertyDto propertyDto, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		if (propertyService.saveProperty(propertyDto, userId)) {
			return new ResponseEntity<PropertyDto>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<PropertyDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/testversion")
	public ResponseEntity<CreatePropertyDto> createPropertyTestVersion(
			@Valid @RequestBody CreatePropertyDto createPropertyDto, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		
		propertyService.saveCreatePropertyDto(createPropertyDto, userId);
		return new ResponseEntity<CreatePropertyDto>(HttpStatus.OK);
	
	}
	

	@PutMapping("/property/{propertyId}")
	public ResponseEntity<PropertyDto> update(@RequestBody PropertyDto propertyDto, @PathVariable("propertyId") Long id,
			Principal principal) throws PropertyNotFoundException, PropertyConfirmOwnerException {
		Long ownerId = Long.parseLong(principal.getName());
		if (propertyService.updateProperty(propertyDto, id, ownerId)) {
			return new ResponseEntity<PropertyDto>(HttpStatus.OK);
		} else {
			return new ResponseEntity<PropertyDto>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/property/search")
	public List<PropertyDto> searchProperty(@RequestParam("checkIn") Date checkIn,
                                            @RequestParam("checkOut")  Date checkOut,
                                            @RequestParam("numberOfGuests") Integer numberOfGuests,
                                            @RequestParam("cityId") Long cityId,
                                            @RequestParam("countryId") Long countryId){
        SearchDto searchDto = new SearchDto();
        searchDto.setCheckIn(checkIn);
        searchDto.setCheckOut(checkOut);
        searchDto.setCityId(cityId);
        searchDto.setCountryId(countryId);
        searchDto.setNumberOfGuests(numberOfGuests);
		List<PropertyDto> result  = propertyService.searchProperty(searchDto);
        return result;
	}
	
	@GetMapping("/property/pages")
	public List<PropertyDto> getPropertiesByPage(
			@RequestParam("getPageNumber") int getPageNumber,
			@RequestParam("getPageSize") int getPageSize){
		List<PropertyDto> dtos = new ArrayList<>();
		Page<Property> page = propertyService.findPropertyByPage(getPageNumber,getPageSize);
		for(Property property : page.getContent()) {
			PropertyDto propertyDto = PropertyMapper.instance
					.propertyToBasePropertyDto(property);
			dtos.add(propertyDto);
		}
		return dtos;
	}
}