package com.price.pegging.ServiceImp;

<<<<<<< Updated upstream
import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.FileUtilittyValidation;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import com.price.pegging.Repository.DsaExportRepository;
import com.price.pegging.Repository.PricePeggingRepository;
import com.price.pegging.Repository.UserRepository;
import com.price.pegging.Service.Service;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
=======
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import com.price.pegging.Utilitty.DateFormatUtility;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
>>>>>>> Stashed changes
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

<<<<<<< Updated upstream
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
=======
import com.price.pegging.FileUtilittyValidation;
import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Entity.User;
import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.DashboardDistinctDetail;
import com.price.pegging.Model.DashboardGraph;
import com.price.pegging.Model.DsaDataComparison;
import com.price.pegging.Model.DsaValues;
import com.price.pegging.Model.FilterModel;
import com.price.pegging.Model.PricePeggingLineChart;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Repository.DsaExportRepository;
import com.price.pegging.Repository.PricePeggingRepository;
import com.price.pegging.Repository.UserRepository;
import com.price.pegging.Service.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

>>>>>>> Stashed changes
@org.springframework.stereotype.Service

public class ServiceImpl implements Service {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DsaExportRepository dsaExportRepository;
    @Autowired
    private PricePeggingRepository pricePeggingRepository;
    @Autowired
    private FileUtilittyValidation fileUtilittyValidation;
<<<<<<< Updated upstream
=======
//    @Autowired
//    private DateFormatUtility dateFormatUtilty;
>>>>>>> Stashed changes

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> userExist(String userEmail) {


        return userRepository.findUser(userEmail);
    }

    @Override
    public UserDetail passwordMatch(String userPassword, User userDetail) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        UserDetail commonResponse = new UserDetail();
        //  System.out.println(savePassword);

        if (passwordEncoder.matches(userPassword, userDetail.getPassword())) {
            System.out.println("password correct");
            commonResponse.setCode("0000");
            commonResponse.setMsg("Login successfully");
            commonResponse.setUserId(userDetail.getUserId());
            commonResponse.setEmail(userDetail.getEmail());
            commonResponse.setName(userDetail.getName());
        } else {
            System.out.println("Incorrect password");
            commonResponse.setCode("1111");
            commonResponse.setMsg("Password did not matched");
        }


