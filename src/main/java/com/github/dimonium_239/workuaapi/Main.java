package com.github.dimonium_239.workuaapi;

import com.google.cloud.Timestamp;
import org.springframework.cache.annotation.EnableCaching;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Main {
    public static void main(String[] args) throws ParseException {
        String time = "2022-09-05T17:39:34+0000";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        System.out.println(dateFormat.parse(time));
    }
}
