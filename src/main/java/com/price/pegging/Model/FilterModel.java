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
        private List<ZoneDis> zoneDis;
        private List<Region> region;
    }
    @Data
    public static class Dsa
    {
        private List<ZoneDis> zoneDis;
        private List<Region> region;
    }
    @Data
    public static class ZoneDis
    {
        public String zoneDis;

    }
    @Data
    public static class Region
    {
        private String region;

    }

}
