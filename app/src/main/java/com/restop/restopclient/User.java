package com.restop.restopclient;

public class User {
    private String UserName;
    private String Email;
    private String ImageName;

    public User() {
    }

    public User(String userName, String email, String imageName) {
        UserName = userName;
        Email = email;
        ImageName = imageName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

}
