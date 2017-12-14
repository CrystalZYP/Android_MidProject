package com.stone.transition;

import java.io.Serializable;

/**
 * Created by Crystal on 2017/11/16.
 */

public class Hero implements Serializable {
    private String first_letter;
    private String name;
    private String sex;
    private String place;
    private String birth_year;
    private String death_year;
    private String power;
    private String event;
    private String profile;
    private int id;

    //初始化
    Hero(String letter, String name, String sex, String place, String birth_year, String death_year, String power, String event, String profile,int id) {
        this.first_letter = letter;
        this.name = name;
        this.sex = sex;
        this.place = place;
        this.birth_year = birth_year;
        this.death_year = death_year;
        this.power = power;
        this.event = event;
        this.profile = profile;
        this.id = id;
    }

    //设置属性
    public void setFirstLetter(String first_letter) {
        this.first_letter = first_letter;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }
    public void setDeath_year(String death_year) {
        this.death_year = death_year;
    }
    public void setPower(String power) {
        this.power = power;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }

    //获取属性
    public String getFirstLetter() {
        return this.first_letter;
    }
    public String getName() {
        return this.name;
    }
    public String getSex() {
        return this.sex;
    }
    public String getPlace() {
        return this.place;
    }
    public String getBirth_year() {
        return this.birth_year;
    }
    public String getDeath_year() {
        return this.death_year;
    }
    public String getPower() {
        return this.power;
    }
    public String getEvent() {
        return this.event;
    }
    public String getProfile() {
        return this.profile;
    }
    public int getId() {
        return this.id;
    }
}
