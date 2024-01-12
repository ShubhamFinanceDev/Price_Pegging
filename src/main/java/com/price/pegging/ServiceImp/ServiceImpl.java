package com.price.pegging.ServiceImp;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< Updated upstream
import org.springframework.boot.autoconfigure.batch.BatchProperties;
=======
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
>>>>>>> Stashed changes
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
<<<<<<< Updated upstream
=======

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
//             rowIterator.next();
            Row headerRow = rowIterator.next();
            Boolean fileFormat = true;
            fileFormat = fileUtilittyValidation.pricePeggingFileFormat(headerRow);
            System.out.println("true/false " + fileFormat);
            if (fileFormat) {

                while (rowIterator.hasNext()) {

                    count++;
                    Row row = rowIterator.next();
                    PricePegging pricePeggingUpload = new PricePegging();

                    for (int i = 0; i < 9; i++) {

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
       exportsData=dsaExportRepository.findByAll(applicationNo,uploadDate,region,zone);
=======
    public List<DsaExport> getAllExportData(String applicationNo, String disbursalDate, String region, String zone) {
        List<DsaExport> exportsData = new ArrayList<>();
        String disbursalDateNew = null;
        Pageable pageable = PageRequest.of(0, 100);

        if (!(disbursalDate == null)) {
            disbursalDateNew = dateFormatUtilty.changeDateFormate(disbursalDate);
        }
        exportsData = dsaExportRepository.findByAll(applicationNo, disbursalDateNew, region, zone, pageable);
>>>>>>> Stashed changes
        return exportsData;
    }

    @Override
    public List<DsaExport> getAllExportDataByZonFromDateToRegion(String applicationNo, String disbursalDate, String region, String fromDate, String zone, String toDate) {
        List<DsaExport> exportsData = new ArrayList<>();
        exportsData = dsaExportRepository.findByZoneAndFromdatetodate(applicationNo, disbursalDate, region, fromDate, zone, toDate);   // create method for fromDate to toDate
        return exportsData;

    }

    /**
     * @param zone
     * @returnget
     */
    @Override
    public List<PricePegging> getAllPricePeggingDataByZoneAndRegion(String zone, String region) {
        List<PricePegging> pricePeggings = new ArrayList<>();
<<<<<<< Updated upstream
        pricePeggings = pricePeggingRepository.findByZoneAndRegion(zone,region);
=======
        Pageable pageable = PageRequest.of(0, 100);

        pricePeggings = pricePeggingRepository.findByZoneAndRegion(zone, region, pageable);
>>>>>>> Stashed changes
        return pricePeggings;
    }

    public List<PricePegging> getAllPricePeggingDataByZonFromDateToRegion(String zone, String fromDate, String toDate, String region) {
        List<PricePegging> pricePeggings = new ArrayList<>();
<<<<<<< Updated upstream
        pricePeggings = pricePeggingRepository.findByZoneAndFromDateTo(zone, fromDate,toDate,region);
=======
        Pageable pageable = PageRequest.of(0, 100);

        pricePeggings = pricePeggingRepository.findByZoneAndFromDateToRegion(zone, fromDate, toDate, region, pageable);
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
        FilterModel filterModel=new FilterModel();
        FilterModel.Dsa dsa=new FilterModel.Dsa();
        FilterModel.Pegging pegging=new FilterModel.Pegging();
try {
    List<FilterModel.Zone> zoneListPegging = new ArrayList<>();
    zoneListPegging = pricePeggingRepository.getAllDistinctZone();
    pegging.setZone(zoneListPegging);
    List<FilterModel.Region> regionListpegging = new ArrayList<>();
    regionListpegging = pricePeggingRepository.getAllDistinctRegion();
    pegging.setRegion(regionListpegging);
=======
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
>>>>>>> Stashed changes


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
    public List<JasperReport> getDataByFlag(String flag) {
        List<JasperReport> jasperReport = new ArrayList<>();
        List<JasperReport> jsp = new ArrayList<>();


        String jasperquery = "SELECT b.*, a.minimum_rate, a.maximum_rate, b.rate_per_sqft, " +
                "CASE WHEN b.rate_per_sqft BETWEEN a.minimum_rate AND a.maximum_rate THEN 'G' " +
                "WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 10) / 100) AND (a.maximum_rate - (a.maximum_rate * 10) / 100) THEN 'R' " +
                "WHEN b.rate_per_sqft BETWEEN (a.minimum_rate - (a.minimum_rate * 15) / 100) AND (a.maximum_rate - (a.maximum_rate * 15) / 100) THEN 'Y' " +
                "ELSE 'B' END AS flag " +
                "FROM price_pegging a, dsa_export b " +
                "WHERE a.pincode = b.property_pincode AND a.region = b.region " +
                "AND a.zone_dist = b.zone AND a.location = b.location";

        try {
            jasperReport = jdbcTemplate.query(jasperquery, new BeanPropertyRowMapper<>(JasperReport.class));


            for (JasperReport jspr : jasperReport) {
                if (jspr.getFlag().equals(flag)) {
                    jsp.add(jspr);

                }
            }
            System.out.println(generateExcel(jsp));
        }
        catch (Exception e) {
            System.out.println(e);
        }

            return jsp;
        }

        public String generateExcel(List<JasperReport> jasperReports) throws IOException {
            Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("JasperReport Data");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"s_no", "application_no","disbursal_date", "property_address", "property_pinCode", "region", "zone",
                    "location", "rate_per_sqft", "property_type", "lattitude", "longitude", "product",
                    "upload_date", "minimum_rate", "maximum_rate", "flag"};

            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
                int rowNum = 1;
                for (JasperReport jspr : jasperReports) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(jspr.getS_no());
                    row.createCell(1).setCellValue(jspr.getApplication_no());
                    row.createCell(2).setCellValue(jspr.getDisbursal_date());
                    row.createCell(3).setCellValue(jspr.getProperty_address());
                    row.createCell(4).setCellValue(jspr.getProperty_pinCode());
                    row.createCell(5).setCellValue(jspr.getRegion());
                    row.createCell(6).setCellValue(jspr.getZone());
                    row.createCell(7).setCellValue(jspr.getLocation());
                    row.createCell(8).setCellValue(jspr.getRate_per_sqft());
                    row.createCell(9).setCellValue(jspr.getProperty_type());
                    row.createCell(10).setCellValue(jspr.getLattitude());
                    row.createCell(11).setCellValue(jspr.getLongitude());
                    row.createCell(12).setCellValue(jspr.getProduct());
                    row.createCell(13).setCellValue(jspr.getUpload_date());
                    row.createCell(14).setCellValue(jspr.getMinimum_rate());
                    row.createCell(15).setCellValue(jspr.getMaximum_rate());
                    row.createCell(16).setCellValue(jspr.getFlag());
                }
                // Save the workbook to a file
                FileOutputStream fileOut = new FileOutputStream("D:\\excel//Jasper.xlsx");
                    workbook.write(fileOut);

        return "success";

    }
}







