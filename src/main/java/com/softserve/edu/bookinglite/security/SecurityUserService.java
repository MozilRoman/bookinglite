package com.softserve.edu.bookinglite.security;

import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.UserRepository;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email);
        if(user != null){
            return UserMapper.instance.UsertoSecurityUser(user);
        } else {
            throw new UsernameNotFoundException("User with email'"+ email +"' not found");
        }
    }
}
