package com.example.beta1.classes;

import java.util.Date;

public class User {

    private String name, month, member, imgurl;
    private Date join, end;
    private int daysaweek;

    public User() {
    }

    public User(String member) {
        this.member = member;
    }

    public User(String name, String month, String member, int daysaweek, String imgurl) {
        this.name = name;
        this.month = month;
        this.member = member;
        this.daysaweek = daysaweek;
        this.imgurl = imgurl;
    }

    public User(String name, String month, String member, Date join, Date end, int daysaweek) {
        this.name = name;
        this.month = month;
        this.member = member;
        this.join = join;
        this.end = end;
        this.daysaweek = daysaweek;
    }

    public User(String name, String month, String member, Date join, Date end, int daysaweek, String imgurl) {
        this.name = name;
        this.month = month;
        this.member = member;
        this.join = join;
        this.end = end;
        this.daysaweek = daysaweek;
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public String getMonth() {
        return month;
    }

    public String getMember() {
        return member;
    }

    public Date getJoin() {
        return join;
    }

    public Date getEnd() {
        return end;
    }

    public int getDaysaweek() {
        return daysaweek;
    }

    public String getImgurl() {
        return imgurl;
    }
}