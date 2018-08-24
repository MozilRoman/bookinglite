package com.softserve.edu.bookinglite.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date toDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // return sdf.format(new Date(date.getYear()-1900,date.getMonth()-1,date.getDate()));
        return new Date();
    }

    public static Date setHourAndMinToDate(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public static int countDay(Date checkIn, Date checkOut) {
        return (int) (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
    }

    public static boolean checkValidationDate(Date in, Date out) {
        boolean isValid = true;

        if (out.before(in)) {
            isValid = false;
        }
        if (in.before(new Date())) {
            isValid = false;
        }
        if (out.compareTo(in) == 0) {
            isValid = false;
        }
        return isValid;
    }
}