        return commonResponse;
    }

    @Override
    public CommonResponse readDataDsa(MultipartFile file) {

        List<DsaExport> dsaExports = new ArrayList<>();
        String errorMsg = "";
        CommonResponse commonResponse = new CommonResponse();
        int count = 0;

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Boolean fileFormat = true;
            Row headerRow = rowIterator.next();

            fileFormat = fileUtilittyValidation.dsaFileFormat(headerRow);

            if (fileFormat) {

                System.out.println("file format matched");

                while (rowIterator.hasNext()) {

                    count++;
                    Row row = rowIterator.next();
                    DsaExport dsaExport = new DsaExport();


                    for (int i = 0; i < 13; i++) {
                        Cell cell = row.getCell(i);

                        errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";

                        if (errorMsg.isEmpty()) {
                            System.out.println("value=" + cell.toString());

                            switch (i) {

                                case 1:
                                    dsaExport.setApplicationNo(row.getCell(1).toString());
                                    break;
                                case 2:
                                    dsaExport.setProduct(row.getCell(2).toString());
                                    break;
                                case 3:
                                    dsaExport.setDisbursalDate(row.getCell(3).toString());
                                    break;
                                case 4:
                                    dsaExport.setProperty_address(row.getCell(4).toString());
                                    break;
                                case 5:
                                    dsaExport.setPropertyPincode(row.getCell(5).toString().replace(".0", ""));
                                    break;
                                case 6:
                                    dsaExport.setRegion(row.getCell(6).toString());
                                    break;
                                case 7:
                                    dsaExport.setZone(row.getCell(7).toString());
                                    break;
                                case 8:
                                    dsaExport.setLocation(row.getCell(8).toString());
                                    break;
                                case 9:
                                    dsaExport.setRate_per_sqft(row.getCell(9).toString().replace(".0", ""));

                                    break;
                                case 10:
                                    dsaExport.setProperty_type(row.getCell(10).toString());
                                    break;
                                case 11:
                                    dsaExport.setLattitude(row.getCell(11).toString());
                                    break;
                                case 12:
                                    dsaExport.setLongitude(row.getCell(12).toString());
                                    break;
                            }


                        }
                        if (!errorMsg.isEmpty())
                            break;
                    }
                    if (!errorMsg.isEmpty())
                        break;
                    dsaExports.add(dsaExport);

                }

            } else {
                //   System.out.println("file format is not matched");
                errorMsg = "file format is not matching or technical issue.";
            }

            System.out.println(errorMsg);
            //System.out.println(count);
        } catch (Exception e) {
            System.out.println(e);
            errorMsg = "file is empty or technical issue.";
        }

        if (errorMsg.isEmpty() && count > 0) {
            dsaExportRepository.saveAll(dsaExports);
            commonResponse.setCode("0000");
            commonResponse.setMsg("file uploaded successfully " + dsaExports.size() + " row uploaded.");
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg = "file is empty or technical issue";
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            } else {
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            }
        }

        return commonResponse;
    }

    @Override
    public CommonResponse peggingFileReadData(MultipartFile file) {
        List<PricePegging> peggingUploads = new ArrayList<>();
        String errorMsg = "";
        CommonResponse commonResponse = new CommonResponse();
        int count = 0;

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            // rowIterator.next();
            Row headerRow = rowIterator.next();
            Boolean fileFormat = true;

            fileFormat = fileUtilittyValidation.pricePeggingFileFormat(headerRow);
            System.out.println("true/false " + fileFormat);
            if (fileFormat) {


                while (rowIterator.hasNext()) {

                    count++;
                    Row row = rowIterator.next();
                    PricePegging pricePeggingUpload = new PricePegging();

                    for (int i = 0; i < 6; i++) {

                        Cell cell = row.getCell(i);

                        errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";


                        if (errorMsg.isEmpty()) {
                            switch (i) {
                                //    case 0: pricePeggingUpload.setsNo(Long.valueOf(row.getCell(0).toString()));
                                //            System.out.println(Long.valueOf(row.getCell(0).toString()));
                                //   break;
                                case 0:
                                    pricePeggingUpload.setZone(row.getCell(0).toString());
                                    break;
                                case 1:
                                    pricePeggingUpload.setLocations(row.getCell(1).toString());
                                    break;
                                case 2:
                                    pricePeggingUpload.setMinimumRate(row.getCell(2).toString());
                                    break;
                                case 3:
                                    pricePeggingUpload.setMaximumRate(row.getCell(3).toString());
                                    break;
                                case 4:
                                    pricePeggingUpload.setAverageRate(row.getCell(4).toString());
                                    break;
                                case 5:
                                    pricePeggingUpload.setPinCode(row.getCell(5).toString().replace(".0", ""));
                                    break;
                            }
                        }

                        if (!errorMsg.isEmpty())
                            break;
                    }
                    if (!errorMsg.isEmpty())
                        break;
                    peggingUploads.add(pricePeggingUpload);

                }
            } else {
                System.out.println("file format is not matched");
                errorMsg = "file format is not matching or technical issue.";
            }

            System.out.println(errorMsg);
            System.out.println(count);
        } catch (Exception e) {
            System.out.println(e);
            errorMsg = "file is empty or technical issue";
        }

        if (errorMsg.isEmpty() && count > 0) {
            pricePeggingRepository.saveAll(peggingUploads);
            commonResponse.setCode("0000");
            commonResponse.setMsg("file uploaded successfully " + peggingUploads.size() + " row uploaded.");
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg = "file is empty or technical issue";
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            } else {
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            }
        }

        return commonResponse;
    }

    @Override
