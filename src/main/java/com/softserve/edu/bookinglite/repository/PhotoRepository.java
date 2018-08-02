package com.softserve.edu.bookinglite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.bookinglite.entity.Photo;
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	public Photo findByUrl(String url);
}
