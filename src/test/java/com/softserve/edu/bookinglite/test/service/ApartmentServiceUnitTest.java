package com.softserve.edu.bookinglite.test.service;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.ApartmentService;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApartmentServiceUnitTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private ApartmentService apartmentService;

    @Test(expected = ApartmentNotFoundException.class)
    public void getById() throws ApartmentNotFoundException {
        Apartment apartment = new Apartment();
        apartment.setId(1L);
        apartmentRepository.delete(apartment);
        apartmentService.findApartmentDtoById(1L);

    }
    
}

