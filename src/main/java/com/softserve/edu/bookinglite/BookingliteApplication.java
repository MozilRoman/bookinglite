package com.softserve.edu.bookinglite;


import com.softserve.edu.bookinglite.repository.UserRepository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.bookinglite.config.CloudinaryConfig.CloudinaryData;
import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import com.softserve.edu.bookinglite.service.ApartmentService;
import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
	private ApartmentService apartmentService;

	@PostConstruct
	public void init(){
		Date in=new Date(118,9,10);
		Date out=new Date(118,9,23);

		System.out.println("Chek in DB  "+bookingService.checkBookingIfExistByChekInandCheckOut(2l,in,out));

		BookingDto bookingDto=new BookingDto();
		bookingDto.setCheck_in(in);
		bookingDto.setCheck_out(out);
		bookingDto.setTotal_price(BigDecimal.valueOf(99.99*7));

		bookingService.createBooking(bookingDto,1l,3l);
		System.out.println("booking size : "+bookingService.findAllBookingDto().size());
		System.out.println("apartment size : "+apartmentService.findAllApartmentDtos().size());
		System.out.println("Available size : "+bookingService.findAvailableApartamentsDtoByCheckInAndCheckOutDates(in,out).size());
		//ObjectMapper mapper = new ObjectMapper();
		//UserHasBookingsDto obj = bookingService.getAllBookingsDtoByUserId(1l);
//Object to JSON in String
		/*try {
			System.out.println(mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}*/


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
