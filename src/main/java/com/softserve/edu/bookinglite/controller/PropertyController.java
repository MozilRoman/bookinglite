package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

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
	public PropertyDto getPropertyById(@PathVariable("propertyId") Long id) {
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

	@PutMapping("/property/{propertyId}")
	public ResponseEntity<PropertyDto> update(@RequestBody PropertyDto propertyDto,
			@PathVariable("propertyId") Long id) throws PropertyNotFoundException {
		if (propertyService.updateProperty(propertyDto, id)) {
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
}
