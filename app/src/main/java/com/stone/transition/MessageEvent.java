package com.stone.transition;

/**
 * Created by Eileen on 2017/11/21.
 */

public class MessageEvent {
    int op_tag;    // add-1  , delete-2 , edit-3
    String name;   //作为搜索tag
    String new_name;
    String firstLetter;
    String power;
    String sex;
    String birth_year;
    String death_year;
    String place;
    String event;
    String img;
    int id;

    public MessageEvent(int op_tag, String name, String new_name, String firstLetter, String power, String sex,
                        String birth_year, String death_year, String place, String event, String img,int id){
        this.op_tag = op_tag;
        this.name = name;
        this.new_name = new_name;
        this.firstLetter = firstLetter;
        this.power = power;
        this.sex = sex;
        this.birth_year = birth_year;
        this.death_year = death_year;
        this.place = place;
        this.event = event;
        this.img = img;
        this.id = id;
    }
    public void setName(String name){this.name = name;}
    public void setLetter(String firstLetter){this.firstLetter = firstLetter;}
    public void setPower(String power){this.power = power;}
    public void setSex(String sex){this.sex = sex;}
    public void setBirth_year(String birth_year){this.birth_year = birth_year;}
    public void setDeath_year(String death_year){this.death_year = death_year;}
    public void setPlace(String place){this.place = place;}
    public void setEvent(String event){this.event = event;}
    public void setImg(String img){this.img = img;}
    public String getImg(){return img;}
}
