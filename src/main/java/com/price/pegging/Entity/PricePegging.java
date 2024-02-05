package com.price.pegging.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.sql.Date;
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
    @Column(name="region")
    private String region;
    @Column(name="zone_dist")
    private String zoneDist;
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

//    @Temporal(TemporalType.DATE)
  //  @Column(name="upload_date",nullable = false)
   @Column(name="upload_date")
    private Date uploadDate;

//    @PrePersist
//    private void oncreate() {
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        uploadDate = currentDate.format(formatter);
//    }

}
