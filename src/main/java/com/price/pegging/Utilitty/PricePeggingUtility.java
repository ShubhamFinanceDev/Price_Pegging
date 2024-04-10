package com.price.pegging.Utilitty;

import org.springframework.stereotype.Service;

@Service
public class PricePeggingUtility {

    public  String CountPeggingQuery() {

        return " SELECT  COUNT(DISTINCT pincode) AS distinctCountPincode,COUNT(DISTINCT zone_dist) AS distinctCountZone,COUNT(DISTINCT location) AS distinctCountLocations, COUNT(DISTINCT upload_date) AS distinctCountUploadDate from price_pegging";
    }

    public String peggingDateFormate1() {

        return " SELECT date Date, total , updatedDate \n"+
                " FROM ( \n"+
        "  SELECT  DATE_FORMAT(upload_date,'%b-%Y') AS date, COUNT(DISTINCT location) AS total , CONCAT(YEAR(upload_date),'-',LPAD(MONTH(upload_date), 2, '0')) AS updatedDate\n"+
        "       FROM price_pegging\n"+
        "        GROUP BY date,updatedDate\n"+
        " ) AS subquery\n"+
        " ORDER BY updatedDate\n";
    }
    public String peggingDateFormate() {

        return " SELECT date Date, total , updatedDate \n"+
                " FROM ( \n"+
                "  SELECT  DATE_FORMAT(upload_date,'%b-%Y') AS date, COUNT(DISTINCT pincode) AS total , CONCAT(YEAR(upload_date),'-',LPAD(MONTH(upload_date), 2, '0')) AS updatedDate\n"+
                "       FROM price_pegging\n"+
                "        GROUP BY date,updatedDate\n"+
                " ) AS subquery\n"+
                " ORDER BY updatedDate\n";
    }
}
