package com.example.sda_project.model;
public class PrisonAdmin {
    private String name;
    private String password;

    // Constructor
    public PrisonAdmin(String name,String password) {
        this.name = name;
        this.password = password;
    }

    // toString method
    @Override
    public String toString() {
        return "Admin{" +
                ", name='" + name + '\'' +
                '}';
    }
}
