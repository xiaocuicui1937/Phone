package com.lol.contacts.bean;

/**
*亲密度的类
*@author wzq
*created at 2016/4/22 17:15
*/
public class HoneyContactInfo {
    String contact_id;
    String name;
    String phone_number;
    int score;
    int count;
    public HoneyContactInfo(String contact_id, String name, String phone_number, int score, int count) {
        this.contact_id = contact_id;
        this.name = name;
        this.phone_number = phone_number;
        this.score = score;
        this.count = count;
    }

    @Override
    public String toString() {
        return "HoneyContactInfo{" +
                "contact_id='" + contact_id + '\'' +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", score=" + score +
                ", count=" + count +
                '}';
    }
    public HoneyContactInfo() {
    }
    public String getContact_id() {
        return contact_id;
    }
    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
