package com.softserve.edu.bookinglite.repository;

import com.softserve.edu.bookinglite.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);

    @Query("SELECT t FROM VerificationToken t join t.user u WHERE u.email=?1")
    VerificationToken findByUserEmail(String email);

}
