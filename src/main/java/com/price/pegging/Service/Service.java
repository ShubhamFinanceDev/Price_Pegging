package com.price.pegging.Service;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface Service {

    public User userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse   readDataDsa(MultipartFile file);

    CommonResponse peggingFileReadData(MultipartFile file);

//    List<DsaExport> getAllExportData(String applicationNo, String region, String zone);
    DsaDataResponse getAllDsaData(Date fromDate, Date toDate, String applicationNo, String region, String zone);
//    List<DsaExport> getAllExportDatatoDatetofromDate(Date fomDate, Date toDate, String applicationNo, String region, String zone);
    List<PricePegging> getAllPricePeggingDataByZoneAndRegion(String zone,String region);
    List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(String zone, Date fromDate,Date toDate,String region);  //change dataType toDate and fromDate
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
