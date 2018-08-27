package com.softserve.edu.bookinglite.test.service;


import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.entity.VerificationToken;
import com.softserve.edu.bookinglite.exception.BadUserCredentialsException;
import com.softserve.edu.bookinglite.exception.EmailAlreadyUsedException;
import com.softserve.edu.bookinglite.exception.UserIsNotVerifiedException;
import com.softserve.edu.bookinglite.exception.UserNotFoundException;
import com.softserve.edu.bookinglite.repository.RoleRepository;
import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.repository.VerificationTokenRepository;
import com.softserve.edu.bookinglite.security.JwtTokenProvider;
import com.softserve.edu.bookinglite.service.UserService;
import com.softserve.edu.bookinglite.service.dto.LoginDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test(expected = UserIsNotVerifiedException.class)
    public void testLoginUserNotVerified() throws UserNotFoundException,UserIsNotVerifiedException,BadUserCredentialsException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("hello@gmail.com");
        loginDto.setPassword("sdadsa");
        User testUser = new User();
        testUser.setVerified(false);
        Mockito.when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(testUser);
        userService.loginUser(loginDto);
    }

    @Test(expected = BadUserCredentialsException.class)
    public void testLoginBadCredentials() throws UserNotFoundException,UserIsNotVerifiedException,BadUserCredentialsException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("hello@gmail.com");
        loginDto.setPassword("sdadsa");
        User testUser = new User();
        testUser.setVerified(true);
        Mockito.when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(testUser);
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ))).thenThrow(new BadCredentialsException("Bad credentials"));
        userService.loginUser(loginDto);
    }

    @Test(expected = EmailAlreadyUsedException.class)
    public void testCheckUserNoToken() throws UserIsNotVerifiedException,EmailAlreadyUsedException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("hello@gmail.com");
        loginDto.setPassword("sdadsa");
        Mockito.when(verificationTokenRepository.findByUserEmail(loginDto.getEmail())).thenReturn(null);
        Mockito.when(userRepository.existsByEmail(loginDto.getEmail())).thenReturn(true);
        userService.checkUser(loginDto.getEmail());
    }

    @Test(expected = UserIsNotVerifiedException.class)
    public void testCheckTokenNotVerified() throws UserIsNotVerifiedException,EmailAlreadyUsedException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("hello@gmail.com");
        loginDto.setPassword("sdadsa");
        User testUser = new User();
        testUser.setVerified(false);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(testUser);
        Mockito.when(verificationTokenRepository.findByUserEmail(loginDto.getEmail())).thenReturn(verificationToken);
        userService.checkUser(loginDto.getEmail());
    }

    @Test(expected = EmailAlreadyUsedException.class)
    public void testCheckTokenVerified() throws UserIsNotVerifiedException,EmailAlreadyUsedException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("hello@gmail.com");
        loginDto.setPassword("sdadsa");
        User testUser = new User();
        testUser.setVerified(true);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(testUser);
        Mockito.when(verificationTokenRepository.findByUserEmail(loginDto.getEmail())).thenReturn(verificationToken);
        userService.checkUser(loginDto.getEmail());
    }

    @Test()
    public void testVerifyUserNoSuchToken() {
        Mockito.when(verificationTokenRepository.findByToken("testtoken")).thenReturn(null);
        assertThat(userService.verifyUser("testtoken")).isEqualTo(false);
    }

    @Test()
    public void testUserDtoPositive() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhoneNumber("1231312");
        user.setPassword("1223123");
        UserDto expectedDto = new UserDto();
        expectedDto = UserMapper.instance.UserToBaseUserDtoWithRoles(user);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThat(userService.findById(1L)).isEqualTo(expectedDto);
    }












}
