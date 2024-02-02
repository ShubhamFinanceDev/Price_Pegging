package com.price.pegging.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data

public class PricePeggingLineChart {

    private String minimumRate;
    private String maximumRate;
    private String averageRate;
    private Date uploadDate;

    public PricePeggingLineChart(String minimumRate, String maximumRate, String averageRate, Date uploadDate) {
        this.minimumRate = minimumRate;
        this.maximumRate = maximumRate;
        this.averageRate = averageRate;
        this.uploadDate = uploadDate;
    }
}


