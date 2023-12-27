package com.price.pegging.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="DSA_EXPORT")
@Data
public class DsaExport {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="s_no")
    private Long  sNo;
    @Column(name="application_no")
    private String applicationNo;
    @Column(name="product")
    private String product;
    @Column(name="disbursal_date")
    private String disbursalDate;
    @Column(name="property_address")
    private String property_address;
    @Column(name="property_pincode")
    private String propertyPincode;
    @Column(name="region")
    private String region;
    @Column(name="zone")
    private String zone;
    @Column(name="location")
    private String location;
    @Column(name="rate_per_sqft")
    private String rate_per_sqft;
    @Column(name="property_type")
    private String property_type;
    @Column(name="lattitude")
    private String lattitude;
    @Column(name="longitude")
    private String longitude;

    @Temporal(TemporalType.DATE)
    @Column(name="upload_date",nullable = false)
    private String uploadDate;
    @PrePersist
    private void oncreate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        uploadDate = currentDate.format(formatter);
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getsNo() {
        return sNo;
    }

    public void setsNo(Long sNo) {
        this.sNo = sNo;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDisbursalDate() {
        return disbursalDate;
    }

    public void setDisbursalDate(String disbursalDate) {
        this.disbursalDate = disbursalDate;
    }

    public String getProperty_address() {
        return property_address;
    }

    public void setProperty_address(String property_address) {
        this.property_address = property_address;
    }

    public String getPropertyPincode() {
        return propertyPincode;
    }

    public void setPropertyPincode(String propertyPincode) {
        this.propertyPincode = propertyPincode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRate_per_sqft() {
        return rate_per_sqft;
    }

    public void setRate_per_sqft(String rate_per_sqft) {
        this.rate_per_sqft = rate_per_sqft;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
