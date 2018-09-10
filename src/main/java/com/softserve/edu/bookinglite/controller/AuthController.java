package com.softserve.edu.bookinglite.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.exception.BadUserCredentialsException;
import com.softserve.edu.bookinglite.exception.EmailAlreadyUsedException;
import com.softserve.edu.bookinglite.exception.UserIsNotVerifiedException;
import com.softserve.edu.bookinglite.exception.UserNotFoundException;
import com.softserve.edu.bookinglite.service.UserService;
import com.softserve.edu.bookinglite.service.dto.LoginDto;
import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;

@RestController
@RequestMapping("/api")
public class AuthController {

	private final UserService userService;

	@Autowired
	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user")
	public UserDto hello(Principal principal) {
		UserDto user = userService.findById(Long.parseLong(principal.getName()));
		return user;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto)
			throws UserNotFoundException, UserIsNotVerifiedException, BadUserCredentialsException {

		return ResponseEntity.ok().body(userService.loginUser(loginDto));
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto)
			throws EmailAlreadyUsedException, UserIsNotVerifiedException {
		userService.registerUser(registerDto);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("/registrationconfirm")
	public ResponseEntity<Void> registrationconfirm(@RequestParam(name = "token") String token) {
		if (userService.verifyUser(token)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
}
