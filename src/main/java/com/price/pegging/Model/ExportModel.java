package com.price.pegging.Model;

import com.price.pegging.Entity.DsaExport;

import java.util.List;

public class ExportModel {
    private String code;
    private String  msg;

    private List<DsaExport> dsaExportList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return  msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DsaExport> getDsaExportList() {
        return dsaExportList;
    }

    public void setDsaExportList(List<DsaExport> dsaExportList) {
        this.dsaExportList = dsaExportList;
    }
}
