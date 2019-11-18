package com.toby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;

@Entity
public class SupportInfoModel implements Serializable {

    @Id
    @Column(length=10)
    @JsonIgnore
    private String creater = "admin";

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String briefIntroduction;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String instructions;

    public SupportInfoModel() {
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
