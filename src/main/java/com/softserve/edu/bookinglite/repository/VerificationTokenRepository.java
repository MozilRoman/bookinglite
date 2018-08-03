package com.softserve.edu.bookinglite.repository;

import com.softserve.edu.bookinglite.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);

}
