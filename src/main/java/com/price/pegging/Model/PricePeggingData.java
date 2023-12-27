package com.price.pegging.Model;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;

import java.util.List;

public class PricePeggingData {
    private String code;
    private String  msg;

    public List<PricePegging> getPricePeggingList() {
        return pricePeggingList;
    }

    public void setPricePeggingList(List<PricePegging> pricePeggingList) {
        this.pricePeggingList = pricePeggingList;
    }

    private List<PricePegging> pricePeggingList;



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


}
