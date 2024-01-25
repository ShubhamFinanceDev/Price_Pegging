package com.price.pegging.Service;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
public interface Service {

    public List<User> userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse   readDataDsa(MultipartFile file);

    CommonResponse peggingFileReadData(MultipartFile file);

    List<DsaExport> getAllExportData(String applicationNo, Date uploadDate, String region, String zone);
    List<DsaExport> getAllExportDatatoDatetofromDate(Date fomDate, Date toDate, String applicationNo, String region, String zone);

    List<PricePegging> getAllPricePeggingDataByZoneAndRegion(String zone,String region);
    List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(String zone, String fromDate,String toDate,String region);
    List getAllZone();

    DashboardDistinctDetail getAllDashboarDetail();

    DashboardGraph countTotalByDate();


    FilterModel getAllFilterData();

    List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);


    // NOTE ... //This service implementation is made by shagun for getDataForMap controller....
    List<DsaExportData> getDataByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone, String location);
}
