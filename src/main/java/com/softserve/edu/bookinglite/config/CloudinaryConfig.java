package com.softserve.edu.bookinglite.config;

import java.util.UUID;

public class CloudinaryConfig {
	static public enum CloudinaryData{
		CLOUD_NAME("cloud_name","lv326"),
		API_KEY("api_key","434135777253576"),
		API_SECRET("api_secret","-n6YNc-sWhCDcYwX7pF1eVNhhUw");
		private String name;
		private String value;
		private CloudinaryData(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}	
	}
	static public enum UploadOptions{
		NAME_OPTION("public_id", "filename"),
		FOLDER_OPTION("folder", "properties"),
		OVERWRITE_OPTION("overwrite", true);
		private String name;
		private Object value;
		private UploadOptions(String name, Object value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public Object getValue() {
			if(name.equals(this.NAME_OPTION.getName())){
				return UUID.randomUUID().toString();
			}
			return value;
		}
		
	}
}
