package com.softserve.edu.bookinglite;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.bookinglite.config.CloudinaryConfig.CloudinaryData;
import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
	@Bean
	Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				CloudinaryData.CLOUD_NAME.getName(), CloudinaryData.CLOUD_NAME.getValue(),
				CloudinaryData.API_KEY.getName(), CloudinaryData.API_KEY.getValue(),
				CloudinaryData.API_SECRET.getName(), CloudinaryData.API_SECRET.getValue()));
	}
	@Bean  
	 public TaskExecutor taskExecutor() {  
	   ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();  
	   threadPoolTaskExecutor.setCorePoolSize(1);  
	   threadPoolTaskExecutor.setMaxPoolSize(5);  
	   return threadPoolTaskExecutor;  
	 }  
	public static void main(String[] args) {
		SpringApplication.run(BookingliteApplication.class, args);
	}
}
