package com.softserve.edu.bookinglite;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.bookinglite.config.CloudinaryConfig.CloudinaryData;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.BookingExistingException;
import com.softserve.edu.bookinglite.exception.BookingInvalidDataException;
import com.softserve.edu.bookinglite.exception.ReviewNotFoundExeption;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.ReviewRepository;
import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.ReviewService;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
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



	@Bean
	Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				CloudinaryData.CLOUD_NAME.getName(), CloudinaryData.CLOUD_NAME.getValue(),
				CloudinaryData.API_KEY.getName(), CloudinaryData.API_KEY.getValue(),
				CloudinaryData.API_SECRET.getName(), CloudinaryData.API_SECRET.getValue()));
	}


	public static void main(String[] args) {
		SpringApplication.run(BookingliteApplication.class, args);
	}
}
