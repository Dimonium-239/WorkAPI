package com.github.dimonium_239.workuaapi.firebase.dto;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Post {

    @Getter @Setter
    private String id;
    @Getter
    private Date created_time;

    @Getter @Setter
    private String message;

    @Getter @Setter
    private String story;


    public void setCreated_time(String created_time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        this.created_time = dateFormat.parse(created_time);
    }

    public void updateDate(Date date){
        this.created_time = new Date(date.toString());
    }

    @Override
    public String toString() {
        return "Post{" +
                "\nid='" + id + '\'' +
                ",\n created_time=" + created_time +
                ",\n message='" + message + '\'' +
                "\n}";
    }
}
