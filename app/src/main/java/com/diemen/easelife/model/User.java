package com.diemen.easelife.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by user on 23-01-2015.
 */
public class User {

    public User() {
    }

    public User(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;

    }

    User(String name,String phoneNo,String contact_id){
        this.name=name;
        this.phoneNo=phoneNo;
        this.contact_id=contact_id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @DatabaseField(generatedId = true)
    private int userid;

    @DatabaseField
    private String name;

    @DatabaseField
    private String contact_id;

    public String getcontact_id() {
        return contact_id;
    }

    public void setcontact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @DatabaseField
    private String phoneNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
