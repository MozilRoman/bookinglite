package com.softserve.edu.bookinglite.mapper;

import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class UserMapper {
    //TODO: REFACTOR
    public static org.springframework.security.core.userdetails.User toSecurityUser(User user){
        String[] roles  = new String[user.getRoles().size()];
        for(int i = 0;i<user.getRoles().size();i++){
            roles[i] = user.getRoles().iterator().next().getName();
        }

        return new org.springframework.security.core.userdetails.User(user.getId().toString(),user.getPassword(),AuthorityUtils.createAuthorityList(roles));
    }
}
