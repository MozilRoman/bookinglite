package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.entity.VerificationToken;
import com.softserve.edu.bookinglite.events.RegistrationCompleteEvent;
import com.softserve.edu.bookinglite.repository.RoleRepository;
import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.repository.VerificationTokenRepository;
import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventMulticaster applicationEventMulticaster;

    @Value("${app.emailverification}")
    private Boolean emailverification;

    @Value("${app.hosturl}")
    private String appUrl;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, ApplicationEventMulticaster applicationEventMulticaster) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.applicationEventMulticaster = applicationEventMulticaster;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //TODO: REFACTOR
    @Transactional
    public boolean registerUser(RegisterDto registerDto) {
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
            if (role.getName().equals("ROLE_OWNER") && registerDto.isOwner()) {
                userRoles.add(role);
                continue;
            } else if (role.getName().equals("ROLE_USER")) {
                userRoles.add(role);
            }
        }
        user.setRoles(userRoles);

        if(emailverification) {
            user.setVerified(false);
        }
        else user.setVerified(true);
        User result = userRepository.save(user);
        if(emailverification) applicationEventMulticaster.multicastEvent(new RegistrationCompleteEvent(result,appUrl));
        if(result != null)return true;
        else return false;
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

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    @Transactional
    public String[] getUserRoles(Long userid){
        ArrayList<String> roles = new ArrayList<>();
        Set<Role> roleSet = userRepository.findById(userid).get().getRoles();
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
    public void checkVerificationToken(String email){
       VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(email);
       if(verificationToken != null) {
           if (verificationToken.getUser().isVerified()) {
               verificationTokenRepository.delete(verificationToken);
           } else {
               User user = verificationToken.getUser();
               verificationTokenRepository.delete(verificationToken);
               userRepository.delete(user);
           }
       }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email);
        if(user != null){
           return UserMapper.instance.UsertoSecurityUser(user);
        } else {
            throw new UsernameNotFoundException("User with this email not exist: " + email);
        }
    }
    
    public Optional<User> getUserById(Long id) {
    	return userRepository.findById(id);
    }
    
}
