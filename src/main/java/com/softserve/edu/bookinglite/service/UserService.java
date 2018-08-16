package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.entity.VerificationToken;
import com.softserve.edu.bookinglite.events.RegistrationCompleteEvent;
import com.softserve.edu.bookinglite.exception.BadUserCredentialsException;
import com.softserve.edu.bookinglite.exception.EmailAlreadyUsedException;
import com.softserve.edu.bookinglite.exception.UserIsNotVerifiedException;
import com.softserve.edu.bookinglite.exception.UserNotFoundException;
import com.softserve.edu.bookinglite.repository.RoleRepository;
import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.repository.VerificationTokenRepository;
import com.softserve.edu.bookinglite.security.JwtTokenProvider;
import com.softserve.edu.bookinglite.service.dto.LoginDto;
import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventMulticaster applicationEventMulticaster;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.emailverification}")
    private Boolean emailverification;

    @Value("${app.hosturl}")
    private String appUrl;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, ApplicationEventMulticaster applicationEventMulticaster, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.applicationEventMulticaster = applicationEventMulticaster;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public String loginUser (LoginDto loginDto) throws UserNotFoundException,UserIsNotVerifiedException,BadUserCredentialsException{
        User user = userRepository.findByEmail(loginDto.getEmail());
        if(user == null){
            throw new UserNotFoundException(loginDto.getEmail());
        }
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
            return jwt;
        } catch (AuthenticationException ex){
            if(ex instanceof BadCredentialsException){
                throw new BadUserCredentialsException();
            }
        }
        return "";
    }


    @Transactional
    public void registerUser(RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setFirst_name(registerDto.getFirst_name());
        user.setLast_name(registerDto.getLast_name());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhone_number(registerDto.getPhone_number());
        user.setAddress(registerDto.getAddress());
        List<Role> roles = roleRepository.findAll();
        Set<Role> userRoles = new HashSet<Role>();
        for (Role role : roles) {
            if ("ROLE_OWNER".equals(role.getName()) && registerDto.isOwner()) {
                userRoles.add(role);
            } else if ("ROLE_USER".equals(role.getName())) {
                userRoles.add(role);
            }
        }
        user.setRoles(userRoles);

        user.setVerified(!emailverification);
        User result = userRepository.save(user);
        if(emailverification) applicationEventMulticaster.multicastEvent(new RegistrationCompleteEvent(result,appUrl));

    }
    @Transactional
    public boolean verifyUser(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken != null){
            if(new Date().before(verificationToken.getExpire_at())){
                verificationToken.getUser().setVerified(true);
                userRepository.save(verificationToken.getUser());
                verificationTokenRepository.delete(verificationToken);
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    @Transactional
    public  UserDto findById(Long id){
        UserDto user = userRepository.findById(id).map(UserMapper.instance::UserToBaseUserDtoWithRoles).orElse(new UserDto());
        return user;
    }

    @Transactional
    public String[] getUserRoles(Long userid) throws UserNotFoundException{
        ArrayList<String> roles = new ArrayList<>();
        Set<Role> roleSet = userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException()).getRoles();
        for(Role role : roleSet){
            roles.add(role.getName());
        }
        String[] result = new String[roles.size()];
        return  roles.toArray(result);
    }
    public void createVerificationToken(User user,String token){
        VerificationToken verificationToken =  new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        Date expire = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        verificationToken.setExpire_at(expire);
        verificationTokenRepository.save(verificationToken);
    }
    public void checkUser(String email) throws UserIsNotVerifiedException,EmailAlreadyUsedException{
       VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(email);
       if(verificationToken != null) {
           if (!verificationToken.getUser().isVerified()) {
               throw new UserIsNotVerifiedException();
           } else {
               throw new EmailAlreadyUsedException(email);
           }
       } else if(!userRepository.existsByEmail(email)) {
           throw new EmailAlreadyUsedException(email);
       }
    }


    
    public Optional<User> getUserById(Long id) {
    	return userRepository.findById(id);
    }
    
}
