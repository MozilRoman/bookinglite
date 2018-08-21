package com.softserve.edu.bookinglite.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.ApartmentType;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.BookingStatus;
import com.softserve.edu.bookinglite.entity.City;
import com.softserve.edu.bookinglite.entity.Country;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.BookingCancelException;
import com.softserve.edu.bookinglite.exception.BookingNotFoundException;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookingServiceUnitTest {
	
	private static final int INDEX = 0;
	private static final Long ID = 1L;
	private final String RESERVED = "Reserved";
	private final String CANCELED = "Canceled";
	
	@Autowired
	private BookingService bookingService;
	@MockBean
	private BookingRepository bookingRepository;
	@MockBean
	private BookingStatusRepository bookingStatusRepository;
	
	@Test
	public void findBookinDTOById() {
		Booking bookingOptional = getBookingInstance();
		BookingDto actualBookingDto = BookingMapper.instance.bookingToBaseBookingDto(bookingOptional);

		Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(bookingOptional);
		// Act
		BookingDto expectedBookingDto = bookingService.findBookinDTOById(ID,ID);
		// Assert
		assertThat(actualBookingDto).isEqualTo(expectedBookingDto);
	}
	
	@Test
	public void findAllBookingsDtoByUserIdTest() {
		// Arrange
		Booking booking = getBookingInstance();
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking);
		BookingDto actualBookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);

		Mockito.when(bookingRepository.getAllByUserIdOrderByCheckInAsc(ID)).thenReturn(bookings);
		// Act
		List<BookingDto> expectedBookingDto = bookingService.findAllBookingsDtoByUserId(1L);
		// Assert
		assertThat(actualBookingDto).isEqualTo(expectedBookingDto.get(INDEX));
	}
	
	@Test
	public void cancelBookingTest() throws BookingNotFoundException, BookingCancelException{//all good
		// Arrange
		Booking booking = getBookingInstance();

		Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
		
		BookingStatus bookingStatus= new BookingStatus();
		bookingStatus.setId(3L);
		bookingStatus.setName(CANCELED);
		Mockito.when(bookingStatusRepository.findByName(CANCELED)).thenReturn(bookingStatus);
				
		Booking actualBooking = getBookingInstance();
		actualBooking.setBookingStatus(bookingStatus);
		
		Mockito.when(bookingRepository.save(booking)).thenReturn(actualBooking);
		boolean expectedCanceledBooking = bookingService.cancelBooking(ID, ID);		
		
		assertThat(expectedCanceledBooking);
	}
	
	@Test(expected = BookingNotFoundException.class)//work
	public void cancelBookingNotFoundExceptionTest() throws BookingNotFoundException, BookingCancelException{
		Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(null);
		boolean expectedBooking= bookingService.cancelBooking(ID, ID);
		assertThat(expectedBooking);
	}
	
	@Test(expected = BookingCancelException.class)///work
	public void cancelBookingWrongBookingStatusTest() throws BookingNotFoundException, BookingCancelException{
		// Arrange
		Booking booking = getBookingInstance();
		BookingStatus bookingStatus= new BookingStatus();
		bookingStatus.setId(3L);
		bookingStatus.setName(CANCELED);
		booking.setBookingStatus(bookingStatus);
		Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);				
		boolean expectedCanceledBooking = bookingService.cancelBooking(ID, ID);		
		
		assertThat(expectedCanceledBooking);
	}
	
	@Test(expected = BookingCancelException.class)
	public void cancelBookingWrongDateTest() throws BookingNotFoundException, BookingCancelException{//all good
		// Arrange
		Booking booking = getBookingInstance();
		booking.setCheckIn(setAllDate("2018-01-11-14-00-00"));
		booking.setCheckOut(setAllDate("2018-01-12-12-00-00"));
		Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
		
		boolean expectedCanceledBooking = bookingService.cancelBooking(ID, ID);				
		assertThat(expectedCanceledBooking);
	}
	
	
	
	private Booking getBookingInstance() {
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
		address.setAddressLine("Groove St. 27");
		address.setCity(city);
		address.setId(1L);
		address.setZip("12312");
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
		user.setId(1L);
		user.setFirst_name("ivan");
		user.setLast_name("zhun");
		user.setPassword("123");
		user.setEmail("zynki.i32@gmail.com");
		user.setPhone_number("12313123");
		user.setVerified(true);
		user.setAddress(address);
		user.setRoles(roles);
		// Property
		Property property = new Property();
		property.setId(1L);
		property.setName("name p");
		property.setDescription("desc p");
		property.setPhoneNumber("212141412");
		property.setContactEmail("w@w");
		property.setPropertyType(propertyType);
		property.setAddress(address);
		property.setUser(user);
		//ApartmentType
		ApartmentType apartmentType= new ApartmentType();
		apartmentType.setId(2L);
		apartmentType.setName("Double");
		//Apartment
		Apartment apartment = new Apartment();
		apartment.setId(1L);
		apartment.setName("Lus");
		apartment.setNumberOfGuests(2);
		apartment.setPrice(new BigDecimal(25.68));
		apartment.setApartmentType(apartmentType);
		apartment.setProperty(property);
		//BookingStatus
		BookingStatus bookingStatus= new BookingStatus();
		bookingStatus.setId(1L);
		bookingStatus.setName("Reserved");
		
		//Booking
		Booking booking= new Booking();
		booking.setId(1L);
		Date in = setAllDate("2018-11-11-14-00-00");
		Date out = setAllDate("2018-11-15-12-00-00");
		out.setHours(12);
		booking.setCheckIn(in);
		booking.setCheckOut(out);
		booking.setTotalPrice(new BigDecimal(100));
		booking.setApartment(apartment);
		booking.setBookingStatus(bookingStatus);
		booking.setUser(user);		
		return booking;
}
	
	public Date setAllDate(String stringDate) {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		 try {
			 date= sdf.parse(stringDate);
	      } catch (ParseException e) { 
	         System.out.println("Unparseable using " + sdf); 
	      }	
		return  date;
	}

}
