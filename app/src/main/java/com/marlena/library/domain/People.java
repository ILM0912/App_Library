package com.marlena.library.domain;

public class People {
    private int id;
    private String name;
    private String phone;
    private String passport;

    public People(int id, String name, String phone, String passport) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.passport = passport;
    }

    public People(String name, String phone, String passport) {
        this.name = name;
        this.phone = phone;
        this.passport = passport;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassport() {
        return passport;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "People{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", passport='" + passport + '\'' +
                '}';
    }
}
