package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.BadUserCredentialsException;
import com.softserve.edu.bookinglite.exception.EmailAlreadyUsedException;
import com.softserve.edu.bookinglite.exception.UserIsNotVerifiedException;
import com.softserve.edu.bookinglite.exception.UserNotFoundException;
import com.softserve.edu.bookinglite.security.JwtTokenProvider;
import com.softserve.edu.bookinglite.service.UserService;
import com.softserve.edu.bookinglite.service.dto.LoginDto;
import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) throws UserNotFoundException,UserIsNotVerifiedException,BadUserCredentialsException {
        User user = userService.findByEmail(loginDto.getEmail());
        if(user != null && !user.isVerified()){
            throw new UserIsNotVerifiedException();
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok().body(jwt);
        } catch (AuthenticationException ex){
            if(ex instanceof BadCredentialsException){
                throw new BadUserCredentialsException();
            }
        }
        return ResponseEntity.badRequest().body("");
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto) throws EmailAlreadyUsedException,UserIsNotVerifiedException {
        userService.checkUser(registerDto.getEmail());
        userService.registerUser(registerDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @GetMapping("/registrationconfirm")
    public ResponseEntity<Void> registrationconfirm(@RequestParam(name = "token") String token){
       if(userService.verifyUser(token)){
           return new ResponseEntity<Void>(HttpStatus.OK);
       } else {
           return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
       }
    }




}
