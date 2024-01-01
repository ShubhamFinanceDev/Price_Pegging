package com.price.pegging.Model;

import lombok.Data;

import java.util.List;

@Data
public class DashboardGraph {
    private DsaData dsaData;
    private PeggingData peggingData;
    private String msg;
    private String code;
  @Data
    public static class DsaData
    {
        private List<Pincode> pincode;
        private List<Location> location;
    }
    @Data
    public static class Pincode
    {
        private String date;
        private String total;
    }

    @Data
    public static class Location
    {
        private String date;
        private String total;
    }
    @Data
    public static class PeggingData
    {
        private List<Pincode> pincode;
        private List<Location> location;
    }

}
