package com.price.pegging.ServiceImp;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Entity.UserRole;
import com.price.pegging.FileUtilittyValidation;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import com.price.pegging.Repository.DsaExportRepository;
import com.price.pegging.Repository.PricePeggingRepository;
import com.price.pegging.Repository.UserRepository;
import com.price.pegging.Service.Service;
import com.price.pegging.Utilitty.DateFormatUtility;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipFile;

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
    @Autowired
    private DateFormatUtility dateFormatUtilty;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public User userExist(String userEmail) {


        return userRepository.findUser(userEmail);
    }

    @Override
    public UserDetail passwordMatch(String userPassword, User userDetails) {


        UserDetail userDetail = new UserDetail();
        //  System.out.println(savePassword);

        if (passwordEncoder.matches(userPassword,userDetails.getPassword())) {
            System.out.println("password correct");
            userDetail.setCode("0000");
            userDetail.setMsg("Login successfully");
            userDetail.setUserId(userDetails.getUserId());
            userDetail.setEmail(userDetails.getEmail());
            userDetail.setName(userDetails.getName());
        } else {
            System.out.println("Incorrect password");
            userDetail.setCode("1111");
            userDetail.setMsg("Password did not matched");
        }


        return userDetail;
    }

    @Override
    public CommonResponse readDataDsa(MultipartFile file) {

        List<DsaExport> dsaExports = new ArrayList<>();
        String errorMsg = "";
        CommonResponse commonResponse = new CommonResponse();
        int count = 0;

        try {

            InputStream inputStream = file.getInputStream();
            ZipSecureFile.setMinInflateRatio(0);                //for zip bomb detected
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
                            System.out.println("value=" + row.getRowNum());

                            switch (i) {

                                case 1:
                                    dsaExport.setApplicationNo(row.getCell(1).toString());
                                    break;
                                case 2:
                                    dsaExport.setProduct(row.getCell(2).toString());
                                    break;
                                case 3:
                                    dsaExport.setDisbursalDate(Date.valueOf(dateFormatUtilty.changeDateFormate((row.getCell(3).toString()))));
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
            ZipSecureFile.setMinInflateRatio(0);                //for zip bomb detected
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

                    for (int i = 0; i < 10; i++) {

                        Cell cell = row.getCell(i);

                        errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";

                        if (errorMsg.isEmpty()) {
                            switch (i) {
//                                    case 0: pricePeggingUpload.setsNo(Long.valueOf(row.getCell(0).toString()));//            System.out.println(Long.valueOf(row.getCell(0).toString()));
//                                   break;
                                case 1:
                                    pricePeggingUpload.setZone(row.getCell(1).toString());
                                    break;
                                case 2:
                                    pricePeggingUpload.setRegion(row.getCell(2).toString());   //Add logic of region column
                                    break;
                                case 3:
                                    pricePeggingUpload.setZoneDist(row.getCell(3).toString());
                                    break;
                                case 4:
                                    pricePeggingUpload.setLocations(row.getCell(4).toString());
                                    break;
                                case 5:
                                    pricePeggingUpload.setMinimumRate(row.getCell(5).toString());
                                    break;

                                case 6:
                                    pricePeggingUpload.setAverageRate(row.getCell(6).toString());
                                    break;
                                case 7:
                                    pricePeggingUpload.setMaximumRate(row.getCell(7).toString());
                                    break;
                                case 8:
                                    pricePeggingUpload.setPinCode(row.getCell(8).toString().replace(".0", ""));
                                    break;
                                case 9:
                                    pricePeggingUpload.setUploadDate(Date.valueOf(dateFormatUtilty.changeDateFormate(row.getCell(9).toString())));
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

//    @Override
//    public List<DsaExport> getAllExportData(String applicationNo, String region, String zone) {
//        List<DsaExport> exportsData = new ArrayList<>();
//        Pageable pageable = PageRequest.of(0, 100);
//
//        exportsData = dsaExportRepository.findByAll(applicationNo, region, zone, pageable);
//        return exportsData;
//    }
//
//
//    public List<DsaExport> getAllExportDatatoDatetofromDate(Date fromDate, Date toDate, String applicationNo, String region, String zone) {
//        List<DsaExport> exportsDatafromDateTotoDate = new ArrayList<>();
//        exportsDatafromDateTotoDate = dsaExportRepository.findByfromdateTotoDate(fromDate, toDate, applicationNo, region, zone);
//        return exportsDatafromDateTotoDate;
//    }
//

    public DsaDataResponse getAllDsaData(Date fromDate, Date toDate, String applicationNo, String region, String zone) {

        DsaDataResponse dsaDataResponse = new DsaDataResponse();
        List<DsaDataModel> dsaDataModelList = new ArrayList<>();

        String dsaQuery = "SELECT b.*, a.minimum_rate, a.maximum_rate, b.rate_per_sqft, a.maximum_rate, a.minimum_rate,\r\n"
        		+ "    CASE \r\n"
        		+ "        WHEN b.rate_per_sqft BETWEEN a.minimum_rate AND a.maximum_rate THEN 'G'\r\n"
        		+ "        WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 10) / 100) AND (a.maximum_rate - (a.maximum_rate * 10) / 100) THEN 'R'\r\n"
        		+ "        WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 15) / 100) AND (a.maximum_rate - (a.maximum_rate * 15) / 100) THEN 'Y'\r\n"
        		+ "        ELSE 'B'\r\n"
        		+ "    END AS flag FROM price_pegging a JOIN dsa_export b ON a.pincode = b.property_pincode AND a.region = b.region AND a.zone_dist = b.zone  AND a.location = b.location\r\n"
        		+ "WHERE a.upload_date = (SELECT MAX(upload_date) FROM price_pegging WHERE pincode = a.pincode AND region = a.region AND zone_dist = a.zone_dist AND location = a.location\r\n"
        		+ "    )\r\n"
        		+ "    AND b.application_no = COALESCE(NULL, b.application_no)\r\n"
        		+ "    AND b.region = COALESCE(NULL, b.region)\r\n"
        		+ "    AND b.zone = COALESCE(NULL, b.zone)\r\n"
        		+ "    AND b.disbursal_date BETWEEN COALESCE(NULL, b.disbursal_date) AND COALESCE(NULL, b.disbursal_date);\r\n"
        		+ "";
        try {


            dsaDataModelList = jdbcTemplate.query(dsaQuery, new BeanPropertyRowMapper<>(DsaDataModel.class));
            dsaDataResponse.setDsaExportList(dsaDataModelList);

        } catch (Exception e) {
            System.out.println(e);
            dsaDataResponse.setCode("1111");
            dsaDataResponse.setMsg("error:" + e);
        }
        return dsaDataResponse;
    }

    public String prepareVariableForQuery(String applicationNo) {
        return (applicationNo == null) ? null : "'" + applicationNo + "'";
    }

    public String prepareVariableForQuery(Date applicationNo) {
        return (applicationNo == null) ? null : "'" + applicationNo + "'";
    }

    /**
     * @param zone
     * @return
     */
    @Override
    public List<PricePegging> getAllPricePeggingDataByZoneAndRegion(String zone, String region) {
        List<PricePegging> pricePeggings = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        pricePeggings = pricePeggingRepository.findByZoneAndRegion(zone, region, pageable);
        return pricePeggings;
    }

    public List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(String zone, Date fromDate, Date toDate, String region) {
        List<PricePegging> pricePeggings = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        pricePeggings = pricePeggingRepository.findByZoneAndFromDateToRegion(zone, fromDate, toDate, region, pageable);
        return pricePeggings;
    }


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


            String peggingQuery = " SELECT  COUNT(DISTINCT pincode) AS distinctCountPincode,COUNT(DISTINCT zone_dist) AS distinctCountZone,COUNT(DISTINCT location) AS distinctCountLocations, COUNT(DISTINCT upload_date) AS distinctCountUploadDate from price_pegging";
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

        FilterModel filterModel = new FilterModel();
        FilterModel.Dsa dsa = new FilterModel.Dsa();
        FilterModel.Pegging pegging = new FilterModel.Pegging();
        try {
            List<FilterModel.ZoneDis> zoneListPegging = new ArrayList<>();
            zoneListPegging = pricePeggingRepository.getAllDistinctZone();
            pegging.setZoneDis(zoneListPegging);
            List<FilterModel.Region> regionListpegging = new ArrayList<>();
            regionListpegging = pricePeggingRepository.getAllDistinctRegion();
            pegging.setRegion(regionListpegging);


            List<FilterModel.ZoneDis> zoneListDsa = new ArrayList<>();
            zoneListDsa = dsaExportRepository.getAllDistinctZone();
            dsa.setZoneDis(zoneListDsa);
            List<FilterModel.Region> regionListDsa = new ArrayList<>();
            regionListDsa = dsaExportRepository.getAllDistinctRegion();
            dsa.setRegion(regionListDsa);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (dsa == null && pegging == null) {
            filterModel.setMsg("Data is not found for Pegging.");
            filterModel.setCode("1111");
        } else {
            filterModel.setMsg("Data found successfully.");
            filterModel.setCode("0000");
        }
        filterModel.setDsa(dsa);
        filterModel.setPegging(pegging);


        return filterModel;
    }

    /**
     * @param zone
     * @param location
     * @return
     */
    @Override
    public List<PricePeggingLineChart> getDataByZoneLocation(String zone, String location) {
        List<Object[]> pricePeggings=new ArrayList<>();
        List<PricePeggingLineChart> pricePeggingLineCharts=new ArrayList<>();

        try {

            if (!(zone == null && location == null)) {
                    pricePeggings = pricePeggingRepository.findDataByZoneLocation(zone, location);

                for (Object[] data: pricePeggings) {
                    PricePeggingLineChart pricePeggingLineChart=new PricePeggingLineChart(data[1].toString(),data[2].toString(),data[3].toString(),data[0].toString());
                    pricePeggingLineCharts.add(pricePeggingLineChart);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return pricePeggingLineCharts;
    }


    // NOTE ... //This service implementation is made by shagun for getDataForMap controller....
    @Override
    public List<DsaExportData> getDataByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone) {
        List<DsaExportData> dsaExportData = new ArrayList<>();
        CommonDsaExportData commonDsaExportData = new CommonDsaExportData();

        dsaExportData = dsaExportRepository.findByPropertyPinCodeRegionZoneLocation(propertyPincode, region, zone);

        return dsaExportData;
    }
    @Override
    public CommonResponse saveuser(User userData) {
        CommonResponse commonResponse = new CommonResponse();
        User user = new User();


        try {
            userData.setPassword(passwordEncoder.encode(userData.getPassword()));
            for (UserRole data : userData.getUserRoles()) {

                data.setUser(userData);

            }
            userRepository.save(userData);
            commonResponse.setCode("0000");
            commonResponse.setMsg("data saved successfully");

        } catch (Exception e) {
            commonResponse.setCode("1111");
            commonResponse.setMsg("error"+e);
        }
        return commonResponse;
    }
    @Override
    public List<DsaDataModel> readData() {
        List<DsaDataModel> dsaDataModelList = new ArrayList<>();
        CommonResponse commonResponse=new CommonResponse();

        String dsaQuery = "SELECT b.*, a.minimum_rate, a.maximum_rate, b.rate_per_sqft, a.maximum_rate, a.minimum_rate,\r\n"
        		+ "    CASE \r\n"
        		+ "        WHEN b.rate_per_sqft BETWEEN a.minimum_rate AND a.maximum_rate THEN 'G'\r\n"
        		+ "        WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 10) / 100) AND (a.maximum_rate - (a.maximum_rate * 10) / 100) THEN 'R'\r\n"
        		+ "        WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 15) / 100) AND (a.maximum_rate - (a.maximum_rate * 15) / 100) THEN 'Y'\r\n"
        		+ "        ELSE 'B'\r\n"
        		+ "    END AS flag FROM price_pegging a JOIN dsa_export b ON a.pincode = b.property_pincode AND a.region = b.region AND a.zone_dist = b.zone  AND a.location = b.location\r\n"
        		+ "WHERE a.upload_date = (SELECT MAX(upload_date) FROM price_pegging WHERE pincode = a.pincode AND region = a.region AND zone_dist = a.zone_dist AND location = a.location\r\n"
        		+ "    )\r\n"
        		+ "    AND b.application_no = COALESCE(NULL, b.application_no)\r\n"
        		+ "    AND b.region = COALESCE(NULL, b.region)\r\n"
        		+ "    AND b.zone = COALESCE(NULL, b.zone)\r\n"
        		+ "    AND b.disbursal_date BETWEEN COALESCE(NULL, b.disbursal_date) AND COALESCE(NULL, b.disbursal_date);\r\n"
        		+ "";
     // System.out.print(dsaQuery);
        try {

            dsaDataModelList = jdbcTemplate.query(dsaQuery, new BeanPropertyRowMapper<>(DsaDataModel.class));

            System.out.print(dsaDataModelList.size());
        } catch (Exception e) {
            System.out.println(e);
        }
        return dsaDataModelList;
    }



@Override
    public CommonResponse generateReport(List<DsaDataModel> dsaDataModel, String type,HttpServletResponse response) {
        List<DsaDataModel> dsaDataModelList = new ArrayList<>();
        CommonResponse commonResponse = new CommonResponse();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        if (!type.equals("All")) {
            for (DsaDataModel data : dsaDataModel) {
                if (data.getFlag().equals(type)) {
                    dsaDataModelList.add(data);
                }
            }
        } else {
            dsaDataModelList = new ArrayList<>(dsaDataModel);


            System.out.println(dsaDataModelList.size());
        }

        List<String> headerName = new ArrayList<>();
        headerName.add("applicationNo");
        headerName.add("disbursal_date");
        headerName.add("property_address");
        headerName.add("location");
        headerName.add("rate_per_sqft");

        Row headerRow = sheet.createRow(0);
        int headerColNum = 0;
        for (String data : headerName) {
            Cell cell = headerRow.createCell(headerColNum);
            cell.setCellValue(data);
            headerColNum++;
        }


        int rowNum = 1;

        for (DsaDataModel dataList : dsaDataModelList) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(dataList.getApplicationNo());
            row.createCell(1).setCellValue(dataList.getDisbursal_date());
            row.createCell(2).setCellValue(dataList.getProperty_address());
            row.createCell(3).setCellValue(dataList.getLocation());
            row.createCell(4).setCellValue(dataList.getRate_per_sqft());

        }

        try {

           response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=dsa_data.xlsx");


            workbook.write(response.getOutputStream());
            workbook.close();
            commonResponse.setCode("0000");
            commonResponse.setMsg("Success");
            return commonResponse;

        } catch (IOException e) {

            e.printStackTrace();
            commonResponse.setCode("0000");
            commonResponse.setMsg(""+e);
            return commonResponse;
        }
    }
}



