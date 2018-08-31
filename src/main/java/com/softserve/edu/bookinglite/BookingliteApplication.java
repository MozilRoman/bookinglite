package com.softserve.edu.bookinglite;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


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
	


	public static void main(String[] args) {
		SpringApplication.run(BookingliteApplication.class, args);
	}
}
