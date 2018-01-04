package ru.taxiplanner.romananchugov.taxiplanner;

/**
 * Created by romananchugov on 03.01.2018.
 */

public class OrderItem {
    public String placeFrom;
    public String placeTo;
    //public Date date;
    public String description;

    public void setPlaceFrom(String placeFrom) {
        this.placeFrom = placeFrom;
    }

    public void setPlaceTo(String placeTo) {
        this.placeTo = placeTo;
    }

//    public void setDate(Date date) {
//        this.date = date;
//    }

    public void setDescription(String description) {
        this.description = description;
    }
}
