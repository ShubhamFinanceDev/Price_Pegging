package com.price.pegging.Model;

import lombok.Data;

import java.util.List;
@Data
public class FilterModel {
    private Pegging pegging;
    private Dsa dsa;
    private String msg;
    private String code;
    @Data
    public static class Pegging
    {
        private List<Zone> zone;

    }
    @Data
    public static class Dsa
    {
        private List<Zone> zone;
        private List<Region> region;
    }
    @Data
    public static class Zone
    {
        public String zone;

    }
    @Data
    public static class Region
    {
        private String region;

    }

}
