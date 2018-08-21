package com.softserve.edu.bookinglite.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.softserve.edu.bookinglite.entity.*;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.ApartmentService;
import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.mapper.ApartmentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApartmentServiceUnitTest {

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private PropertyService propertyService;
    @MockBean
    private ApartmentRepository apartmentRepository;
    @MockBean
    private PropertyRepository propertyRepository;

    @Test(expected = ApartmentNotFoundException.class)
    public void findByIdTestException() throws ApartmentNotFoundException {
        Apartment apartment = getApartment();
        apartment.setId(1L);
        apartmentRepository.delete(apartment);
        apartmentService.findApartmentDtoById(1L);
    }

    @Test(expected = PropertyNotFoundException.class)
    public void findAllApartmentsByPropertyIdTestException () throws PropertyNotFoundException {
        Property property = new Property();
        property.setId(1L);
        propertyRepository.delete(property);
        propertyService.getPropertyDtoById(1L);
    }

    @Test
    public void findApartmentDtoByIdTest() throws ApartmentNotFoundException {
        Apartment apartment = getApartment();
        ApartmentDto actualApartmentDto = ApartmentMapper.instance.toDto(apartment);
        Mockito.when(apartmentRepository.findById(1L)).thenReturn(Optional.of(apartment));
        ApartmentDto expectedApartmentDto = apartmentService.findApartmentDtoById(1L);
        assertThat(actualApartmentDto.getId()).isEqualTo(expectedApartmentDto.getId());
    }

    @Test
    public void findAllApartmentsByPropertyIdTest () throws PropertyNotFoundException {
        Apartment apartment = new Apartment();
        List<Apartment> apartmentList = new ArrayList<>();
        apartmentList.add(apartment);

        Property property = getPropertyInstance();
        property.setApartments(apartmentList);

        List<ApartmentDto> apartmentDtosList = new ArrayList<>();
        property.getApartments().forEach(a -> apartmentDtosList.add(ApartmentMapper.instance.toDto(a)));

        Mockito.when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        List<ApartmentDto> expectedApartmentDtos = apartmentService.findAllApartmentsByPropertyId(1L);

        assertThat(apartmentDtosList.size()).isEqualTo(expectedApartmentDtos.size());
    }

    private Apartment getApartment(){
        //ApartmentType
        ApartmentType apartmentType = new ApartmentType();
        apartmentType.setName("Double");
        //Amenity
        Amenity amenity = new Amenity();
        amenity.setName("WiFi");
        Set<Amenity> amenities = new HashSet<>();
        amenities.add(amenity);
        //Apartment
        Apartment apartment = new Apartment();
        apartment.setId(1L);
        apartment.setName("TestName");
        apartment.setPrice(BigDecimal.valueOf(444.99));
        apartment.setNumberOfGuests(2);
        apartment.setApartmentType(apartmentType);
        apartment.setAmenities(amenities);
        apartment.setProperty(getPropertyInstance());
        return apartment;
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
        user.setFirst_name("Marian");
        user.setLast_name("Mazurak");
        user.setPassword("qwerty");
        user.setEmail("asd@ASd.com");
        user.setPhone_number("456654456");
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

