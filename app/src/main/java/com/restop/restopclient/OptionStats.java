package com.restop.restopclient;

public class OptionStats {
    private String name;
    private String price;
    private int quantity;
    private String img;

    public OptionStats(String name, String price, int quantity, String img) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
