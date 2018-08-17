package com.softserve.edu.bookinglite.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class BookingUtil {
    public static BigDecimal getPriceForPeriod(BigDecimal priceOneDay, Date checkIn, Date checkOut) {
        BigDecimal priceForPeriod= new BigDecimal(BigInteger.ZERO,2);
        int diff= DateUtil.countDay(checkIn, checkOut);
        priceOneDay= priceOneDay.multiply( new BigDecimal(diff));
        return priceForPeriod.add(priceOneDay);
    }
}
