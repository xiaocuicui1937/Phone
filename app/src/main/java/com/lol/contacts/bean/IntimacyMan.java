package com.lol.contacts.bean;

/**
 * Created by hh on 2016/4/21.
 */
public class IntimacyMan {

    private String name;
    private int logoId;

    public IntimacyMan(String name, int logoId) {
        this.name = name;
        this.logoId = logoId;
    }

    public IntimacyMan() {
    }

    public String getName() {
        return name;
    }

    public int getLogoId() {
        return logoId;
    }
}
