package com.price.pegging.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
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
    private Date disbursalDate;
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


}
