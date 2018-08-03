package com.softserve.edu.bookinglite;

import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
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

	@PostConstruct
	public void init(){
		BookingDto bookingDto=new BookingDto();
		bookingDto.setCheck_in(new Date(118,8,8,14,0));
		bookingDto.setCheck_out(new Date(118,18,8,12,0));
		bookingDto.setTotal_price(BigDecimal.valueOf(99.99*7));
		bookingService.createBooking(bookingDto,1l,1l);

	}


	public static void main(String[] args) {
		SpringApplication.run(BookingliteApplication.class, args);
	}
}
