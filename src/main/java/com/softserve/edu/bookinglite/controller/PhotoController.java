package com.softserve.edu.bookinglite.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.bookinglite.exception.PhotoNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyConfirmOwnerException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.exception.TooLargePhotoSizeException;
import com.softserve.edu.bookinglite.exception.WrongPhotoFormatException;
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
											@PathVariable("property_id") Long property_id,
											Principal principal) throws NumberFormatException, TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		photoService.uploadPhoto(file, property_id, Long.parseLong(principal.getName()));
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	@DeleteMapping("/photo/{name}")
	public ResponseEntity<Void> deletePhoto(@PathVariable("name") String name, Principal principal) throws NumberFormatException, PhotoNotFoundException, PropertyConfirmOwnerException, IOException{
		photoService.deletePhoto(name, Long.parseLong(principal.getName()));
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