<<<<<<< Updated upstream
    public List<DsaExport> getAllExportData(String applicationNo, String uploadDate,String region,String zone) {
        List<DsaExport> exportsData = new ArrayList<>();


//        if (applicationNo != null && uploadDate != null) {
//            exportsData = dsaExportRepository.findByApllicationAndUpdatedDate(applicationNo, uploadDate);
//        } else if (applicationNo != null) {
//            exportsData = dsaExportRepository.findByApplicationNo(applicationNo);
//        } else if (uploadDate != null) {
//            exportsData = dsaExportRepository.findByUpdatedDate(uploadDate);
//        } else {
//            exportsData = dsaExportRepository.findAll();
//        }

       exportsData=dsaExportRepository.findByAll(applicationNo,uploadDate,region,zone);


=======
    public List<DsaExport> getAllExportData(Integer pageNumber,String applicationNo, String uploadDate,String region,String zone) {
        List<DsaExport> exportsData = new ArrayList<>();
        String disbursalDateNew=null;
        if(pageNumber == null) {
        	pageNumber = 0;
        }
        Pageable pageable = PageRequest.of(pageNumber, 100);
        
        
        if(uploadDate!=null) {
        	DateTimeFormatter inputDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	DateTimeFormatter outputDate = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        	
        	LocalDate localdate = LocalDate.parse(uploadDate, inputDate);
        	
        	String outPutdate = localdate.format(outputDate);
        	  exportsData=dsaExportRepository.findByAll(applicationNo,outPutdate,region,zone, pageable);
        }
        else
        {
        	  exportsData=dsaExportRepository.findByAll(applicationNo,uploadDate,region,zone, pageable);
        }
>>>>>>> Stashed changes
        return exportsData;
    }
    

    /**
     * @param zone
     * @return
     */
    @Override
<<<<<<< Updated upstream
    public List<PricePegging> getAllPricePeggingData(String zone, String uploadDate) {
        List<PricePegging> pricePeggings = new ArrayList<>();
=======
    public List<PricePegging> getAllPricePeggingDataByZoneAndRegion(Integer pageNumber,String zone,String region) {
        List<PricePegging> pricePeggings = new ArrayList<>();
        if(pageNumber == null) {
        	pageNumber = 0;
        }
        Pageable pageable = PageRequest.of(pageNumber, 100);
>>>>>>> Stashed changes


        if (zone != null && uploadDate != null) {
            pricePeggings = pricePeggingRepository.findByZoneAndUpdatedDate(zone, uploadDate);
        } else if (zone != null) {
            pricePeggings = pricePeggingRepository.findByZone(zone);
        } else if (uploadDate != null) {
            pricePeggings = pricePeggingRepository.findByUpdatedDate(uploadDate);
        } else {
            pricePeggings = pricePeggingRepository.findAll();
        }

        return pricePeggings;
    }

<<<<<<< Updated upstream
=======
    public List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(Integer pageNumber,String zone, String fromDate,String toDate,String region) {
        List<PricePegging> pricePeggings = new ArrayList<>();
        if(pageNumber == null) {
        	pageNumber = 0;
        }
        Pageable pageable = PageRequest.of(pageNumber, 100);

        pricePeggings = pricePeggingRepository.findByZoneAndFromDateToRegion(zone, fromDate,toDate,region,pageable);
        return pricePeggings;
    }


>>>>>>> Stashed changes
    /**
     * @return
     */
    @Override
    public List getAllZone() {
        List zones;
        zones = pricePeggingRepository.getUniqeZones();
        return zones;
    }

    @Override
    public DashboardDistinctDetail getAllDashboarDetail() {
        DashboardDistinctDetail dashboardDistinctDetail = new DashboardDistinctDetail();
        DashboardDistinctDetail.DsaData dashboardDsa = new DashboardDistinctDetail.DsaData();
        DashboardDistinctDetail.PeggingData peggingData = new DashboardDistinctDetail.PeggingData();

        try {


            String peggingQuery = " SELECT  COUNT(DISTINCT pincode) AS distinctCountPincode,COUNT(DISTINCT zone) AS distinctCountZone,COUNT(DISTINCT location) AS distinctCountLocations, COUNT(DISTINCT upload_date) AS distinctCountUploadDate from price_pegging";
            String dsaQuery = "SELECT  COUNT(DISTINCT property_pincode) AS distinctCountPincode,COUNT(DISTINCT zone) AS distinctCountZone,COUNT(DISTINCT location) AS distinctCountLocations,COUNT(DISTINCT region) AS distinctCountRegion, COUNT(DISTINCT upload_date) As distinctCountUploadDate from dsa_export";

            peggingData = jdbcTemplate.queryForObject(peggingQuery, new MyRowMapperPegging());
            dashboardDsa = jdbcTemplate.queryForObject(dsaQuery, new MyRowMapperDsa());

            if (dashboardDsa == null && peggingData == null) {
                dashboardDistinctDetail.setMsg("Data found successfully.");
                dashboardDistinctDetail.setCode("0000");
            } else {
                if (dashboardDsa == null) {
                    dashboardDistinctDetail.setMsg("Data not available  for Dsa.");
                    dashboardDistinctDetail.setCode("1111");
                } else {
                    if (peggingData == null) {
                        dashboardDistinctDetail.setMsg("Data is not found for Pegging.");
                        dashboardDistinctDetail.setCode("1111");
                    } else

                        dashboardDistinctDetail.setMsg("Data found successfully.");
                    dashboardDistinctDetail.setCode("0000");

                }

            }

            dashboardDistinctDetail.setPeggingData(peggingData);
            dashboardDistinctDetail.setDsaData(dashboardDsa);
        } catch (Exception e) {
            System.out.println(e);
        }

        return dashboardDistinctDetail;
    }


    public class MyRowMapperPegging implements RowMapper<DashboardDistinctDetail.PeggingData> {
        @Override
        public DashboardDistinctDetail.PeggingData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            DashboardDistinctDetail.PeggingData dashboardPegging = new DashboardDistinctDetail.PeggingData();
            dashboardPegging.setPeggingZoneTotal(resultSet.getString("distinctCountZone"));
            dashboardPegging.setPeggingPincodeTotal(resultSet.getString("distinctCountPincode"));
            dashboardPegging.setPeggingLocationsTotal(resultSet.getString("distinctCountLocations"));
            dashboardPegging.setPeggingQuartersTotal(resultSet.getString("distinctCountUploadDate"));

            return dashboardPegging;
        }
    }


    public class MyRowMapperDsa implements RowMapper<DashboardDistinctDetail.DsaData> {
        @Override
        public DashboardDistinctDetail.DsaData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            DashboardDistinctDetail.DsaData dashboardDsa = new DashboardDistinctDetail.DsaData();
            dashboardDsa.setDsaPincodeTotal(resultSet.getString("distinctCountPincode"));
            dashboardDsa.setDsaZoneTotal(resultSet.getString("distinctCountZone"));
            dashboardDsa.setDsaRegionTotal(resultSet.getString("distinctCountRegion"));
            dashboardDsa.setDsaLocationsTotal(resultSet.getString("distinctCountLocations"));
            dashboardDsa.setDsaQuartersTotal(resultSet.getString("distinctCountUploadDate"));

            return dashboardDsa;
        }
    }


    @Override
    public DashboardGraph countTotalByDate() {

        DashboardGraph dashboardGraph = new DashboardGraph();
        DashboardGraph.DsaData dsaData = new DashboardGraph.DsaData();
        DashboardGraph.PeggingData peggingData = new DashboardGraph.PeggingData();


        List<DashboardGraph.Pincode> pincodesDsa = new ArrayList<>();
        List<DashboardGraph.Location> locationsDsa = new ArrayList<>();
        List<DashboardGraph.Pincode> pincodesPegging = new ArrayList<>();
        List<DashboardGraph.Location> locationsPeeging = new ArrayList<>();

        List<DashboardGraph.Pincode> pincodes = new ArrayList<>();
        List<DashboardGraph.Location> locations = new ArrayList<>();


//        List<DashboardGraph.PeggingData> peggingData=new ArrayList<>();

        try {
            String dsaQuery = "SELECT date_format(upload_date,'%Y-%M') date,COUNT(property_pincode)total FROM dsa_export group BY date_format(upload_date,'%Y-%M')";
            String peggingQuery = "SELECT date_format(upload_date,'%Y-%M') Date,COUNT(pincode)total FROM price_pegging group BY date_format(upload_date,'%Y-%M')";

            String dsaQuery1 = "SELECT date_format(upload_date,'%Y-%M') date,COUNT(property_pincode)total FROM dsa_export group BY date_format(upload_date,'%Y-%M')";
            String peggingQuery1 = "SELECT date_format(upload_date,'%Y-%M') Date,COUNT(pincode)total FROM price_pegging group BY date_format(upload_date,'%Y-%M')";


            pincodesDsa = jdbcTemplate.query(dsaQuery, new BeanPropertyRowMapper<>(DashboardGraph.Pincode.class));
            pincodesPegging = jdbcTemplate.query(peggingQuery, new BeanPropertyRowMapper<>(DashboardGraph.Pincode.class));

            locationsDsa = jdbcTemplate.query(dsaQuery1, new BeanPropertyRowMapper<>(DashboardGraph.Location.class));
            locationsPeeging = jdbcTemplate.query(peggingQuery1, new BeanPropertyRowMapper<>(DashboardGraph.Location.class));
//    peggingData = jdbcTemplate.query(peggingQuery, new BeanPropertyRowMapper<>(DashboardGraph.PeggingData.class));
        } catch (Exception e) {
            System.out.println(e);
        }

        dsaData.setPincode(pincodesDsa);
        dsaData.setLocation(locationsDsa);
        peggingData.setPincode(pincodesPegging);
        peggingData.setLocation(locationsPeeging);

        if (dsaData == null && peggingData == null) {
            dashboardGraph.setMsg("Data is not found for Pegging.");
            dashboardGraph.setCode("1111");
        } else {
            dashboardGraph.setMsg("Data found successfully.");
            dashboardGraph.setCode("0000");
        }
        dashboardGraph.setDsaData(dsaData);
        dashboardGraph.setPeggingData(peggingData);

        return dashboardGraph;
    }

    @Override
    public FilterModel getAllFilterData() {

        FilterModel filterModel=new FilterModel();
        FilterModel.Dsa dsa=new FilterModel.Dsa();
        FilterModel.Pegging pegging=new FilterModel.Pegging();
try {
    List<FilterModel.Zone> zoneListPegging = new ArrayList<>();
    zoneListPegging = pricePeggingRepository.getAllDistictZone();
    pegging.setZone(zoneListPegging);

    List<FilterModel.Zone> zoneListDsa = new ArrayList<>();
    zoneListDsa = dsaExportRepository.getAllDistinctZone();
    dsa.setZone(zoneListDsa);
    List<FilterModel.Region> regionListDsa = new ArrayList<>();
    regionListDsa = dsaExportRepository.getAllDistinctRegion();
    dsa.setRegion(regionListDsa);
}
catch (Exception e)
{
    System.out.println(e);
}
        if(dsa==null && pegging== null)
        {
            filterModel.setMsg("Data is not found for Pegging.");
            filterModel.setCode("1111");
        }
        else
        {
            filterModel.setMsg("Data found successfully.");
            filterModel.setCode("0000");
        }
        filterModel.setDsa(dsa);
        filterModel.setPegging(pegging);



        return filterModel;
    }

