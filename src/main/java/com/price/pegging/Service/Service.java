package com.price.pegging.Service;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;

import net.sf.jasperreports.engine.JRException;

import com.price.pegging.Entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

<<<<<<< HEAD
import java.io.FileNotFoundException;
import java.util.List;

public interface Service {

	public List<User> userExist(String userEmail);
=======
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface Service {

    public User userExist(String userEmail);
>>>>>>> development

	UserDetail passwordMatch(String userPassword, User userDetail);

<<<<<<< HEAD
	CommonResponse readDataDsa(MultipartFile file);
=======
    CommonResponse   readDataDsa(MultipartFile file);
>>>>>>> development

	CommonResponse peggingFileReadData(MultipartFile file);

<<<<<<< HEAD
	List<DsaExport> getAllExportData(Integer pageNumber, String applicationNo, String uploadDate, String region,
			String zone);

<<<<<<< Updated upstream
    List<PricePegging> getAllPricePeggingData(String zone,String uploadDate);

=======
//    List<DsaExport> getAllExportData(String applicationNo, String region, String zone);
    DsaDataResponse getAllDsaData(Date fromDate, Date toDate, String applicationNo, String region, String zone);
//    List<DsaExport> getAllExportDatatoDatetofromDate(Date fomDate, Date toDate, String applicationNo, String region, String zone);
    List<PricePegging> getAllPricePeggingDataByZoneAndRegion(String zone,String region);
    List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(String zone, String fromDate,String toDate,String region);
>>>>>>> development
    List getAllZone();
=======
	List<PricePegging> getAllPricePeggingDataByZoneAndRegion(Integer pageNumber, String zone, String region);
>>>>>>> Stashed changes

	List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(Integer pageNumber, String zone, String fromDate,
			String toDate, String region);

	List getAllZone();

	DashboardDistinctDetail getAllDashboarDetail();

<<<<<<< Updated upstream
    FilterModel getAllFilterData();
<<<<<<< HEAD
=======
	DashboardGraph countTotalByDate();

	FilterModel getAllFilterData();

	List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);

	List<DsaDataComparison> rateRange(String location, String pinCode) throws FileNotFoundException, JRException;

	List<DsaDataComparison> checkStatus(String flag) throws FileNotFoundException, JRException;

	//List<DsaDataComparison> checkStatus() throws FileNotFoundException, JRException;
>>>>>>> Stashed changes
=======

    List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);


    // NOTE ... //This service implementation is made by shagun for getDataForMap controller....
    List<DsaExportData> getDataByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone);

    CommonResponse saveuser(User userData);
   List<DsaDataModel> readData();

    CommonResponse generateReport(List<DsaDataModel> dsaDataModelList, String type,HttpServletResponse response);
>>>>>>> development
}
