package com.price.pegging.Model;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import lombok.Data;

import java.util.List;
@Data
public class PricePeggingData {
    private String code;
    private String  msg;
    private boolean nextPage;
    private Long totalCount;     // add column total count  ticket No 3302




    private List<PricePegging> pricePeggingList;







}
