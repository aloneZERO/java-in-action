package com.leo.java8.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 解决 SimpleDateFormat 线程安全问题
 */
public class DateFormatThreadLocal {

    private static final ThreadLocal<DateFormat> DF =
            ThreadLocal.withInitial(
                    () -> new SimpleDateFormat("yyyyMMdd"));

    public static Date convert(String source) throws ParseException {
        return DF.get().parse(source);
    }

}
