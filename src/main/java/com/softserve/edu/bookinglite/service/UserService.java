package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.AddressRepository;
import com.softserve.edu.bookinglite.repository.RoleRepository;
import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    //TODO: REFACTOR
    @Transactional
    public boolean registerUser(RegisterDto registerDto){
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setFirst_name(registerDto.getFirst_name());
        user.setLast_name(registerDto.getLast_name());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhone_number(registerDto.getPhone_number());
        user.setAddress(registerDto.getAddress());
        List<Role> roles = roleRepository.findAll();
        Set<Role> userRoles = new HashSet<Role>();
        userRoles.add(roles.get(0));
        if(registerDto.isOwner()){
            userRoles.add(roles.get(1));
        }
        user.setRoles(userRoles);
        User result = userRepository.save(user);
        if(result != null)return true;
        else return false;
    }


    public  UserDto findById(Long id){
        UserDto user = userRepository.findById(id).map(UserMapper.instance::UserToBaseUserDtoWithRoles).orElse(new UserDto());
        return user;
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
