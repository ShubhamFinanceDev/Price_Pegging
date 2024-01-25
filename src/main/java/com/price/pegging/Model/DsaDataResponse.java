package com.price.pegging.Model;

import lombok.Data;

import java.util.List;

@Data
public class DsaDataResponse {
    private String msg;
    private String code;
    private List<DsaDataModel> dsaDataModelList;
}
