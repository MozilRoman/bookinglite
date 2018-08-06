package com.softserve.edu.bookinglite.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtil {
    public static Date toDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       // return sdf.format(new Date(date.getYear()-1900,date.getMonth()-1,date.getDate()));
      return   new Date();
    }
}
