package com.price.pegging.Model;

import lombok.Data;

import java.util.List;
@Data
public class CommonResponseForLineChart {
    private String msg;
    private String code;
    private List<PricePeggingLineChart> pricePeggingLineCharts;
}
