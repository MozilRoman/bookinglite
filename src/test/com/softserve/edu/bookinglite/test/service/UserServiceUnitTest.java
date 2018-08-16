package com.softserve.edu.bookinglite.test.service;


import com.softserve.edu.bookinglite.exception.BadUserCredentialsException;
import com.softserve.edu.bookinglite.exception.UserIsNotVerifiedException;
import com.softserve.edu.bookinglite.exception.UserNotFoundException;
import com.softserve.edu.bookinglite.repository.RoleRepository;
import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.repository.VerificationTokenRepository;
import com.softserve.edu.bookinglite.security.JwtTokenProvider;
import com.softserve.edu.bookinglite.service.UserService;
import com.softserve.edu.bookinglite.service.dto.LoginDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {




    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private ApplicationEventMulticaster applicationEventMulticaster;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    UserService userService;

    @Test(expected = UserNotFoundException.class)
    public void testLoginUserNotExist() throws UserNotFoundException,UserIsNotVerifiedException,BadUserCredentialsException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("hello@gmail.com");
        loginDto.setPassword("sdadsa");
        Mockito.when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(null);
        userService.loginUser(loginDto);
    }


}
