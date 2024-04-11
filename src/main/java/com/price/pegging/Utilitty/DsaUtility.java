package com.price.pegging.Utilitty;

import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class DsaUtility {
    public String dsaQuery(Date fromDate, Date toDate, String applicationNo, String region, String zone, Integer pageNo, String pinCode, int offSetData, String flag) {

        return "SELECT *\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        b.*,  \n" +
                "        a.minimum_rate,\n" +
                "        a.maximum_rate,\n" +
                "        b.rate_per_sqft AS dsa_rate_per_sqft,  \n" +
                "        CASE \n" +
                "            WHEN b.rate_per_sqft BETWEEN a.minimum_rate AND a.maximum_rate THEN 'G' \n" +
                "            WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 10) / 100) AND (a.maximum_rate - (a.maximum_rate * 10) / 100) THEN 'R'\n" +
                "            WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 15) / 100) AND (a.maximum_rate - (a.maximum_rate * 15) / 100) THEN 'Y'\n" +
                "            ELSE 'B' \n" +
                "        END AS flag \n" +
                "    FROM \n" +
                "        price_pegging a \n" +
                "    INNER JOIN \n" +
                "        dsa_export b ON a.pincode = b.property_pincode \n" +
                "                     AND a.region = b.region \n" +
                "                     AND a.zone_dist = b.zone \n" +
                "                     AND a.location = b.location\n" +
                "    WHERE \n" +
                "        a.upload_date = (SELECT MAX(upload_date) FROM price_pegging)\n" +
                "and b.application_no=COALESCE(" + prepareVariableForQuery(applicationNo) + ", b.application_no)\n"  + "and b.property_pincode=COALESCE(" + prepareVariableForQuery(pinCode) + ", b.property_pincode)\n" + "and b.region = COALESCE(" + prepareVariableForQuery(region) + ",b.region)\n" + "and b.zone = COALESCE(" + prepareVariableForQuery(zone) +
                ",b.zone)\n" + "and b.disbursal_date between COALESCE(" + prepareVariableForQuery(fromDate) + ",b.disbursal_date) And COALESCE(" + prepareVariableForQuery(toDate) + ",b.disbursal_date)\n"+
                ") AS subquery\n" +

                "WHERE \n" +
                "flag =COALESCE("+ prepareVariableForQuery(flag)+",flag)\n" +
                "ORDER BY b.s_no LIMIT 100 OFFSET " + offSetData;
    }

    public String prepareVariableForQuery(String applicationNo) {
        return (applicationNo == null || applicationNo.isEmpty()) ? null : "'" + applicationNo + "'";
    }

    public String prepareVariableForQuery(Date applicationNo) {
        return (applicationNo == null) ? null : "'" + applicationNo + "'";
    }


    public String totalCount(Date fromDate, Date toDate, String applicationNo, String region, String zone, Integer pageNo, String pinCode,String flag) {

        return "SELECT COUNT(*) FROM ( " + "SELECT b.*, CASE WHEN b.rate_per_sqft BETWEEN a.minimum_rate AND a.maximum_rate THEN 'G' " + "WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 10) / 100) AND (a.maximum_rate - (a.maximum_rate * 10) / 100) THEN 'R' " + "WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 15) / 100) AND (a.maximum_rate - (a.maximum_rate * 15) / 100) THEN 'Y' " +
                "ELSE 'B' END AS flag FROM price_pegging a INNER JOIN dsa_export b ON a.pincode = b.property_pincode AND a.region = b.region AND a.zone_dist = b.zone AND a.location = b.location " + "WHERE a.upload_date = (SELECT MAX(upload_date) FROM price_pegging) " + "and b.application_no=COALESCE(" + prepareVariableForQuery(applicationNo) + ", b.application_no) " + "and b.property_pincode=COALESCE(" + prepareVariableForQuery(pinCode) + ", b.property_pincode)\n" +
                "and b.region = COALESCE(" + prepareVariableForQuery(region) + ",b.region) " + "and b.zone = COALESCE(" + prepareVariableForQuery(zone) + ",b.zone) " + "and b.disbursal_date between COALESCE(" + prepareVariableForQuery(fromDate) + ",b.disbursal_date) And COALESCE(" + prepareVariableForQuery(toDate) + ",b.disbursal_date) " + ") AS subquery where flag =COALESCE("+ prepareVariableForQuery(flag)+",flag)";
    }

    public String CountDsaQuery() {

        return "SELECT  COUNT(DISTINCT property_pincode) AS distinctCountPincode,COUNT(DISTINCT zone) AS distinctCountZone,COUNT(DISTINCT location) AS distinctCountLocations,COUNT(DISTINCT region) AS distinctCountRegion, COUNT(DISTINCT upload_date) As distinctCountUploadDate from dsa_export";
    }

    public String dsaDateFormat() {

        return " SELECT date Date, total , updatedDate \n"+
                " FROM ( \n"+
                "  SELECT  DATE_FORMAT(upload_date,'%b-%Y') AS date, COUNT(DISTINCT property_pincode) AS total , CONCAT(YEAR(upload_date),'-',LPAD(MONTH(upload_date), 2, '0')) AS updatedDate\n"+
                "       FROM dsa_export\n"+
                "        GROUP BY date,updatedDate\n"+
                " ) AS subquery\n"+
                " ORDER BY updatedDate\n";
    }

    public String dsaDateFormat1() {

        return " SELECT date Date, total , updatedDate \n"+
                " FROM ( \n"+
                "  SELECT  DATE_FORMAT(upload_date,'%b-%Y') AS date, COUNT(DISTINCT location) AS total , CONCAT(YEAR(upload_date),'-',LPAD(MONTH(upload_date), 2, '0')) AS updatedDate\n"+
                "       FROM dsa_export\n"+
                "        GROUP BY date,updatedDate\n"+
                " ) AS subquery\n"+
                " ORDER BY updatedDate\n";
    }
    public String dsaReport() {

        return "SELECT b.*,a.minimum_rate,a.maximum_rate, CASE WHEN b.rate_per_sqft BETWEEN a.minimum_rate AND a.maximum_rate THEN 'G' \n" +
                "WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 10) / 100) AND (a.maximum_rate - (a.maximum_rate * 10) / 100) THEN 'R'\n" +
                "WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 15) / 100) AND (a.maximum_rate - (a.maximum_rate * 15) / 100) THEN 'Y'\n" +
                "        ELSE 'B' END AS flag FROM price_pegging a INNER JOIN dsa_export b ON a.pincode = b.property_pincode AND a.region = b.region AND a.zone_dist = b.zone AND a.location = b.location\n" +
                "WHERE a.upload_date = (SELECT MAX(upload_date) FROM price_pegging)\n";
    }
}
