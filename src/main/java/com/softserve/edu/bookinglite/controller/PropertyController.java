package com.softserve.edu.bookinglite.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.softserve.edu.bookinglite.service.dto.AdvanceSearchDto;
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
	public ResponseEntity<CreatePropertyDto> createPropertyTestVersion(
			@Valid @RequestBody CreatePropertyDto createPropertyDto, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		propertyService.save(createPropertyDto, userId);
		return new ResponseEntity<CreatePropertyDto>(HttpStatus.OK);
	}
	
	@PutMapping("/property/{propertyId}")
	public ResponseEntity<PropertyDto> update(@RequestBody CreatePropertyDto createPropertyDto, @PathVariable("propertyId") Long id,
			Principal principal) throws PropertyNotFoundException, PropertyConfirmOwnerException {
		Long ownerId = Long.parseLong(principal.getName());
		if (propertyService.updateProperty(createPropertyDto, id, ownerId)) {
			return new ResponseEntity<PropertyDto>(HttpStatus.OK);
		} else {
			return new ResponseEntity<PropertyDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
//	@GetMapping("/property/search")
//	public List<PropertyDto> searchProperty(@RequestParam("checkIn") @DateTimeFormat (pattern="yyyy-MM-dd") Date checkIn,
//            									@RequestParam("checkOut") @DateTimeFormat (pattern="yyyy-MM-dd") Date checkOut,
//            									@RequestParam("numberOfGuests") Integer numberOfGuests,
//            									@RequestParam("cityId") Long cityId,
//            									@RequestParam("countryId") Long countryId){
//		SearchDto searchDto = new SearchDto();
//		searchDto.setCheckIn(checkIn);
//		searchDto.setCheckOut(checkOut);
//		searchDto.setCityId(cityId);
//		searchDto.setCountryId(countryId);
//		searchDto.setNumberOfGuests(numberOfGuests);
//		return propertyService.searchProperty(searchDto);
//	}
//	
//	 @GetMapping("/property/advancesearch")
//	    public List<PropertyDto> globalSearch(@RequestParam("checkIn") @DateTimeFormat (pattern="yyyy-MM-dd") Date checkIn,
//	                                          @RequestParam("checkOut") @DateTimeFormat (pattern="yyyy-MM-dd") Date checkOut,
//	                                          @RequestParam("numberOfGuests") Integer numberOfGuests,
//	                                          @RequestParam("cityId") Long cityId,
//	                                          @RequestParam("countryId") Long countryId,
//	                                          @RequestParam("price") BigDecimal price,
//	                                          @RequestParam("facilityIds") List<Long> facilityIds,
//	                                          @RequestParam("amenityIds") List<Long> amenityIds) {
//	        AdvanceSearchDto advanceSearchDto = new AdvanceSearchDto();
//	        advanceSearchDto.setCheckIn(checkIn);
//	        advanceSearchDto.setCheckOut(checkOut);
//	        advanceSearchDto.setCityId(cityId);
//	        advanceSearchDto.setCountryId(countryId);
//	        advanceSearchDto.setNumberOfGuests(numberOfGuests);
//	        advanceSearchDto.setPriceFromUser(price);
//	        advanceSearchDto.setFacilitiesId(facilityIds);
//	        advanceSearchDto.setAmenitiesId(amenityIds);	        
//	        return propertyService.advanceSearchProperty(advanceSearchDto);
//	    }
	 
	 @GetMapping("/property/search")//
		public Page<PropertyDto> searchPropertiesByPage(@RequestParam("pageNumber") int pageNumber,
													@RequestParam("pageSize") int pageSize,
	            									@RequestParam("checkIn")@DateTimeFormat (pattern="yyyy-MM-dd") Date checkIn,
	            									@RequestParam("checkOut")@DateTimeFormat (pattern="yyyy-MM-dd")  Date checkOut,
	            									@RequestParam("numberOfGuests") Integer numberOfGuests,
	            									@RequestParam("cityId") Long cityId,
	            									@RequestParam("countryId") Long countryId){
			SearchDto searchDto = new SearchDto();
			searchDto.setCheckIn(checkIn);
			searchDto.setCheckOut(checkOut);
			searchDto.setCityId(cityId);
			searchDto.setCountryId(countryId);
			searchDto.setNumberOfGuests(numberOfGuests);
			return propertyService.searchPropertiesByPage(searchDto, pageNumber, pageSize);
		}

	@GetMapping("/property/advancesearch")//
			public Page<PropertyDto> searchPropertiesByPage(@RequestParam("pageNumber") int pageNumber,
														@RequestParam("pageSize") int pageSize,
		            									@RequestParam("checkIn")@DateTimeFormat (pattern="yyyy-MM-dd") Date checkIn,
		            									@RequestParam("checkOut")@DateTimeFormat (pattern="yyyy-MM-dd")  Date checkOut,
		            									@RequestParam("numberOfGuests") Integer numberOfGuests,
		            									@RequestParam("cityId") Long cityId,
		            									@RequestParam("countryId") Long countryId,
		            									@RequestParam("price") BigDecimal price,
		            									@RequestParam("facilityIds") List<Long> facilityIds,
		            									@RequestParam("amenityIds") List<Long> amenityIds) {
			 AdvanceSearchDto advanceSearchDto = new AdvanceSearchDto();
		        advanceSearchDto.setCheckIn(checkIn);
		        advanceSearchDto.setCheckOut(checkOut);
		        advanceSearchDto.setCityId(cityId);
		        advanceSearchDto.setCountryId(countryId);
		        advanceSearchDto.setNumberOfGuests(numberOfGuests);
		        advanceSearchDto.setPriceFromUser(price);
		        advanceSearchDto.setFacilitiesId(facilityIds);
		        advanceSearchDto.setAmenitiesId(amenityIds);
				return propertyService.advanceSearchPropertiesByPage(advanceSearchDto, pageNumber, pageSize);
			}
	
	@GetMapping("/myproperties")
	public List<PropertyDto> getAllPropetiesByOwnerId(Principal principal){
		Long userId = Long.parseLong(principal.getName());
		return propertyService.getAllPropertyByOwner(userId);
	}
}