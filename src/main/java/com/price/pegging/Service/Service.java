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

    List<PricePegging> getAllPricePeggingDataByZone(String zone);
    List<PricePegging> getAllPricePeggingDataByZonFromDateTo(String zone, String fromDate,String toDate);
    public List<PricePegging> getAllPricePeggingDataByFromToDate(String fromDate,String toDate);
    List<PricePegging> getAllPricePeggingDataByAll();
    List getAllZone();

    DashboardDistinctDetail getAllDashboarDetail();

    DashboardGraph countTotalByDate();


    FilterModel getAllFilterData();

    List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);
}
