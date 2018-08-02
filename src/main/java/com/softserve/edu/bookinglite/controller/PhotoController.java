package com.softserve.edu.bookinglite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.softserve.edu.bookinglite.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/api")
public class PhotoController {
	@Autowired
	PhotoService photoService;
	
	@PostMapping("property/{property_id}/photo")
	public ResponseEntity<Void> uploadPhoto(@RequestParam("file") MultipartFile file, 
			@PathVariable("property_id") Long property_id) {
		if(photoService.uploadPhoto(file, property_id)) {
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/photos/{url}")
	public ResponseEntity<Void> deletePhoto(@PathVariable("url") String url){
		if(photoService.deletePtoto(url)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}else{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
