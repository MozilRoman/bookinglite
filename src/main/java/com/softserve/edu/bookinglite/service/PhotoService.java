package com.softserve.edu.bookinglite.service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.bookinglite.entity.Photo;
import com.softserve.edu.bookinglite.repository.PhotoRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
@Service
public class PhotoService {
	@Autowired
	PhotoRepository photoRepository;
	@Autowired
	PropertyRepository propertyRepository;
	@Autowired
	Cloudinary cloudinary;
	
	public boolean uploadPhoto(MultipartFile file, Long property_id) {
		System.out.println("uploadFile()");
		if(file==null) {
			return false;
		}
		System.out.println("file not null");
		String filename = UUID.randomUUID().toString();
		Map options = ObjectUtils.asMap(                 
				"public_id", filename,
				"folder", "properties",   
				"overwrite", true);
		Map uploadResult=null;
		
		try {
			uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
			System.out.println("upload finished");
		} catch (IOException e) {
			
			System.out.println("uploadException");
			e.printStackTrace();
			return false;
		}
		
		
		Photo photo = new Photo();
		photo.setProperty(propertyRepository.getOne(property_id));
		photo.setUrl((String)uploadResult.get("url"));
		photoRepository.save(photo);
		
		return true;
	}
	
	public boolean deletePtoto(String url) {
		boolean result = true;
		Photo photo = photoRepository.findByUrl(url);
		System.out.println(photo.getId());
		try {
			photoRepository.delete(photo);
		}catch(Exception exc) {
			return false;
		}
		return result;
	}
}
