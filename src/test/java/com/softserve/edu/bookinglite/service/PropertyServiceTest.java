package com.softserve.edu.bookinglite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.City;
import com.softserve.edu.bookinglite.entity.Country;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyServiceTest {

	@Autowired
	private PropertyService propertyService;

	@MockBean
	private PropertyRepository propertyRepository;
	
	@Test
	public void testGetAllPropertyDtos() {
		//Arrange
		Property property = getPropertyInstance();
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		
		PropertyDto actualPropertyDto = PropertyMapper.instance
				.propertyToBasePropertyDtoWithApartmentAddressUser(property);
		
		Mockito.when(propertyRepository.findAll()).thenReturn(properties);
		//Act
		List<PropertyDto> expectedPropertyDto = propertyService.getAllPropertyDtos();
		//Assert
		assertThat(actualPropertyDto.getName()).isEqualTo(expectedPropertyDto.get(0).getName());
	}

	@Test
	public void getPropertyByIdTest() {
		//Arrange
		Property property = getPropertyInstance();
		Optional<Property> actualProperty = Optional.of(property); 
		Mockito.when(propertyRepository.findById(1L)).thenReturn(actualProperty);
		//Act
		Optional<Property> expectedProperty = propertyService.getPropertyById(1L);
		//Assert
		assertThat(actualProperty).isEqualTo(expectedProperty);
		
	}

	@Test
	public void getPropertyDtoByIdTest() throws PropertyNotFoundException {
		//Arrange
		Property property = getPropertyInstance();
		Optional<Property> propertyOptional = Optional.of(property);
		PropertyDto actualPropertyDto = PropertyMapper.
				instance.propertyToBasePropertyDtoWithApartmentAddressUser(propertyOptional.get());
		
		Mockito.when(propertyRepository.findById(1L)).thenReturn(propertyOptional);
		//Act
		PropertyDto expectedPropertyDto = propertyService.getPropertyDtoById(1L);
		//Assert
		assertThat(actualPropertyDto).isEqualTo(expectedPropertyDto);
	}
	
	
	@Test(expected = PropertyNotFoundException.class)
	public void getPropertyDtoByIdThatNotExistTest() {
		//Arrange
//		Property property = getPropertyInstance();
//		Optional<Property> propertyOptional = Optional.of(property);
//		PropertyDto actualPropertyDto = PropertyMapper.
//				instance.propertyToBasePropertyDtoWithApartmentAddressUser(propertyOptional.get());
		
		Mockito.when(propertyRepository.findById(456456L)).thenThrow(new PropertyNotFoundException(456456L));
		//Act
//		PropertyDto propertyDto = propertyService.getPropertyDtoById(456456L);
		//Assert
	}
	
	
	
	private Property getPropertyInstance() {
		//Country
				Country country = new Country();
				country.setId(1L);
				country.setName("Ukraine");
				//City
				City city = new City();
				city.setId(1L);
				city.setName("Lviv");
				city.setCountry(country);
				//Address
				Address address = new Address();
				address.setAddressLine("adsasd");
				address.setCity(city);
				address.setId(1L);
				address.setZip("12332");
				// PropertyType
				PropertyType propertyType = new PropertyType();
				propertyType.setId(1L);
				propertyType.setName("Hotel");
				//Role
				Role role = new Role();
				Set<Role> roles = new HashSet<>();
				roles.add(role);
				role.setId(1L);
				role.setName("Owner");
				//User
				User user = new User();
				user.setFirst_name("Marian");
				user.setLast_name("Mazurak");
				user.setPassword("qwerty");
				user.setEmail("asd@ASd.com");
				user.setPhone_number("456654456");
				user.setVerified(true);
				user.setAddress(address);
				user.setRoles(roles);
				//Property
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
