package com.softserve.edu.bookinglite.service;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.bookinglite.config.CloudinaryConfig;
import com.softserve.edu.bookinglite.entity.Photo;
import com.softserve.edu.bookinglite.events.UploadPhotoEvent;
import com.softserve.edu.bookinglite.exception.PhotoNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyConfirmOwnerException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.exception.TooLargePhotoSizeException;
import com.softserve.edu.bookinglite.exception.WrongPhotoFormatException;
import com.softserve.edu.bookinglite.repository.PhotoRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
@Service
public class PhotoService {
	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private Cloudinary cloudinary;
	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;
	private static final String FILE_NOT_FOUND_EXCEPTION = "File not found!";
	private final int MAX_BYTES = 1024*1024*15;
	private final String AVAILABLE_TYPE = "image";
	
	public void uploadPhoto(MultipartFile file, Long property_id, Long user_id) throws TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		if(file==null) {
			throw new FileNotFoundException(FILE_NOT_FOUND_EXCEPTION);
		}
		if(!propertyRepository.findById(property_id).isPresent()) {
			throw new PropertyNotFoundException(property_id);
		}
		if(!propertyRepository.findById(property_id).get().getUser().getId().equals(user_id)) {
			throw new PropertyConfirmOwnerException();
		}
		if(file.getContentType().indexOf(AVAILABLE_TYPE)==-1) {
			throw new WrongPhotoFormatException(file.getContentType());
		}
		if( file.getSize()>MAX_BYTES) {
			throw new TooLargePhotoSizeException(file.getSize());
		}
		applicationEventMulticaster.multicastEvent(new UploadPhotoEvent(file.getBytes(), property_id));
	}
	
	public boolean deletePhoto(String name, Long user_id) throws PhotoNotFoundException, PropertyConfirmOwnerException, IOException {
		
			
		if(photoRepository.findByUrlLike(name).isEmpty()) {
			throw new PhotoNotFoundException(name);
		}
		Photo photo = photoRepository.findByUrlLike(name).get(0);
		if(!photo.getProperty().getUser().getId().equals(user_id))
			throw new PropertyConfirmOwnerException();
		
		cloudinary.uploader().destroy(CloudinaryConfig.PROPERTY_PHOTO_FOLDER_OPTION_VALUE+"/"+name, ObjectUtils.emptyMap());
		photoRepository.delete(photo);
		
		return true;
	}
}
