package com.price.pegging.Model;

import lombok.Data;

@Data
public class DsaExportData {
    private String property_address;
    private String lattitude;
    private String longitude;

    public DsaExportData(String property_address, String lattitude, String longitude) {
        this.property_address = property_address;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public void setProperty_address(String property_address) {
        this.property_address = property_address;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
