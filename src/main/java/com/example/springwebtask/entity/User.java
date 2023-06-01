package com.example.springwebtask.entity;

public class User {
    private String loginId;
    private String password;
    private String name;
    private int role;

    public User(String loginId, String password, String name, int role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