<<<<<<< Updated upstream
=======
    /**
     * @param zone
     * @param location
     * @return
     */
    @Override
    public List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location) {
        List<PricePeggingLineChart> pricePeggingLineCharts = new ArrayList<>();
        try {


        if (!(zone == null && location == null)) {
            pricePeggingLineCharts = pricePeggingRepository.findDataByZoneLocation(zone, location);
        }
    }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return pricePeggingLineCharts;
    }

    @Override
    public List<DsaDataComparison> rateRange(String location, String pinCode) throws FileNotFoundException, JRException{
    	
    	List<DsaDataComparison> dsaComparison = new ArrayList<>();
    	String path = "D:\\new";
//    	status.setLocation(location);
//		status.setPincode(pinCode);
//		status.setStatus("red");
    	
//		DsaExport dsaExports = new DsaExport();
    	List<DsaExport> dsaExports  = new ArrayList<>();
		PricePegging pricePegging = new PricePegging();
	    dsaExports = dsaExportRepository.findByLocationAndPincode(location,pinCode);
		pricePegging = pricePeggingRepository.findByLocationAndpropertyPincode(location,pinCode);
		
		if(pricePegging != null && !(dsaExports.isEmpty())) {
		 int maximumRate = Integer.parseInt(pricePegging.getMaximumRate().replace(".0",""));
		 int minimumRate = Integer.parseInt(pricePegging.getMinimumRate().replace(".0",""));

		 int maximumper = maximumRate-((maximumRate*10)/100);
		 int minimumper = minimumRate-((minimumRate*10)/100);

		 int maximumperthird = maximumRate-((maximumRate*15)/100);
		 int minimumperthird = minimumRate-((minimumRate*15)/100);
		
//		status.setDsaExport(dsaExports);
//		status.setPricePegging(pricePegging);
		
//		int square_ft = Integer.parseInt(rate_per_sqft);
//		int minimum = Integer.parseInt(minimumRate);
//		int maximum = Integer.parseInt(maximumRate);
		   
		   for(DsaExport ds : dsaExports) {
			   
			   DsaDataComparison dsaData = new DsaDataComparison();
			   
			   int squareRate = Integer.parseInt(ds.getRate_per_sqft().replace(".",""));

		          if (squareRate <= maximumRate && squareRate >= minimumRate){
		              dsaData.setFlag("green");
		   } else if (squareRate <= maximumper && squareRate >= minimumper){
		              dsaData.setFlag("yellow");
		   } else if (squareRate <= maximumperthird && squareRate >= minimumperthird){
		              dsaData.setFlag("yellow");
		   } else {
		              dsaData.setFlag("Bad rate");
		   }
		          File file = ResourceUtils.getFile("classpath:jasperfolder\\dsa_pegging.jrxml");
		          JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		          JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dsaComparison);
		          Map<String, Object> parameters = new HashMap<>();
		          JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
//		          JRXlsxExporter.(jasperPrint, path+"\\GenerateReport.xlsx");
		          JRXlsxExporter exporter = new JRXlsxExporter();
		          exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		          exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path+ "\\GenerateReport.xlsx"));
		          exporter.exportReport();

		          dsaData.setLocation(ds.getLocation());
		          dsaData.setPropertyPincode(ds.getPropertyPincode());
		          dsaData.setApplicationNo(ds.getApplicationNo());
		          dsaData.setRate_per_sqft(ds.getRate_per_sqft());
		          dsaData.setMaximumRate(pricePegging.getMaximumRate());
		          dsaData.setMinimumRate(pricePegging.getMinimumRate());

		       dsaComparison.add(dsaData);
		   }
		
		}
		
    	return dsaComparison;
    	
    }

    @Override
    public List<DsaDataComparison> checkStatus(String flag) throws FileNotFoundException, JRException{
    	List<DsaDataComparison> dsaComparison = new ArrayList<>();
    	//DsaDataComparison dsaData = new DsaDataComparison();
		 List<DsaDataComparison> dsa = new ArrayList<>();
		 String path = "D:\\new";
		
	            String dsaQuery = "select  b.*,a.minimum_rate,a.maximum_rate,b.rate_per_sqft, case when b.rate_per_sqft between a.minimum_rate and a.maximum_rate   then \r\n"
	            		+ "'G'  when b.rate_per_sqft between (a.minimum_rate-(a.minimum_rate*10)/100) and (a.maximum_rate-(a.maximum_rate*10)/100) then 'R'\r\n"
	            		+ "when b.rate_per_sqft between (a.minimum_rate-(a.minimum_rate*15)/100) and (a.maximum_rate-(a.maximum_rate*15)/100) then 'Y'\r\n"
	            		+ "  else 'B' end  flag  from pricepegging.price_pegging  a, pricepegging.dsa_export b  where a.pincode = b.property_pincode  and a.region=b.region \r\n"
	            		+ "and a.zone_dist = b.zone  and a.location = b.location";	           
	            try {		
	            dsaComparison = jdbcTemplate.query(dsaQuery, new BeanPropertyRowMapper<>(DsaDataComparison.class));
	  
	            for(DsaDataComparison dsaList :dsaComparison) {
	   			 if(dsaList.getFlag().equals(flag)) {
	   				 dsa.add(dsaList);
	   			 }
	   		 }System.out.println(generateExcelSheet(dsa));
	        } catch (Exception e) {
	            System.out.println(e);
	        }
//	            System.out.println("value check for pricePegging ======== "+pricePegging);
//	            System.out.println("value check for dsaexport ====== "+dsaExports);
//	            System.out.println("loop check ======= "+(pricePegging != null && !(dsaExports.isEmpty())));
	       
	            
		
//		 System.out.println("value check ======== "+dsa);
////		 }
//		 System.out.println("before return value check ======== "+dsa);
    	return dsa;
    	
    }
    
    public String generateExcelSheet(List<DsaDataComparison> dsaData) throws IOException {
		
    	List<String> headerRow = new ArrayList<>();
    	headerRow.add("s_no");
    	headerRow.add("applicationNo");
    	headerRow.add("disbursal_date");
    	headerRow.add("property_address");
    	headerRow.add("propertyPincode");
    	headerRow.add("region");
    	headerRow.add("zone");
    	headerRow.add("location");
    	headerRow.add("rate_per_sqft");
    	headerRow.add("property_type");
    	headerRow.add("lattitude");
    	headerRow.add("longitude");
    	headerRow.add("product");
    	headerRow.add("upload_date");
    	headerRow.add("minimumRate");
    	headerRow.add("maximumRate");
    	headerRow.add("flag");
    	
    	Workbook workbook = new XSSFWorkbook();
    	Sheet sheet = workbook.createSheet("Sheet1");
    	
    	 Row row = sheet.createRow(0);
    	 
    	 int cellIndex=0;
    	 for(String head : headerRow) {
    		 Cell headerCell = row.createCell(cellIndex++); 
    		 headerCell.setCellValue(head);
    	 }
    	 
    	int rowNum=1;
    	for(DsaDataComparison dsc:dsaData) {
    		Row dataRow = sheet.createRow(rowNum++);
    		Cell dataCell = dataRow.createCell(0);
    		dataCell.setCellValue(dsc.getS_no());
    		dataCell = dataRow.createCell(1);
    		dataCell.setCellValue(dsc.getApplicationNo());
    		dataCell = dataRow.createCell(2);
    		dataCell.setCellValue(dsc.getDisbursal_date());
    		dataCell = dataRow.createCell(3);
    		dataCell.setCellValue(dsc.getProperty_address());
    		dataCell = dataRow.createCell(4);
    		dataCell.setCellValue(dsc.getPropertyPincode());
    	    dataCell = dataRow.createCell(5);
    		dataCell.setCellValue(dsc.getRegion());
    		dataCell = dataRow.createCell(6);
    		dataCell.setCellValue(dsc.getZone());
    		dataCell = dataRow.createCell(7);
    		dataCell.setCellValue(dsc.getLocation());
    		dataCell = dataRow.createCell(8);
    		dataCell.setCellValue(dsc.getRate_per_sqft());
    		dataCell = dataRow.createCell(9);
    		dataCell.setCellValue(dsc.getProperty_type());
    		dataCell = dataRow.createCell(10);
    		dataCell.setCellValue(dsc.getLattitude());
    		dataCell = dataRow.createCell(11);
    		dataCell.setCellValue(dsc.getLongitude());
    		dataCell = dataRow.createCell(12);
    		dataCell.setCellValue(dsc.getProduct());
    		dataCell = dataRow.createCell(13);
    		dataCell.setCellValue(dsc.getUpload_date());
    		dataCell = dataRow.createCell(14);
    		dataCell.setCellValue(dsc.getMinimumRate());
    		dataCell = dataRow.createCell(15);
    		dataCell.setCellValue(dsc.getMaximumRate());
    		dataCell = dataRow.createCell(16);
    		dataCell.setCellValue(dsc.getFlag());
    	}
    	
    	String filePath = "D:\\new\\generated_excel1.xlsx";
    	workbook.write(new java.io.FileOutputStream(filePath));
    	workbook.close();
    	return "file generated";
    	
    	
    }
>>>>>>> Stashed changes

}



