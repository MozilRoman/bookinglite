package com.softserve.edu.bookinglite.repository;

import com.softserve.edu.bookinglite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {
    public User findByEmail(String email);
    public boolean existsByEmail(String email);


}
