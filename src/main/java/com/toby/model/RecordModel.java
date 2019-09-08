package com.toby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class RecordModel implements Serializable {

    @Id
    @Column(length=32)
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @Column
    private Double totalDose;

    @Column
    private Integer count;

    @Column
    private String country;

    @Column
    private String province;

    @Column
    private String city;

    @Column
    private String localDes;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @Column
    private Integer recordType;

    @Column
    private String equipAddress;

    @Column
    private Double  thresholdValue = 1.0;

    @Column
    private Boolean overThreshold = false;

    @Column
    @JsonIgnore
    private Boolean deleteFlag;


    public RecordModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getTotalDose() {
        return totalDose;
    }

    public void setTotalDose(Double totalDose) {
        this.totalDose = totalDose;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getLocalDes() {
        return localDes;
    }

    public void setLocalDes(String localDes) {
        this.localDes = localDes;
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

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getEquipAddress() {
        return equipAddress;
    }

    public void setEquipAddress(String equipAddress) {
        this.equipAddress = equipAddress;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Boolean getOverThreshold() {
        return overThreshold;
    }

    public void setOverThreshold(Boolean overThreshold) {
        this.overThreshold = overThreshold;
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
}
