package com.softserve.edu.bookinglite.test.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.bookinglite.entity.Photo;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.PhotoNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyConfirmOwnerException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.exception.TooLargePhotoSizeException;
import com.softserve.edu.bookinglite.exception.WrongPhotoFormatException;
import com.softserve.edu.bookinglite.repository.PhotoRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.PhotoService;

@RunWith(MockitoJUnitRunner.class)
public class PhotoServiceUnitTest {
	@Mock
	private PhotoRepository photoRepository;
	@Mock
	private PropertyRepository propertyRepository;
	@Mock
	private MultipartFile file;
	@InjectMocks
	private PhotoService photoService;
	static final long EXIST_PROPERTY_ID = 1l;
	static final long NOT_EXIST_PROPERTY_ID = 2l;
	static final long OWNER_ID = 1l;
	static final long NOT_OWNER_ID = 2l;
	static final String WRONG_PHOTO_FORMAT = "text";
	static final String CORRECT_PHOTO_FORMAT = "image";
	static final long INVALID_SIZE = 1024*1024*16;
	static final String NOT_EXIST_PHOTO_NAME = "notexistphoto";
	static final String EXIST_PHOTO_NAME = "existphoto";
	@Test(expected = FileNotFoundException.class)
	public void uploadPhotoFileTest1() throws TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		photoService.uploadPhoto(null, EXIST_PROPERTY_ID, OWNER_ID);
	}
	@Test(expected = PropertyNotFoundException.class)
	public void uploadPhotoPropertyTest2() throws TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		Mockito.when(this.propertyRepository.findById(NOT_EXIST_PROPERTY_ID)).thenReturn(Optional.empty());
		photoService.uploadPhoto(file, NOT_EXIST_PROPERTY_ID, NOT_OWNER_ID);
	}
	@Test(expected = PropertyConfirmOwnerException.class)
	public void uploadPhotoConfirmTest3() throws TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		User propertyOwner = new User();
		propertyOwner.setId(OWNER_ID);
		Property uploadedProperty = new Property();
		uploadedProperty.setUser(propertyOwner);
		Mockito.when(propertyRepository.findById(EXIST_PROPERTY_ID)).thenReturn(Optional.of(uploadedProperty));
		this.photoService.uploadPhoto(file, EXIST_PROPERTY_ID, NOT_OWNER_ID);
	}
	@Test(expected = WrongPhotoFormatException.class)
	public void uploadPhotoFormatTest4() throws TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		User propertyOwner = new User();
		propertyOwner.setId(OWNER_ID);
		Property uploadedProperty = new Property();
		uploadedProperty.setUser(propertyOwner);
		Mockito.when(propertyRepository.findById(EXIST_PROPERTY_ID)).thenReturn(Optional.of(uploadedProperty));
		Mockito.when(file.getContentType()).thenReturn(WRONG_PHOTO_FORMAT);
		this.photoService.uploadPhoto(file, EXIST_PROPERTY_ID, OWNER_ID);
	}
	@Test(expected = TooLargePhotoSizeException.class)
	public void uploadPhotoSizeTest5() throws TooLargePhotoSizeException, WrongPhotoFormatException, PropertyConfirmOwnerException, PropertyNotFoundException, IOException {
		User propertyOwner = new User();
		propertyOwner.setId(OWNER_ID);
		Property uploadedProperty = new Property();
		uploadedProperty.setUser(propertyOwner);
		Mockito.when(propertyRepository.findById(EXIST_PROPERTY_ID)).thenReturn(Optional.of(uploadedProperty));
		Mockito.when(file.getContentType()).thenReturn(CORRECT_PHOTO_FORMAT);
		Mockito.when(file.getSize()).thenReturn(INVALID_SIZE);
		this.photoService.uploadPhoto(file, EXIST_PROPERTY_ID, OWNER_ID);
	}
	@Test(expected = PhotoNotFoundException.class)
	public void deletePhotoTest1() throws PhotoNotFoundException, PropertyConfirmOwnerException, IOException {
		Mockito.when(photoRepository.findByUrlLike(NOT_EXIST_PHOTO_NAME)).thenReturn(new LinkedList<Photo>());
		photoService.deletePhoto(NOT_EXIST_PHOTO_NAME, OWNER_ID);
	}
	@Test(expected = PropertyConfirmOwnerException.class)
	public void deletePhotoTest2() throws PhotoNotFoundException, PropertyConfirmOwnerException, IOException {
		User owner = new User();
		Property property = new Property();
		Photo deletedPhoto = new Photo();
		owner.setId(OWNER_ID);
		property.setUser(owner);
		deletedPhoto.setProperty(property);
		List photos = new LinkedList<Photo>();
		photos.add(deletedPhoto);
		Mockito.when(photoRepository.findByUrlLike(EXIST_PHOTO_NAME)).thenReturn(photos);
		photoService.deletePhoto(EXIST_PHOTO_NAME, NOT_OWNER_ID);
	}
}
