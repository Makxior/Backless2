package com.example.backless2;

public class Person {
    private String name;
    private String surname;

    public Person() {
        this.name = null;
        this.surname = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
