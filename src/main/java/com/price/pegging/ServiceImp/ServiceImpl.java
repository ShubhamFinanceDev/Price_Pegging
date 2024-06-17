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
import com.price.pegging.Utilitty.DsaUtility;
import com.price.pegging.Utilitty.PricePeggingUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
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
    private DsaUtility dsaUtility;
    @Autowired
    private PricePeggingUtility pricePeggingUtility;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Override
    public User userExist(String userEmail) {
        return userRepository.findUser(userEmail).orElseThrow(RuntimeException::new);
    }

    @Override
    public UserDetail passwordMatch(String userPassword, User userDetails) {
        UserDetail userDetail = new UserDetail();
        if (passwordEncoder.matches(userPassword, userDetails.getPassword())) {
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
    public CommonResponse readDataDsa(MultipartFile file, String uploadBy) {

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
            boolean fileFormat = true;
            Row headerRow = rowIterator.next();

            fileFormat = fileUtilittyValidation.dsaFileFormat(headerRow);

            if (fileFormat) {

                System.out.println("file format matched");

                while (rowIterator.hasNext()) {
                    count++;
                    Row row = rowIterator.next();
                    DsaExport dsaExport = new DsaExport();
                    String applicationNo = null;

                    for (int i = 0; i < 13; i++) {
                        Cell cell = row.getCell(i);

                        errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";

                        if (errorMsg.isEmpty()) {
//                            System.out.println("value=" + row.getRowNum());

                            switch (i) {

                                case 1:
                                    applicationNo = row.getCell(1).toString();                                          //check application no already exist or not: 3307
                                    int countApplicationNo = dsaExportRepository.checkApplicationNo(applicationNo);
                                    if (countApplicationNo > 0) {
                                        errorMsg = "Application number " + applicationNo + " already exist.";
                                        System.out.println("error: application number already exist");

                                    }
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
                        if (!errorMsg.isEmpty()) break;
                    }
                    if (!errorMsg.isEmpty()) break;
                    dsaExport.setUploadBy(uploadBy);
                    dsaExports.add(dsaExport);
                    System.out.println("dsa size="+dsaExports.size());

                }

            } else {
                //   System.out.println("file format is not matched");
                errorMsg = "file format is not matching or technical issue.";
                logger.error("file format is not matching or technical issue :" + uploadBy);
            }

            System.out.println(errorMsg);
            //System.out.println(count);
        } catch (Exception e) {
            System.out.println(e);
            errorMsg = "file is empty or technical issue.";
            logger.error("file is empty or technical issue or Exception :" + uploadBy, e);
        }

        if (errorMsg.isEmpty() && count > 0) {
            dsaExportRepository.saveAll(dsaExports);
            commonResponse.setCode("0000");
            commonResponse.setMsg("file uploaded successfully " + dsaExports.size() + " row uploaded.");
            logger.info("file uploaded successfully " + dsaExports.size() + " row uploaded :" + uploadBy);
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg = "file is empty or technical issue";
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
                logger.error(errorMsg, uploadBy);
            } else {
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            }
        }

        return commonResponse;
    }

    @Override
    public CommonResponse peggingFileReadData(MultipartFile file, String uploadBy) {
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
            boolean fileFormat = true;
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

                        if (!errorMsg.isEmpty()) break;
                    }
                    if (!errorMsg.isEmpty()) break;
                    pricePeggingUpload.setUploadBy(uploadBy);
                    peggingUploads.add(pricePeggingUpload);

                }
            } else {
                System.out.println("file format is not matched");
                errorMsg = "file format is not matching or technical issue.";
                logger.error(errorMsg, uploadBy);
            }

            System.out.println(errorMsg);
            System.out.println(count);
        } catch (Exception e) {
            System.out.println(e);
            errorMsg = "file is empty or technical issue :";
            logger.error(errorMsg, uploadBy, e);
        }

        if (errorMsg.isEmpty() && count > 0) {
            pricePeggingRepository.saveAll(peggingUploads);
            commonResponse.setCode("0000");
            commonResponse.setMsg("file uploaded successfully " + peggingUploads.size() + " row uploaded.");
            logger.info("file uploaded successfully " + peggingUploads.size() + " row uploaded." + uploadBy);
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg = "file is empty or technical issue";
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
                logger.error(errorMsg, uploadBy);
            } else {
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
                logger.error(errorMsg, uploadBy);
            }
        }

        return commonResponse;
    }



    public DsaDataResponse getAllDsaData(Date fromDate, Date toDate, String applicationNo, String region, String zone, Integer pageNo, String pinCode, String flag) {
        DsaDataResponse dsaDataResponse = new DsaDataResponse();

        int offSetData = (pageNo - 1) * 100;
        int pageSize = 100;

        String dsaQuery = dsaUtility.dsaQuery(fromDate, toDate, applicationNo, region, zone, pageNo, pinCode, offSetData, flag);
        String totalCount = dsaUtility.totalCount(fromDate, toDate, applicationNo, region, zone, pageNo, pinCode,flag);
        try {
            Long totalCountResult = jdbcTemplate.queryForObject(totalCount, Long.class);

            List<DsaDataModel> dsaDataModelList = jdbcTemplate.query(dsaQuery, new BeanPropertyRowMapper<>(DsaDataModel.class));

            dsaDataResponse.setDsaExportList(dsaDataModelList);
            setDataInDsaObject(pageNo, pageSize, dsaDataModelList, dsaDataResponse, totalCountResult);

        } catch (Exception e) {
            System.out.println(e);
            dsaDataResponse.setCode("1111");
            dsaDataResponse.setMsg("Technical Error.");
        }
        return dsaDataResponse;
    }

    private void setDataInDsaObject(Integer pageNo, int pageSize, List<DsaDataModel> dsaDataModelList, DsaDataResponse dsaDataResponse, Long totalCountResult) {
        if (!(dsaDataModelList.isEmpty())) {
            dsaDataResponse.setMsg("Data found successfully");
            dsaDataResponse.setCode("0000");
            dsaDataResponse.setTotalCount(totalCountResult);
            dsaDataResponse.setNextPage(pageNo <= (totalCountResult / pageSize));
            dsaDataResponse.setDsaExportList(dsaDataModelList);
        } else {
            dsaDataResponse.setMsg("Data not found");
            dsaDataResponse.setCode("1111");
        }
    }

    /**
     * @param zone
     * @param pinCode
     * @return
     */
    @Override
    public PricePeggingData getAllPricePeggingDataByZoneAndRegion(String zone, String region, int pageNo, String pinCode, String area) {

        int pageSize = 100;
        List<PricePegging> pricePeggings = new ArrayList<>();
        PricePeggingData pricePeggingData = new PricePeggingData();

        try {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize); //ticket no.3304
            pricePeggings = pricePeggingRepository.findByZoneAndRegion(zone, region, pageable, pinCode, area);
            long totalCount = pricePeggingRepository.findByZoneAndRegion(zone, region, pinCode, area);
            setDataInObject(pageNo, pageSize, pricePeggings, pricePeggingData, totalCount);
        } catch (Exception e) {
            pricePeggingData.setMsg("Technical error");
            pricePeggingData.setCode("1111");
        }
        return pricePeggingData;
    }

    private void setDataInObject(int pageNo, int pageSize, List<PricePegging> pricePeggings, PricePeggingData pricePeggingData, long totalCount) {
        if (!(pricePeggings.isEmpty())) {
            pricePeggingData.setMsg("Data found successfully");
            pricePeggingData.setCode("0000");
            pricePeggingData.setTotalCount(totalCount);
            pricePeggingData.setNextPage(pageNo <= (totalCount / pageSize));
            pricePeggingData.setPricePeggingList(pricePeggings);

        } else {
            pricePeggingData.setMsg("Data not found");
            pricePeggingData.setCode("1111");
        }
    }


    public PricePeggingData getAllPricePeggingDataByZonFromDateToRegion(String zone, Date fromDate, Date toDate, String region, int pageNo, String pinCode, String area) {
        int pageSize = 100;
        List<PricePegging> pricePeggings = new ArrayList<>();
        PricePeggingData pricePeggingData = new PricePeggingData();

        try {
            Pageable pageable = PageRequest.of(pageNo - 1, 100); //ticket no.3304
            pricePeggings = pricePeggingRepository.findByZoneAndFromDateToRegion(zone, fromDate, toDate, region, pageable, pinCode, area);
            long totalCount = pricePeggingRepository.findByZoneAndFromDateToRegion(zone, fromDate, toDate, region, pinCode, area);
            setDataInObject(pageNo, pageSize, pricePeggings, pricePeggingData, totalCount);

        } catch (Exception e) {
            pricePeggingData.setMsg("Technical error");
            pricePeggingData.setCode("1111");
        }
        return pricePeggingData;
    }


    /**
     * @return
     */
    @Override
    public List<String> getAllZone() {
        List<String> zones;
        zones = pricePeggingRepository.getUniqueZones();
        return zones;
    }

    @Override
    public DashboardDistinctDetail getAllDashboarDetail() {
        DashboardDistinctDetail dashboardDistinctDetail = new DashboardDistinctDetail();
        DashboardDistinctDetail.DsaData dashboardDsa = new DashboardDistinctDetail.DsaData();
        DashboardDistinctDetail.PeggingData peggingData = new DashboardDistinctDetail.PeggingData();

        try {
            String peggingQuery = pricePeggingUtility.CountPeggingQuery();
            String dsaQuery = dsaUtility.CountDsaQuery();
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


    public static class MyRowMapperPegging implements RowMapper<DashboardDistinctDetail.PeggingData> {
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


    public static class MyRowMapperDsa implements RowMapper<DashboardDistinctDetail.DsaData> {
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
    public DashboardGraph graphCount() {

        DashboardGraph dashboardGraph = new DashboardGraph();
        DashboardGraph.DsaData dsaData = new DashboardGraph.DsaData();
        DashboardGraph.PeggingData peggingData = new DashboardGraph.PeggingData();

        String dsaPincode = dsaUtility.dsaDateFormat();
        String peggingPincode = pricePeggingUtility.peggingDateFormat();
        String dsaLocation = dsaUtility.dsaDateFormat1();
        String peggingLocation = pricePeggingUtility.peggingDateFormat1();

        try {

            List<DashboardGraph.Pincode> pincodesDsa = jdbcTemplate.query(dsaPincode, new BeanPropertyRowMapper<>(DashboardGraph.Pincode.class));
            List<DashboardGraph.Pincode> pincodesPegging = jdbcTemplate.query(peggingPincode, new BeanPropertyRowMapper<>(DashboardGraph.Pincode.class));

            List<DashboardGraph.Location> locationsDsa = jdbcTemplate.query(dsaLocation, new BeanPropertyRowMapper<>(DashboardGraph.Location.class));
            List<DashboardGraph.Location> locationsPeeging = jdbcTemplate.query(peggingLocation, new BeanPropertyRowMapper<>(DashboardGraph.Location.class));

            dsaData.setPincode(pincodesDsa);
            dsaData.setLocation(locationsDsa);
            peggingData.setPincode(pincodesPegging);
            peggingData.setLocation(locationsPeeging);
            dashboardGraph.setDsaData(dsaData);
            dashboardGraph.setPeggingData(peggingData);
            dashboardGraph.setMsg("Data found successfully.");
            dashboardGraph.setCode("0000");

        } catch (Exception e) {
            System.out.println(e);
            dashboardGraph.setMsg("Technical error.");
            dashboardGraph.setCode("1111");
        }

        return dashboardGraph;
    }

    @Override
    public FilterModel getAllFilterData() {

        FilterModel filterModel = new FilterModel();
        FilterModel.Dsa dsa = new FilterModel.Dsa();
        FilterModel.Pegging pegging = new FilterModel.Pegging();
        try {
            List<FilterModel.ZoneDis> zoneListPegging = pricePeggingRepository.getAllDistinctZone();
            pegging.setZoneDis(zoneListPegging);
            List<FilterModel.Region> regionListpegging = pricePeggingRepository.getAllDistinctRegion();
            pegging.setRegion(regionListpegging);

            List<FilterModel.ZoneDis> zoneListDsa = dsaExportRepository.getAllDistinctZone();
            dsa.setZoneDis(zoneListDsa);
            List<FilterModel.Region> regionListDsa = dsaExportRepository.getAllDistinctRegion();
            dsa.setRegion(regionListDsa);

            filterModel.setMsg("Data found successfully.");
            filterModel.setCode("0000");
            filterModel.setDsa(dsa);
            filterModel.setPegging(pegging);

        } catch (Exception e) {
            System.out.println(e);
            filterModel.setMsg("Technical error.");
            filterModel.setCode("1111");

        }


        return filterModel;
    }

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
                List<Object[]> pricePeggings = pricePeggingRepository.findDataByZoneLocation(zone, location);

                for (Object[] data : pricePeggings) {
                    PricePeggingLineChart pricePeggingLineChart = new PricePeggingLineChart(data[1].toString(), data[2].toString(), data[3].toString(), data[0].toString());
                    pricePeggingLineCharts.add(pricePeggingLineChart);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return pricePeggingLineCharts;
    }



    @Override
    public List<DsaExportData> getDataByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone) {
        return dsaExportRepository.findByPropertyPinCodeRegionZoneLocation(propertyPincode, region, zone);
    }

    @Override
    public CommonResponse saveuser(User userData) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Optional<User> emailExist = userRepository.findUser(userData.getEmail());     // Changes for check email exist or not

            if (emailExist.isEmpty()) {
                userData.setPassword(passwordEncoder.encode(userData.getPassword()));
                for (UserRole data : userData.getUserRoles()) {
                    data.setUser(userData);
                }
                userRepository.save(userData);
                commonResponse.setCode("0000");
                commonResponse.setMsg("User added successfully");

            } else {                                                     //End
                commonResponse.setMsg("User already exist");
                commonResponse.setCode("1111");
            }
        } catch (Exception e) {
            commonResponse.setCode("1111");
            commonResponse.setMsg("Technical error.");
        }
        return commonResponse;
    }

    @Override
    public List<DsaDataModel> readData() {
        List<DsaDataModel> dsaDataModelList = new ArrayList<>();
        String dsaQuery = dsaUtility.dsaReport();
        try {

            dsaDataModelList = jdbcTemplate.query(dsaQuery, new BeanPropertyRowMapper<>(DsaDataModel.class));

            System.out.print(dsaDataModelList.size());
        } catch (Exception e) {
            System.out.println(e);
        }
        return dsaDataModelList;
    }


    @Override
    public CommonResponse generateReport(List<DsaDataModel> dsaDataModel, String type, HttpServletResponse response) {
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
        headerName.add("maximum_rate");        // Ticket 3301 changes Done
        headerName.add("minimum_rate");       // Ticket 3301 changes Done

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
            row.createCell(5).setCellValue(dataList.getMaximum_rate());         // Ticket 3301 changes Done
            row.createCell(6).setCellValue(dataList.getMinimum_rate());         // Ticket 3301 changes Done

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
            commonResponse.setMsg("" + e);
            return commonResponse;
        }
    }




}






