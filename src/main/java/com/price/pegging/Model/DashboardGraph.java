package com.price.pegging.Model;

import lombok.Data;

import java.util.List;

@Data
public class DashboardGraph {
    private List<DsaData> dsaData;
    private List<PeggingData> peggingData;
  @Data
    public static class DsaData
    {
        private String date;
        private String total;
    }
    @Data
    public static class PeggingData
    {
        private String date;
        private String total;
    }

}
