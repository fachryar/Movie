package com.fachryar.moviecatalogue.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {

    private static String dateFormat(String date, String type){
        String result = "";

        SimpleDateFormat moviedbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date defaultDate = moviedbFormat.parse(date);
            SimpleDateFormat customFormat = new SimpleDateFormat(type, Locale.getDefault());
            result = customFormat.format(defaultDate);
        } catch (ParseException e){
            e.printStackTrace();
        }

        return result;
    }

    public static String getYear(String date){
        return dateFormat(date, AppConfig.FORMAT_YEAR_ONLY);
    }

    public static String getDate(String date, String locale){
        if (locale.equals("in_ID")){
            return dateFormat(date, AppConfig.FORMAT_DATE_INDONESIA);
        } else {
            return dateFormat(date, AppConfig.FORMAT_DATE_ENGLISH);
        }
    }
}
