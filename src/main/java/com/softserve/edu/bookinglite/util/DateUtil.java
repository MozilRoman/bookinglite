package com.softserve.edu.bookinglite.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date toDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       // return sdf.format(new Date(date.getYear()-1900,date.getMonth()-1,date.getDate()));
      return   new Date();
    }
    
    public static Date setHourAndMinToDate(Date date,int hour,int minuts){ 
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE,minuts);
		return calendar.getTime();
	}
    
    public static int countDay(Date checkIn, Date checkOut) {
    	return (int)(checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
    }
}
