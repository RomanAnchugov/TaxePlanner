package ru.taxiplanner.romananchugov.taxiplanner.service;

/**
 * Created by romananchugov on 15.02.2018.
 */

public class UserItem {
    private String name;
    private String surname;
    private String phoneNumber;

    public UserItem(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }
    public UserItem(){

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
