package com.price.pegging.Model;

import lombok.Data;
@Data
public class DashboardDistinctDetail {

    private DsaData dsaData;
    private PeggingData peggingData;
    @Data
    public static class DsaData {
        private String dsaPincodeTotal;
        private String dsaZoneTotal;
        private String dsaRegionTotal;
        private String dsaLocationsTotal;
        private String dsaQuartersTotal;

        // Constructors, getters, and setters
    }
    @Data
    public static class PeggingData {
        private String peggingPincodeTotal;
        private String peggingZoneTotal;
        private String peggingLocationsTotal;
        private String peggingQuartersTotal;

        // Constructors, getters, and setters
    }

}
