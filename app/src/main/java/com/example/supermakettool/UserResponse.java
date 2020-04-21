package com.example.supermakettool;

import com.google.gson.annotations.Expose;

public class UserResponse {

    @Expose
    private int userrId;
    @Expose
    private String name;
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private int age;


    public int getUserrId() {
        return userrId;
    }

    public void setUserrId(int userrId) {
        this.userrId = userrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
