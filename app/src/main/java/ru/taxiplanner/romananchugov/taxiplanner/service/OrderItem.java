package ru.taxiplanner.romananchugov.taxiplanner.service;

import java.util.ArrayList;

/**
 * Created by romananchugov on 03.01.2018.
 */

public class OrderItem {
    private String placeFrom;
    private String placeTo;

    private int year;
    private int month;
    private int day;
    private String date;

    private int hour;
    private int minutes;
    private String time;

    private String description;

    private String stringForSearch;

    private String userCreatedId;

    private int numberOfSeatsInCar;
    private int numberOfOccupiedSeats;

    private ArrayList<String> joinedUsers;

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getJoinedUsers() {
        return joinedUsers;
    }

    public void addJoinedUser(String user){
        joinedUsers.add(user);
    }
    public void removeJoinedUser(String user){
        joinedUsers.remove(user);
    }

    public void setJoinedUsers(ArrayList<String> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public OrderItem(){
        date = "";
        time = "";
        placeTo = "";
        placeFrom = "";
        description = "";
        numberOfSeatsInCar = 0;
        joinedUsers = new ArrayList<>();
    }

    public int getNumberOfOccupiedSeats() {
        return numberOfOccupiedSeats;
    }

    public void setNumberOfOccupiedSeats(int numberOfOccupiedSeats) {
        this.numberOfOccupiedSeats = numberOfOccupiedSeats;
    }

    public String getStringForSearch() {
        return stringForSearch;
    }

    public void setStringForSearch() {
        stringForSearch = placeFrom.replaceAll("\\s+", "").toLowerCase()+placeTo.replaceAll("\\s+", "").toLowerCase();
    }

    public void setPlaceFrom(String placeFrom) {
        this.placeFrom = placeFrom;
        setStringForSearch();
    }

    public void setPlaceTo(String placeTo) {
        this.placeTo = placeTo;
        setStringForSearch();
    }

    public String getPlaceFrom() {
        return placeFrom;
    }

    public String getPlaceTo() {
        return placeTo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;

        String result = "";

        if(day < 10){
            result += "0" + day;
        }else{
            result += day;
        }

        result += ".";

        if(month < 10){
            result += "0" + month;
        }else{
            result += month;
        }

        result += "." + year;

        date = result;
    }

    public void setStringForSearch(String stringForSearch) {
        this.stringForSearch = stringForSearch;
    }

    public String getUserCreatedId() {
        return userCreatedId;
    }

    public void setUserCreatedId(String userCreatedId) {
        this.userCreatedId = userCreatedId;
    }

    public int getNumberOfSeatsInCar() {
        return numberOfSeatsInCar;
    }

    public void setNumberOfSeatsInCar(int numberOfSeatsInCar) {
        this.numberOfSeatsInCar = numberOfSeatsInCar;
    }

    public void setTime(int minutes, int hour){
        this.hour = hour;
        this.minutes = minutes;

        String result = "";

        if(hour < 10){
            result += "0" + hour;
        }else{
            result += hour;
        }

        result += ":";

        if(minutes < 10){
            result += "0" + minutes;
        }else{
            result += minutes;
        }

        time = result;

    }

    public String getTime() {
        return time;
    }

    public void setDate(int year, int month, int day, int hour, int minutes){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;

        String result = "";

        if(hour < 10){
            result += "0" + hour;
        }else{
            result += hour;
        }

        result += ":";

        if(minutes < 10){
            result += "0" + minutes;
        }else{
            result += minutes;
        }

        time = result;

        result = " ";

        if(day < 10){
            result += "0" + day;
        }else{
            result += day;
        }

        result += ".";

        if(month < 10){
            result += "0" + month;
        }else{
            result += month;
        }

        result += "." + year;

        date = result;

    }

    @Override
    public String toString() {
        String result = "";
        result += "PlaceFrom: " + placeFrom + "\n";
        result += "PlaceTo: " + placeTo + "\n";
        result += "Date: " + date + "\n";
        result += "Time: " + time + "\n";
        result += "OrderDescription: " + description + "\n";
        result += "NumberOfSeatsInCar: " + numberOfSeatsInCar + "\n";
        result += "UserCreatedId: " + userCreatedId + "\n";
        return result;
    }
}
