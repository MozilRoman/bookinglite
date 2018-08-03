package com.softserve.edu.bookinglite;


import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class BookingliteApplication {

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	JwtAuthorizationFilter jwtAuthenticationFilter(){
		return new JwtAuthorizationFilter();
	}

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void init(){
		/*BookingDto bookingDto=new BookingDto();
		bookingDto.setCheck_in(new Date(118,0,8,16,0));
		bookingDto.setCheck_out(new Date(118,0,18,14,0));
		bookingDto.setTotal_price(BigDecimal.valueOf(99.99*7));
		bookingService.createBooking(bookingDto,1l,1l);
		System.out.println(bookingService.getAllBookingDto());*/
		//ObjectMapper mapper = new ObjectMapper();
		//UserHasBookingsDto obj = bookingService.getAllBookingsDtoByUserId(1l);
//Object to JSON in String
		/*try {
			System.out.println(mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}*/
		System.out.println("Chek in DB"+bookingService.getBookinDTOById(2l).getCheck_in());
		System.out.println(new Date(118,0,8,17,0));

		bookingService.chekBookingByChekInandCheckOut(1l,new Date(118,0,8,16,0),
				new Date(118,0,18,15,0));
	}


	public static void main(String[] args) {
		SpringApplication.run(BookingliteApplication.class, args);
	}
}
