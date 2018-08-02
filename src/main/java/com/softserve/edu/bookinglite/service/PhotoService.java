package com.softserve.edu.bookinglite.service;
import java.io.IOException;
import java.util.Map;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.softserve.edu.bookinglite.config.CloudinaryConfig.UploadOptions;
import com.softserve.edu.bookinglite.entity.Photo;
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
	private TaskExecutor taskExecutor;
	
	private final String URL_RESULT_KEY = "url";
	private final int MAX_BYTES = 1024*1024*15;
	private final String AVAILABLE_TYPE = "image";
	@Async
	public boolean uploadPhoto(MultipartFile file, Long property_id) {
		
		if(file==null || file.getContentType().indexOf(AVAILABLE_TYPE)==-1 || file.getSize()>MAX_BYTES) {
			return false;
		}
		
		taskExecutor.execute(new Runnable() {  
			   @Override  
			   public void run() {  
					Map options = ObjectUtils.asMap(                 
							UploadOptions.NAME_OPTION.getName(), UploadOptions.NAME_OPTION.getValue(),
							UploadOptions.FOLDER_OPTION.getName(), UploadOptions.FOLDER_OPTION.getValue());
					Map uploadResult=null;
					try {
						uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Photo photo = new Photo();
					photo.setProperty(propertyRepository.getOne(property_id));
					photo.setUrl((String)uploadResult.get(URL_RESULT_KEY));
					photoRepository.save(photo);   
			   }  
			 });  
		return true;
	}
	//name example: 047059ef-6950-469c-8d44-90a311f39982
	public boolean deletePtoto(String name) {
		try {
			Photo photo = photoRepository.findByUrlLike(name).get(0);
			cloudinary.uploader().destroy(UploadOptions.FOLDER_OPTION.getValue()+"/"+name, ObjectUtils.emptyMap());
			photoRepository.delete(photo);
		}catch(Exception exc) {
			return false;
		}
		return true;
	}
}
