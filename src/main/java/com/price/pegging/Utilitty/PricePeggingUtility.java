package com.price.pegging.Utilitty;

import org.springframework.stereotype.Service;

@Service
public class PricePeggingUtility {

    public static String CountPeggingQuery() {
        String peggingQuery = " SELECT  COUNT(DISTINCT pincode) AS distinctCountPincode,COUNT(DISTINCT zone_dist) AS distinctCountZone,COUNT(DISTINCT location) AS distinctCountLocations, COUNT(DISTINCT upload_date) AS distinctCountUploadDate from price_pegging";

        return peggingQuery;
    }

    public String peggingDateFormat() {
        String peggingQuery = "SELECT date_format(upload_date,'%Y-%M') Date,COUNT(DISTINCT pincode)total FROM price_pegging group BY date_format(upload_date,'%Y-%M')";

        return peggingQuery;
    }

    public String peggingDateFormat1() {

        String peggingQuery1 = "SELECT date_format(upload_date,'%Y-%M') Date,COUNT(DISTINCT location)total FROM price_pegging group BY date_format(upload_date,'%Y-%M')";

        return peggingQuery1;
    }
}
