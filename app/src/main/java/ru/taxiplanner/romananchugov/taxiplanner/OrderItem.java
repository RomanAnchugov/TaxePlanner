package ru.taxiplanner.romananchugov.taxiplanner;

/**
 * Created by romananchugov on 03.01.2018.
 */

public class OrderItem {
    private String placeFrom;
    private String placeTo;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private String date;
    private String description;
    private String stringForSearch;

    public String getStringForSearch() {
        return stringForSearch;
    }

    public void setStringForSearch() {
        stringForSearch = placeFrom.replaceAll("\\s+", "").toLowerCase()+placeTo.replaceAll("\\s+", "").toLowerCase();
    }

    public void setPlaceFrom(String placeFrom) {
        this.placeFrom = placeFrom;
    }

    public void setPlaceTo(String placeTo) {
        this.placeTo = placeTo;
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

        result += " ";

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
    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
