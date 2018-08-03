package com.softserve.edu.bookinglite.service;
import java.io.IOException;
import java.util.Map;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.softserve.edu.bookinglite.config.CloudinaryConfig.UploadOptions;
import com.softserve.edu.bookinglite.entity.Photo;
import com.softserve.edu.bookinglite.events.UploadPhotoEvent;
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
	
	private final int MAX_BYTES = 1024*1024*15;
	private final String AVAILABLE_TYPE = "image";
	
	public boolean uploadPhoto(MultipartFile file, Long property_id, Long user_id) {
		
		
		if(!propertyRepository.getOne(property_id).getUser().getId().equals(user_id) 
				|| file==null 
				|| file.getContentType().indexOf(AVAILABLE_TYPE)==-1 
				|| file.getSize()>MAX_BYTES) {
			return false;
		}else {
			
			try {
				applicationEventMulticaster.multicastEvent(new UploadPhotoEvent(file.getBytes(), property_id));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean deletePtoto(String name, Long user_id) {
		try {
			Photo photo = photoRepository.findByUrlLike(name).get(0);
			
			if(!photo.getProperty().getUser().getId().equals(user_id))
				throw new Exception("not secure");
			
			cloudinary.uploader().destroy(UploadOptions.FOLDER_OPTION.getValue()+"/"+name, ObjectUtils.emptyMap());
			photoRepository.delete(photo);
		}catch(Exception exc) {
			return false;
		}
		return true;
	}
}
