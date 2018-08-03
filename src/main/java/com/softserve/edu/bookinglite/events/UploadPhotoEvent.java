package com.softserve.edu.bookinglite.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

public class UploadPhotoEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;
	byte[] file_bytes;
	Long property_id;
	
	public UploadPhotoEvent(byte[] file_bytes, Long property_id) {
		super(file_bytes);
		this.file_bytes = file_bytes;
		this.property_id = property_id;
	}
	
	public byte[] getFileBytes() {
		return file_bytes;
	}
	public Long getPropertyId() {
		return property_id;
	}
	
	
	
	
	
}
