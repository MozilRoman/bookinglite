package com.softserve.edu.bookinglite.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.*;

import com.softserve.edu.bookinglite.exception.*;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import com.softserve.edu.bookinglite.util.DateUtils;
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
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceUnitTest {

	private final int INDEX = 0;
    private final Long ID = 1L;
    private final String CANCELED = "Canceled";
    private final int PAGE_AND_SIZE = 1;
    private final int HOUR_CHECK_IN = 17;
    private final int HOUR_CHECK_OUT = 15;
    private final Date NOW_SHORT_DATE = DateUtils.getDateAndTimeWithoutSeconds();
    private final String ALL_BOOKINGS = "allBookings";
    private final String CURRENT_BOOKINGS = "currentBookings";
    private final String ARCHIEVED_BOOKINGS = "archievedBookings";

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingStatusRepository bookingStatusRepository;
    @Mock
    private ApartmentRepository apartmentRepository;
    @InjectMocks
    private BookingService bookingService;


    @Test
    public void findBookinDTOById() throws BookingNotFoundException {
        Booking booking = getBookingInstance();
        BookingDto actualBookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
        Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
        BookingDto expectedBookingDto = bookingService.findBookinDTOById(ID, ID);
        assertThat(actualBookingDto).isEqualTo(expectedBookingDto);
    }

    @Test(expected = BookingNotFoundException.class)
    public void findBookinDTOByIdBookingNotFoundException() throws BookingNotFoundException {
        Booking booking = null;
        Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
        bookingService.findBookinDTOById(ID, ID);
    }

    @Test
    public void cancelBookingTest() throws BookingNotFoundException, BookingCancelException {//all good
        Booking booking = getBookingInstance();
        Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(3L);
        bookingStatus.setName(CANCELED);
        Mockito.when(bookingStatusRepository.findByName(CANCELED)).thenReturn(bookingStatus);
        Booking actualBooking = getBookingInstance();
        actualBooking.setBookingStatus(bookingStatus);
        Mockito.when(bookingRepository.save(booking)).thenReturn(actualBooking);
        boolean expectedCanceledBooking = bookingService.cancelBooking(ID, ID);
        assertThat(expectedCanceledBooking);
    }

    @Test(expected = BookingNotFoundException.class)
    public void cancelBookingNotFoundExceptionTest() throws BookingNotFoundException, BookingCancelException {
        Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(null);
        bookingService.cancelBooking(ID, ID);
    }

    @Test(expected = BookingCancelException.class)
    public void cancelBookingWrongBookingStatusTest() throws BookingNotFoundException, BookingCancelException {
        Booking booking = getBookingInstance();
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(3L);
        bookingStatus.setName(CANCELED);
        booking.setBookingStatus(bookingStatus);
        Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
        bookingService.cancelBooking(ID, ID);
    }

    @Test(expected = BookingCancelException.class)
    public void cancelBookingWrongDateTest() throws BookingNotFoundException, BookingCancelException {
        Booking booking = getBookingInstance();
        booking.setCheckIn(DateUtils.setAllDate("2018-01-11-14-00-00"));
        booking.setCheckOut(DateUtils.setAllDate("2018-01-12-12-00-00"));
        Mockito.when(bookingRepository.findBookingById(ID, ID)).thenReturn(booking);
        bookingService.cancelBooking(ID, ID);
    }
    
    @Test
    public void findPageAllBookingsDtoByUserIdAllBookings()  {
    	List<Booking> bookingsList = new ArrayList<>();
    	bookingsList.add(getBookingInstance());
    	List<BookingDto> bookingsDtoList = new ArrayList<>();
    	bookingsDtoList.add(BookingMapper.instance.bookingToBaseBookingDto(getBookingInstance()));
        Page<Booking> pageBooking = new PageImpl<>(bookingsList);    	
        Page<BookingDto> pageBookingDto = new PageImpl<>(bookingsDtoList );    	
        Mockito.when(bookingRepository.getPageAllBookingsByUserId(ID, PageRequest.of(PAGE_AND_SIZE, PAGE_AND_SIZE)))
        .thenReturn(pageBooking);                        		        
        Page<BookingDto> pageBookingExpected= bookingService.findPageAllBookingsDtoByUserId(
        		ID, PAGE_AND_SIZE, PAGE_AND_SIZE, ALL_BOOKINGS);        
        assertThat(pageBookingDto.getContent().get(INDEX)).isEqualTo(pageBookingExpected.getContent().get(INDEX));
    }
    
    @Test
    public void findPageAllBookingsDtoByUserIdCurrentBookings()  {
    	List<Booking> bookingsList = new ArrayList<>();
    	bookingsList.add(getBookingInstance());
    	List<BookingDto> bookingsDtoList = new ArrayList<>();
    	bookingsDtoList.add(BookingMapper.instance.bookingToBaseBookingDto(getBookingInstance()));
        Page<Booking> pageBooking = new PageImpl<>(bookingsList);    	
        Page<BookingDto> pageBookingDto = new PageImpl<>(bookingsDtoList );    	
        Mockito.when(bookingRepository.getPageCurrentBookingsByUserId(ID, NOW_SHORT_DATE, 
        		PageRequest.of(PAGE_AND_SIZE, PAGE_AND_SIZE))).thenReturn(pageBooking); 
        Page<BookingDto> pageBookingExpected= bookingService.findPageAllBookingsDtoByUserId(
        		ID, PAGE_AND_SIZE, PAGE_AND_SIZE, CURRENT_BOOKINGS);  
        assertThat(pageBookingDto.getContent().get(INDEX)).isEqualTo(pageBookingExpected.getContent().get(INDEX));
    }
    
    @Test
    public void findPageAllBookingsDtoByUserIdArchievedBookings()  {
    	Booking booking = getBookingInstance();
    	booking.setCheckIn(DateUtils.setAllDate("2017-01-11-14-00-00"));
        booking.setCheckOut(DateUtils.setAllDate("2017-01-12-12-00-00"));
    	List<Booking> bookingsList = new ArrayList<>();
    	bookingsList.add(booking);
    	List<BookingDto> bookingsDtoList = new ArrayList<>();
    	bookingsDtoList.add(BookingMapper.instance.bookingToBaseBookingDto(booking));
        Page<Booking> pageBooking = new PageImpl<>(bookingsList);    	
        Page<BookingDto> pageBookingDto = new PageImpl<>(bookingsDtoList );    	
        Mockito.when(bookingRepository.getPageArchievedBookingsByUserId(ID, NOW_SHORT_DATE,
        		PageRequest.of(PAGE_AND_SIZE, PAGE_AND_SIZE)))
        .thenReturn(pageBooking);                        		        
        Page<BookingDto> pageBookingExpected= bookingService.findPageAllBookingsDtoByUserId(
        		ID, PAGE_AND_SIZE, PAGE_AND_SIZE, ARCHIEVED_BOOKINGS);        
        assertThat(pageBookingDto.getContent().get(INDEX)).isEqualTo(pageBookingExpected.getContent().get(INDEX));
    }

    /*@Test
    public void findPageAllBookingsDtoByOwnerIdPastBookingsTest()  {
        Page<Booking> bookings = Mockito.mock(Page.class);
        Page<Booking> pageBooking = new PageImpl(bookingsList);
        Page<BookingDto> pageBookingDto = new PageImpl(bookingsDtoList );
        Mockito.when(bookingRepository.getPastBookingsByPropertyAndOwnerId(ID,ID, nowDate,
                PageRequest.of(PAGE_AND_SIZE, PAGE_AND_SIZE)))
                .thenReturn(PageRequest.of(5,5));
        Page<BookingDto> pageBookingExpected= bookingService.getPageBookingsByOwner(
               ID, ID, PAST_BOOKINGS, PAGE_AND_SIZE, PAGE_AND_SIZE);
        assertThat(pageBookingDto.getContent().size()).isEqualTo(pageBookingExpected.getContent().size() );
    }*/

    @Test(expected = BookingInvalidDataException.class)
    public void createBookingInvalidDataTest() throws BookingInvalidDataException, ApartmentNotFoundException, BookingExistingException, NumberOfGuestsException {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setCheckIn(getFutureDayByDays(2));
        createBookingDto.setCheckOut(getPastDayByDays(3));
        Booking booking = getBookingInstance();
        Mockito.when(apartmentRepository.findById(ID)).thenReturn(Optional.of(booking.getApartment()));
        bookingService.createBookingWithValidation(createBookingDto, 1l, ID);
    }

    @Test(expected = NumberOfGuestsException.class)
    public void createBookingInvalidNumberOfGuestsTest() throws BookingInvalidDataException, ApartmentNotFoundException, BookingExistingException, NumberOfGuestsException {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setCheckIn(DateUtils.setAllDate("2019-05-01-14-00-00"));
        createBookingDto.setCheckOut(DateUtils.setAllDate("2019-05-02-12-00-00"));
        createBookingDto.setNumberOfGuests(4);
        Booking booking = getBookingInstance();
        Mockito.when(apartmentRepository.findById(ID)).thenReturn(Optional.of(booking.getApartment()));
        bookingService.createBookingWithValidation(createBookingDto, 1l, ID);
    }

    @Test(expected = ApartmentNotFoundException.class)
    public void createBookingInvalidApartmentTest() throws BookingInvalidDataException, ApartmentNotFoundException, BookingExistingException {
        Date in = DateUtils.setAllDate("2020-11-11-14-00-00");
        Date out = DateUtils.setAllDate("2020-11-15-12-00-00");
        Mockito.when(apartmentRepository.findById(ID)).thenReturn(Optional.empty());
       bookingService.validateApartmentExistAndDateIsNotInvalid(ID, in, out);
    }

    @Test(expected = BookingExistingException.class)
    public void createBookingInvalidGetBookingByCheckTest() throws BookingInvalidDataException, ApartmentNotFoundException, BookingExistingException, NumberOfGuestsException {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        Date in = DateUtils.setHourAndMinToDate(DateUtils.setAllDate("2018-11-8"), HOUR_CHECK_IN);
        Date out = DateUtils.setHourAndMinToDate(DateUtils.setAllDate("2018-11-11"), HOUR_CHECK_OUT);
        createBookingDto.setCheckIn(in);
        createBookingDto.setCheckOut(out);
        createBookingDto.setNumberOfGuests(2);
        Booking booking = getBookingInstance();
        Mockito.when(apartmentRepository.findById(ID)).thenReturn(Optional.of(booking.getApartment()));
        Mockito.when(bookingRepository.isApartmentBookedWithinDateRange(ID, createBookingDto.getCheckIn(), createBookingDto.getCheckOut())).thenReturn(true);
        bookingService.createBookingWithValidation(createBookingDto, ID, ID);
    }

    @Test
    public void createBookingTest() throws Exception {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        Date in = DateUtils.setHourAndMinToDate(DateUtils.setAllDate("2018-11-8"), HOUR_CHECK_IN);
        Date out = DateUtils.setHourAndMinToDate(DateUtils.setAllDate("2018-11-11"), HOUR_CHECK_OUT);
        createBookingDto.setCheckIn(in);
        createBookingDto.setCheckOut(out);
        createBookingDto.setNumberOfGuests(2);
        Booking booking = getBookingInstance();
        Mockito.when(apartmentRepository.findById(ID)).thenReturn(Optional.of(booking.getApartment()));
        Mockito.when(bookingRepository.isApartmentBookedWithinDateRange(ID, createBookingDto.getCheckIn(), createBookingDto.getCheckOut())).thenReturn(false);
        assertThat(bookingService.createBookingWithValidation(createBookingDto, ID, ID)).isTrue();
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
        user.setFirstName("ivan");
        user.setLastName("zhun");
        user.setPassword("123");
        user.setEmail("zynki.i32@gmail.com");
        user.setPhoneNumber("12313123");
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
        ApartmentType apartmentType = new ApartmentType();
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
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(1L);
        String RESERVED = "Reserved";
        bookingStatus.setName(RESERVED);

        //Booking
        Booking booking = new Booking();
        booking.setId(1L);
        Date in = DateUtils.setHourAndMinToDate(DateUtils.setAllDate("2019-11-11"), 14 );
        Date out = DateUtils.setHourAndMinToDate(DateUtils.setAllDate("2019-11-15"), 12 );
        booking.setCheckIn(in);
        booking.setCheckOut(out);
        booking.setTotalPrice(new BigDecimal(100));
        booking.setApartment(apartment);
        booking.setBookingStatus(bookingStatus);
        booking.setUser(user);
        return booking;
    }   
    


    public Date getPastDayByDays(int minusDays){
    	final Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -minusDays);
    	return cal.getTime();
    }
    
    public Date getFutureDayByDays(int plusDays){
    	final Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, +plusDays);
    	return cal.getTime();
    }
    
    public Date getTodayDate(){
    	return  Calendar.getInstance().getTime();
    }
}
