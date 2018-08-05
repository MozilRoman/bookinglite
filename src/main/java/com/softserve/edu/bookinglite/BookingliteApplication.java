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
		Date in=new Date(118,10,10);
		Date out=new Date(118,10,23);

		//System.out.println("Chek in DB  "+bookingService.checkBookingIfExistByChekInandCheckOut(2l,in,out));

		BookingDto bookingDto=new BookingDto();
		bookingDto.setCheck_in(in);
		bookingDto.setCheck_out(out);
		bookingDto.setTotal_price(BigDecimal.valueOf(99.99*7));

		System.out.println(bookingService.createBooking(bookingDto,1l,2l));
		System.out.println(bookingService.findAllBookingDto().size());
		//ObjectMapper mapper = new ObjectMapper();
		//UserHasBookingsDto obj = bookingService.getAllBookingsDtoByUserId(1l);
//Object to JSON in String
		/*try {
			System.out.println(mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}*/


	}


	public static void main(String[] args) {
		SpringApplication.run(BookingliteApplication.class, args);
	}
}
