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


}
