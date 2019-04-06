package com.huarui.entity;


public class User {

    private Long id;
    private String name;
    private Integer age;
    private String description;
    private String createtm;

    public User(){}
    public User(Long id, String name, Integer age, String description, String createtm) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.description = description;
        this.createtm = createtm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatetm() {
        return createtm;
    }

    public void setCreatetm(String createtm) {
        this.createtm = createtm;
    }
}
