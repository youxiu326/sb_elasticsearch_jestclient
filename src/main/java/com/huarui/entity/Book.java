package com.huarui.entity;

import java.util.Date;

/**
 * Created by lihui on 2019/1/30.
 */
public class Book extends SearchEntity {

    private String name;

    private String code;

    private float price;

    public Book(){}
    public Book(String id, Date createTime, String code, String name, float price) {
        super(id,createTime);
        this.name = name;
        this.price = price;
        this.code = code;
    }

    public Book(String name, float price, String code) {
        this.name = name;
        this.price = price;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", price=" + price +
                '}';
    }
}
