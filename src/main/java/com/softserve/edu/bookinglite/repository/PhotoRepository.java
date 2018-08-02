package com.softserve.edu.bookinglite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.bookinglite.entity.Photo;
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	public Photo findByUrl(String url);
	@Query("Select p from Photo p where p.url LIKE  %?1%")
	public List<Photo> findByUrlLike(String photoName);
}
