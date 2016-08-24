package com.example.manfredi.glicemy.db.model;

/**
 * Created by Manfredi on 23/08/2016.
 */
public class Property {

    private String value;
    private String date;
    private String time;
    private String meal;

    // constructor
    public Property(String value, String date, String time, String meal) {
        this.value = value;
        this.date = date;
        this.time = time;
        this.meal = meal;
    }

    // getters and setters


    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
