package com.price.pegging.Model;

import java.util.ArrayList;
import java.util.List;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;

import lombok.Data;

@Data
public class DsaDataComparison {

	private int s_no;
	private String applicationNo;
	private String disbursal_date;
	private String property_address;
	private String propertyPincode;
	private String region;
	private String zone;
	private String location;
	private String rate_per_sqft;
	private String property_type;
	private String lattitude;
	private String longitude;
	private String product;
	private String upload_date;
	private String minimumRate;
	private String maximumRate;
	private String flag;
	
	//private String status;
	
	
	
	//private List<DsaDataComparison> flag;
	
//	private PricePegging pricePegging;
//	
//	private List<DsaExport> dsaExport;
		
}
