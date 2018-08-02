package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.service.dto.RegisterDto;
import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import com.softserve.edu.bookinglite.repository.AddressRepository;
import com.softserve.edu.bookinglite.repository.RoleRepository;
import com.softserve.edu.bookinglite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


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
    public  User findById(Long id){
        return userRepository.findById(id).get();
    }
    public String[] getUserRoles(Long userid){
        User user = new User();
        user.setId(userid);
        ArrayList<String> roles = new ArrayList<>();
        Set<Role> roleSet = userRepository.findById(userid).get().getRoles();
        for(Role role : roleSet){
            roles.add(role.getName());
        }
        String[] result = new String[roles.size()];
        return  roles.toArray(result);
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email);
        if(user != null){
           return UserMapper.toSecurityUser(user);
        } else {
            throw new UsernameNotFoundException("User with this email not exist: " + email);
        }
    }
}
