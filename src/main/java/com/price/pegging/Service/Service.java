package com.price.pegging.Service;

import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

public interface Service {

    public User userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse   readDataDsa(MultipartFile file);

    CommonResponse peggingFileReadData(MultipartFile file);

//    List<DsaExport> getAllExportData(String applicationNo, String region, String zone);
    DsaDataResponse getAllDsaData(Date fromDate, Date toDate, String applicationNo, String region, String zone,Integer pageNo);
//    List<DsaExport> getAllExportDatatoDatetofromDate(Date fomDate, Date toDate, String applicationNo, String region, String zone);
    PricePeggingData getAllPricePeggingDataByZoneAndRegion(String zone,String region,int pageNo);
    PricePeggingData getAllPricePeggingDataByZonFromDateToRegion(String zone, Date fromDate,Date toDate,String region, int pageNo);  //change dataType toDate and fromDate
    List getAllZone();

    DashboardDistinctDetail getAllDashboarDetail();

    DashboardGraph countTotalByDate();


    FilterModel getAllFilterData();

    List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);


    // NOTE ... //This service implementation is made by shagun for getDataForMap controller....
    List<DsaExportData> getDataByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone);

    CommonResponse saveuser(User userData);
   List<DsaDataModel> readData();

    CommonResponse generateReport(List<DsaDataModel> dsaDataModelList, String type,HttpServletResponse response);
}
