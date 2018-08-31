package com.softserve.edu.bookinglite.config;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
@Configuration
@PropertySource("file:src/main/resources/cloudinary.properties")
public class CloudinaryConfig {
	private static final String CLOUD_NAME_KEY = "cloud_name";
	private static final String API_KEY_KEY = "api_key";
	private static final String API_SECRET_KEY = "api_secret";
	private static final String PROPERTY_PHOTO_NAME_OPTION_NAME = "public_id";
	private static final String PROPERTY_PHOTO_FOLDER_OPTION_NAME = "folder";
	@Value("${propertyPhoto.option.folder}")
	public static final String PROPERTY_PHOTO_FOLDER_OPTION_VALUE = null;
	private static final String PROPERTY_PHOTO_OVERWRITE_OPTION_NAME = "overwrite";
	@Value("${propertyPhoto.option.overwrite}")
	private static final Boolean PROPERTY_PHOTO_OVERWRITE_OPTION_VALUE = null;
	@Autowired
	Environment env;
	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				CLOUD_NAME_KEY, env.getProperty(CLOUD_NAME_KEY),
				API_KEY_KEY,    env.getProperty(API_KEY_KEY),
				API_SECRET_KEY, env.getProperty(API_SECRET_KEY)));
	}
	public static Map createPropertyPhotoOptions() {
		return ObjectUtils.asMap(                
				PROPERTY_PHOTO_NAME_OPTION_NAME,      UUID.randomUUID().toString(),
				PROPERTY_PHOTO_FOLDER_OPTION_NAME,    PROPERTY_PHOTO_FOLDER_OPTION_VALUE,
				PROPERTY_PHOTO_OVERWRITE_OPTION_NAME, PROPERTY_PHOTO_OVERWRITE_OPTION_VALUE);
	}
}
