package com.toby.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class DetailModel implements Serializable {

    @Id
    @Column(length=32)
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
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
    private String equipAddress;

    @Column(nullable = false)
    private String recordId;

    public DetailModel() {
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

    public String getEquipAddress() {
        return equipAddress;
    }

    public void setEquipAddress(String equipAddress) {
        this.equipAddress = equipAddress;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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
}
