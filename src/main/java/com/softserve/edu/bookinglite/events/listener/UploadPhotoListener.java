package com.softserve.edu.bookinglite.events.listener;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.bookinglite.config.CloudinaryConfig.UploadOptions;
import com.softserve.edu.bookinglite.entity.Photo;
import com.softserve.edu.bookinglite.events.UploadPhotoEvent;
import com.softserve.edu.bookinglite.repository.PhotoRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
@Component
public class UploadPhotoListener implements ApplicationListener<UploadPhotoEvent>{
	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private Cloudinary cloudinary;
	private final String URL_RESULT_KEY = "url";
	
	@Override
	public void onApplicationEvent(UploadPhotoEvent event) {
		System.out.println("onAplicationEvent()");
		saveUrlIntoDb(saveFileIntoCloud(event.getFileBytes()),
						event.getPropertyId());
		
	}
	
	private void saveUrlIntoDb(String url, Long property_id) {
		Photo photo = new Photo();
		photo.setProperty(propertyRepository.getOne(property_id));
		photo.setUrl(url);
		photoRepository.save(photo);   
	}
	private String saveFileIntoCloud(byte[] file_bytes) {
		Map options = ObjectUtils.asMap(                 
				UploadOptions.NAME_OPTION.getName(), UploadOptions.NAME_OPTION.getValue(),
				UploadOptions.FOLDER_OPTION.getName(), UploadOptions.FOLDER_OPTION.getValue());
		Map uploadResult=null;
		try {
			uploadResult = cloudinary.uploader().upload(file_bytes, options);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String)uploadResult.get(URL_RESULT_KEY);
	}
}
