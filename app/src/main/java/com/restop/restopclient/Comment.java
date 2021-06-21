package com.restop.restopclient;


import com.google.firebase.database.ServerValue;

public class Comment {
    boolean expanded;
    private String content,key, reply="";
    private float rating;

    private Object timestamp;

    public Comment() {
    }

    public Comment(String content, float rating) {
        this.content = content;
        this.rating = rating;
        this.expanded = false;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getReply() {
        return reply;
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







    public void setReply(String reply) {
        this.reply = reply;
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

                ", key='" + key + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}