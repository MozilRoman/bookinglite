package com.softserve.edu.bookinglite.test.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;

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
		Property property = getPropertyInstance();
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(property);
		Mockito.when(propertyRepository.findById(ID)).thenReturn(Optional.empty());
		
		// Act
		propertyService.updateProperty(actualPropertyDto, ID,ID);
	}
	@Test(expected = PropertyConfirmOwnerException.class)
	public void updatePropertyConfirmOwnerExceptionTest() throws PropertyNotFoundException, PropertyConfirmOwnerException {
		// Arrange
		Property property = getPropertyInstance();
		Optional<Property> optinalProperty = Optional.of(property);
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(property);
		Long ownerId = 12L;
		Mockito.when(propertyRepository.findById(ID)).thenReturn(optinalProperty);
		//Act
		propertyService.updateProperty(actualPropertyDto, ID, ownerId);
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