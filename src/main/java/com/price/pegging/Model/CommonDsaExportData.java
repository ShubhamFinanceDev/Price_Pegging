package com.price.pegging.Model;

import lombok.Data;

import java.util.List;
@Data
public class CommonDsaExportData {
    private String msg;
    private String code;
    private List<DsaExportData> dsaExportData;
}
