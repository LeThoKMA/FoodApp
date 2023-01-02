package com.example.footapp.model;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String name;
    private int price;
    private int amount;
    private String imgUrl;
    private int type;

    public Item() {
    }

    public Item(int id, String name, int price, int amount, String imgUrl, int type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
