package com.price.pegging.Service;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;

import net.sf.jasperreports.engine.JRException;

import com.price.pegging.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface Service {

	public List<User> userExist(String userEmail);

	UserDetail passwordMatch(String userPassword, User userDetail);

	CommonResponse readDataDsa(MultipartFile file);

	CommonResponse peggingFileReadData(MultipartFile file);

	List<DsaExport> getAllExportData(Integer pageNumber, String applicationNo, String uploadDate, String region,
			String zone);

<<<<<<< Updated upstream
    List<PricePegging> getAllPricePeggingData(String zone,String uploadDate);

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
=======
	DashboardGraph countTotalByDate();

	FilterModel getAllFilterData();

	List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location);

	List<DsaDataComparison> rateRange(String location, String pinCode) throws FileNotFoundException, JRException;

	List<DsaDataComparison> checkStatus(String flag) throws FileNotFoundException, JRException;

	//List<DsaDataComparison> checkStatus() throws FileNotFoundException, JRException;
>>>>>>> Stashed changes
}
