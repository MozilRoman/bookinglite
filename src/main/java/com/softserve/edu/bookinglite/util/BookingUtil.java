package com.softserve.edu.bookinglite.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class BookingUtil {
    public static BigDecimal getPriceForPeriod(BigDecimal priceOneDay, Date checkIn, Date checkOut) {
        BigDecimal priceForPeriod = new BigDecimal(BigInteger.ZERO, 2);
        int numberOfDays = DateUtils.countDay(checkIn, checkOut);
        return priceForPeriod.add(priceOneDay.multiply(new BigDecimal(numberOfDays)));
    }
}
