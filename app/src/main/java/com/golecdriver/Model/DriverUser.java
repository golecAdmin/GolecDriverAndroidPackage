package com.golecdriver.Model;

public class DriverUser {
    private String name;
    private String id;
    private String mail;

    public DriverUser(String name, String id, String mail) {
        this.name = name;
        this.id = id;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getid() {
        return id;
    }

    public void setid(String age) {
        this.id = age;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return name;
    }
}