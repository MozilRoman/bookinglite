package com.softserve.edu.bookinglite.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.City;
import com.softserve.edu.bookinglite.entity.Country;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.PropertyConfirmOwnerException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.dto.AdvanceSearchDto;
import com.softserve.edu.bookinglite.service.dto.CreatePropertyDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.SearchDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;
import com.softserve.edu.bookinglite.util.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class PropertyServiceTest {

	private static final String CITY_NAME = "Lviv";
	private static final String COUNTRY_NAME = "Ukraine";
	private static final String INCORRECT_NAME = "incorrecName";
	private static final int INDEX = 0;
	private static final Long ID = 1L;

	
	@Mock
	private PropertyRepository propertyRepository;
	@InjectMocks
	private PropertyService propertyService;

	@Test
	public void getAllPropertyDtosTest() {
		// Arrange
		Property property = getPropertyInstance();
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(property);

		Mockito.when(propertyRepository.findAll()).thenReturn(properties);
		// Act
		List<PropertyDto> expectedPropertyDto = propertyService.getAllPropertyDtos();
		// Assert
		assertThat(actualPropertyDto).isEqualTo(expectedPropertyDto.get(INDEX));
	}

	@Test
	public void getPropertyByIdTest() {
		// Arrange
		Property property = getPropertyInstance();
		Optional<Property> actualProperty = Optional.of(property);
		Mockito.when(propertyRepository.findById(ID)).thenReturn(actualProperty);
		// Act
		Optional<Property> expectedProperty = propertyService.getPropertyById(ID);
		// Assert
		assertThat(actualProperty).isEqualTo(expectedProperty);
	}

	@Test
	public void getPropertyDtoByIdTest() throws PropertyNotFoundException {
		// Arrange
		Optional<Property> propertyOptional = Optional.of(getPropertyInstance());
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(propertyOptional.get());

		Mockito.when(propertyRepository.findById(ID)).thenReturn(propertyOptional);
		// Act
		PropertyDto expectedPropertyDto = propertyService.getPropertyDtoById(ID);
		// Assert
		assertThat(actualPropertyDto).isEqualTo(expectedPropertyDto);
	}
	
	@Test
	public void getPropertyDtosByCityNameTest() {
		// Arrange
		Property property = getPropertyInstance();
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(property);

		Mockito.when(propertyRepository.getAllPropertyByCityName(CITY_NAME.toLowerCase())).thenReturn(properties);
		// Act
		List<PropertyDto> expected = propertyService.getPropertyDtosByCityName(CITY_NAME.toLowerCase());
		// Assert
		assertThat(expected.get(INDEX)).isEqualTo(actualPropertyDto);
	}

	@Test
	public void getPropertyDtosByCityNameEmptyListTest() {
		// Arrange
		List<Property> properties = new ArrayList<>();
		properties.isEmpty();
		Mockito.when(propertyRepository.getAllPropertyByCityName(INCORRECT_NAME.toLowerCase())).thenReturn(properties);
		// Act
		List<PropertyDto> expected = propertyService.getPropertyDtosByCityName(INCORRECT_NAME.toLowerCase());
		// Assert
		assertThat(expected).isEmpty();
	}

	@Test
	public void getPropertyDtosByCountryNameTest() {
		// Arrange
		Property property = getPropertyInstance();
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(property);
		Mockito.when(propertyRepository.getAllPropertyByCountryName(COUNTRY_NAME.toLowerCase())).thenReturn(properties);
		// Act
		List<PropertyDto> expected = propertyService.getPropertyDtosByCountryName(COUNTRY_NAME.toLowerCase());
		// Assert
		assertThat(expected.get(INDEX)).isEqualTo(actualPropertyDto);
	}

	@Test
	public void getPropertyDtosByCountryNameEmptyListTest() {
		// Arrange
		List<Property> properties = new ArrayList<>();
		properties.isEmpty();
		Mockito.when(propertyRepository.getAllPropertyByCountryName(INCORRECT_NAME.toLowerCase()))
				.thenReturn(properties);
		// Act
		List<PropertyDto> expected = propertyService.getPropertyDtosByCountryName(INCORRECT_NAME.toLowerCase());
		// Assert
		assertThat(expected).isEmpty();
	}
	
	@Test(expected = PropertyNotFoundException.class)
	public void getPropertyDtoByIdExceptionTest() throws PropertyNotFoundException {
		// Arrange
		Mockito.when(propertyRepository.findById(ID)).thenReturn(Optional.empty());
		// Act
		@SuppressWarnings("unused")
		PropertyDto expectedPropertyDto = propertyService.getPropertyDtoById(ID);
		// Assert
	}

	@Test(expected = PropertyNotFoundException.class)
	public void updatePropertyExceptionTest() throws PropertyNotFoundException, PropertyConfirmOwnerException {
		// Arrange
		CreatePropertyDto actualPropertyDto = new CreatePropertyDto();
		Mockito.when(propertyRepository.findById(ID)).thenReturn(Optional.empty());
		// Act
		propertyService.updateProperty(actualPropertyDto, ID,ID);
	}
	@Test(expected = PropertyConfirmOwnerException.class)
	public void updatePropertyConfirmOwnerExceptionTest() throws PropertyNotFoundException, PropertyConfirmOwnerException {
		// Arrange
		Property property = getPropertyInstance();
		Optional<Property> optinalProperty = Optional.of(property);
		CreatePropertyDto actualPropertyDto = new CreatePropertyDto();
		Long ownerId = 12L;
		Mockito.when(propertyRepository.findById(ID)).thenReturn(optinalProperty);
		//Act
		propertyService.updateProperty(actualPropertyDto, ID, ownerId);
	}
	
	@Test
    public void searchPropertiesByPage()  {
		SearchDto searchDto = new SearchDto();
		searchDto.setCheckIn(DateUtils.setAllDate("2017-12-11"));
		searchDto.setCheckOut(DateUtils.setAllDate("2017-12-11"));
		searchDto.setCityId(1L);
		searchDto.setCountryId(1L);
		searchDto.setNumberOfGuests(2);
		
    	List<Property> propertList = new ArrayList<>();
    	propertList.add(getPropertyInstance());
    	List<PropertyDto> propertyDtoList = new ArrayList<>();
    	propertyDtoList.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(
    			getPropertyInstance()));
        Page<Property> pageProperty = new PageImpl<>(propertList);    	
        Page<PropertyDto> pagePropertyDto = new PageImpl<>(propertyDtoList );
        
        Mockito.when(propertyRepository.searchProperties(searchDto.getNumberOfGuests(), searchDto.getCityId(),
				searchDto.getCountryId(),searchDto.getCheckIn(), searchDto.getCheckOut(), PageRequest.of(1, 1)))
        .thenReturn(pageProperty);                        		        
        Page<PropertyDto> pagePropertyExpected= propertyService.searchProperties(searchDto, 1, 1);        
        assertThat(pagePropertyDto.getContent().get(INDEX)).isEqualTo(pagePropertyExpected.getContent().get(INDEX));
    }


	
	@Test
    public void advanceSearchPropertiesByPage()  {
		List <Long> listFacilitiesAndAmenities = new ArrayList<>();
		listFacilitiesAndAmenities.add(1L);
		 AdvanceSearchDto advanceSearchDto = new AdvanceSearchDto();
	        advanceSearchDto.setCheckIn(DateUtils.setAllDate("2017-12-11"));
	        advanceSearchDto.setCheckOut(DateUtils.setAllDate("2017-12-12"));
	        advanceSearchDto.setCityId(1L);
	        advanceSearchDto.setCountryId(1L);
	        advanceSearchDto.setNumberOfGuests(2);
	        advanceSearchDto.setPriceFromUser(new BigDecimal(25.68));
	        advanceSearchDto.setFacilitiesId(listFacilitiesAndAmenities);
	        advanceSearchDto.setAmenitiesId(listFacilitiesAndAmenities);
		
    	List<Property> propertList = new ArrayList<>();
    	propertList.add(getPropertyInstance());
    	List<PropertyDto> propertyDtoList = new ArrayList<>();
    	propertyDtoList.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(
    			getPropertyInstance()));
        Page<Property> pageProperty = new PageImpl<>(propertList);    	
        Page<PropertyDto> pagePropertyDto = new PageImpl<>(propertyDtoList );
        
        Mockito.when(propertyRepository.advanceSearchProperties(advanceSearchDto.getNumberOfGuests(),
				advanceSearchDto.getCityId(), 
				advanceSearchDto.getCountryId(),
				advanceSearchDto.getCheckIn(),
				advanceSearchDto.getCheckOut(),
				advanceSearchDto.getAmenitiesId(),
				advanceSearchDto.getFacilitiesId(),
				advanceSearchDto.getPriceFromUser(),
				PageRequest.of(1, 1)))
        	.thenReturn(pageProperty);                        		        
        Page<PropertyDto> pagePropertyExpected= propertyService.advanceSearchProperties(advanceSearchDto, 1, 1);        
        assertThat(pagePropertyDto.getContent().get(INDEX)).isEqualTo(pagePropertyExpected.getContent().get(INDEX));
    }
	
	private Property getPropertyInstance() {
		// Country
		Country country = new Country();
		country.setId(1L);
		country.setName("Ukraine");
		// City
		City city = new City();
		city.setId(1L);
		city.setName("Lviv");
		city.setCountry(country);
		// Address
		Address address = new Address();
		address.setAddressLine("adsasd");
		address.setCity(city);
		address.setId(1L);
		address.setZip("12332");
		// PropertyType
		PropertyType propertyType = new PropertyType();
		propertyType.setId(1L);
		propertyType.setName("Hotel");
		// Role
		Role role = new Role();
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		role.setId(1L);
		role.setName("Owner");
		// User
		User user = new User();
		user.setId(13L);
		user.setFirstName("Marian");
		user.setLastName("Mazurak");
		user.setPassword("qwerty");
		user.setEmail("asd@ASd.com");
		user.setPhoneNumber("456654456");
		user.setVerified(true);
		user.setAddress(address);
		user.setRoles(roles);
		// Property
		Property property = new Property();
		property.setId(1L);
		property.setName("Andrit");
		property.setDescription("dasd");
		property.setPhoneNumber("4564");
		property.setContactEmail("rewr@aeqw");
		property.setPropertyType(propertyType);
		property.setAddress(address);
		property.setUser(user);
		return property;
	}

}