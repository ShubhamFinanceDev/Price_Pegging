package com.price.pegging.Service;

import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

public interface Service {

    User userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse readDataDsa(MultipartFile file, String uploadBy);

    CommonResponse peggingFileReadData(MultipartFile file, String uploadBy);

    //    List<DsaExport> getAllExportData(String applicationNo, String region, String zone);
    DsaDataResponse getAllDsaData(Date fromDate, Date toDate, String applicationNo, String region, String zone, Integer pageNo, String pinCode, String flag);

    //    List<DsaExport> getAllExportDatatoDatetofromDate(Date fomDate, Date toDate, String applicationNo, String region, String zone);
    PricePeggingData getAllPricePeggingDataByZoneAndRegion(String zone, String region, int pageNo, String pinCode, String area);

    PricePeggingData getAllPricePeggingDataByZonFromDateToRegion(String zone, Date fromDate, Date toDate, String region, int pageNo, String pinCode, String area);  //change dataType toDate and fromDate

    List<String> getAllZone();

    DashboardDistinctDetail getAllDashboarDetail();

    DashboardGraph graphCount();


    FilterModel getAllFilterData();

    List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);

    List<DsaExportData> getDataByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone);

    CommonResponse saveuser(User userData);

    List<DsaDataModel> readData();

    CommonResponse generateReport(List<DsaDataModel> dsaDataModelList, String type, HttpServletResponse response);
}
