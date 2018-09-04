package com.softserve.edu.bookinglite.events.listener;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.cloudinary.Cloudinary;
import com.softserve.edu.bookinglite.config.CloudinaryConfig;
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
	private final String RESULT_URL_KEY = "url";
	@Override
	public void onApplicationEvent(UploadPhotoEvent event) {
		saveUrlIntoDb(saveFileIntoCloud(event.getFileBytes(), CloudinaryConfig.createPropertyPhotoOptions()),
						event.getPropertyId());
		
	}
	private void saveUrlIntoDb(String url, Long property_id) {
		Photo photo = new Photo();
		photo.setProperty(propertyRepository.getOne(property_id));
		photo.setUrl(url);
		photoRepository.save(photo);   
	}
	private String saveFileIntoCloud(byte[] file_bytes, Map options) {
		Map uploadResult=null;
		try {
			uploadResult = cloudinary.uploader().upload(file_bytes, options);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String)uploadResult.get(RESULT_URL_KEY);
	}
}