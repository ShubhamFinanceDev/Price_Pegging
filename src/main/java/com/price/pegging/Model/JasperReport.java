package com.price.pegging.Model;

import lombok.Data;

@Data
public class JasperReport {

    private String  s_no;
    private String application_no;
    private  String disbursal_date;
    private String property_address;
    private String property_pinCode;
    private String region;
    private String zone;
    private String location;
    private String rate_per_sqft;
    private String property_type;
    private String lattitude;
    private String longitude;
    private String product;
    private String upload_date;          // create model for show on postman

    private String minimum_rate;
    private String maximum_rate;
    private String flag;




}
