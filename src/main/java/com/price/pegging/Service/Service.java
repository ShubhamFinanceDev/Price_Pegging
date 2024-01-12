package com.price.pegging.Service;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface Service {

    public List<User> userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse      readDataDsa(MultipartFile file);

    CommonResponse peggingFileReadData(MultipartFile file);

    List<DsaExport> getAllExportData(String applicationNo,String uploadDate,String region,String zone);

    List<PricePegging> getAllPricePeggingDataByZoneAndRegion(String zone,String region);
    List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(String zone, String fromDate,String toDate,String region);
    List getAllZone();

    DashboardDistinctDetail getAllDashboarDetail();

    DashboardGraph countTotalByDate();


    FilterModel getAllFilterData();

    List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);
}
