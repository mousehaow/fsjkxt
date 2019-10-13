package com.toby.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class EquipModel implements Serializable {

    @Id
    @Column(length=36)
    private String id;

    @Column
    private Double value;

    @Column
    private Double electricQuantity;

    @Column
    private Date timeStamp;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @Column
    private Boolean online;

    @Column
    private String country;

    @Column
    private String province;

    @Column
    private String city;

    @Column
    private String localDes;

    @Column
    private Double  thresholdValue = 1.0;

    @Column
    private String lastRecordId;

    public EquipModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getElectricQuantity() {
        return electricQuantity;
    }

    public void setElectricQuantity(Double electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalDes() {
        return localDes;
    }

    public void setLocalDes(String localDes) {
        this.localDes = localDes;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getLastRecordId() {
        return lastRecordId;
    }

    public void setLastRecordId(String lastRecordId) {
        this.lastRecordId = lastRecordId;
    }
}
