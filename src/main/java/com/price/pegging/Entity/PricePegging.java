package com.price.pegging.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="PRICE_PEGGING")
@Data
public class PricePegging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pegging_id")
    private Long peggingId;

    @Column(name="zone")
    private String zone;

    @Column(name="location")
    private String locations;

    @Column(name="minimum_rate")
    private String minimumRate;

    @Column(name="maximum_rate")
    private String maximumRate;

    @Column(name="average_rate")
    private String averageRate;

    @Column(name="pincode")
    private String pinCode;

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

    public Long getPeggingId() {
        return peggingId;
    }

    public void setPeggingId(Long peggingId) {
        this.peggingId = peggingId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getMinimumRate() {
        return minimumRate;
    }

    public void setMinimumRate(String minimumRate) {
        this.minimumRate = minimumRate;
    }

    public String getMaximumRate() {
        return maximumRate;
    }

    public void setMaximumRate(String maximumRate) {
        this.maximumRate = maximumRate;
    }

    public String getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(String averageRate) {
        this.averageRate = averageRate;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
