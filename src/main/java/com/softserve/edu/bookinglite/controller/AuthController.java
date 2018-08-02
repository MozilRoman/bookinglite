package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.security.JwtTokenProvider;
import com.softserve.edu.bookinglite.service.UserService;
import com.softserve.edu.bookinglite.service.dto.LoginDto;
import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping("/hello")
    public UserDto hello(Principal principal){
       UserDto user = userService.findById(Long.parseLong(principal.getName()));
       return user;
    }


    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            //SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok().body(jwt);
        } catch (AuthenticationException ex){
            return ResponseEntity.status(401).body(ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto){
        if(userService.existsByEmail(registerDto.getEmail())){
        	return new ResponseEntity<Void>(HttpStatus.ALREADY_REPORTED);
        	//return ResponseEntity.badRequest().body("User already exists");
        }
        if (userService.registerUser(registerDto)){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }




}
