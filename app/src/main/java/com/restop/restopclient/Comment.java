package com.restop.restopclient;


import com.google.firebase.database.ServerValue;

public class Comment {
    private String content, uimg, uname,key;
    private float rating;

    private Object timestamp;

    public Comment() {
    }

    public Comment(String content,String uimg, String uname,float rating) {
        this.content = content;
        this.uimg = uimg;
        this.uname = uname;
        this.rating = rating;

        this.timestamp = ServerValue.TIMESTAMP;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +

                ", uimg='" + uimg + '\'' +
                ", uname='" + uname + '\'' +
                ", key='" + key + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}