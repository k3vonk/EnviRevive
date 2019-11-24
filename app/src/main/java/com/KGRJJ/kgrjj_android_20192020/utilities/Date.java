package com.KGRJJ.kgrjj_android_20192020.utilities;

public class Date {
    private int year;
    private int month;
    private int day;


    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};


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
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    @Override
    public String toString(){
        return day +"-"+months[month-1]+"-"+year;
    }
}
