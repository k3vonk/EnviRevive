package com.KGRJJ.kgrjj_android_20192020.utilities;

public class Time {
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    private int hour;
    private int minute;
    public Time(int hour,int minute){
        this.hour = hour;
        this.minute = minute;
    }
}
