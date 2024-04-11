package com.price.pegging.Controller;

import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import com.price.pegging.Service.Service;
import com.price.pegging.Utilitty.PasswordPattern;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin
public class Controller {

    @Autowired
    private Service service;
    @Autowired
    private PasswordPattern passwordPattern;

    @CrossOrigin
    @PostMapping("/loginValidation")
    public ResponseEntity<UserDetail> loginAuthentication(@RequestBody User userRequest) {

        UserDetail userDetail1 = new UserDetail();
        String userEmail = userRequest.getEmail();
        String userPassword = userRequest.getPassword();

        if (userEmail.contains("@shubham") && passwordPattern.patternCheck(userPassword)) {

            try {
                User userDetail = service.userExist(userEmail);
                userDetail1 = service.passwordMatch(userPassword, userDetail);
            } catch (Exception e) {
                userDetail1.setCode("1111");
                userDetail1.setMsg("User-email does not exist.");
            }
            return new ResponseEntity<>(userDetail1, HttpStatus.OK);
        }
        System.out.println("User-email & password invalid.");
        userDetail1.setCode("1111");
        userDetail1.setMsg("User-email & password format invalid.");

        return new ResponseEntity<>(userDetail1, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/dsaExportUpload")
    public ResponseEntity<CommonResponse> exportFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(value = "uploadBy", required = false) String uploadBy  ) {
        CommonResponse commonResponse = service.readDataDsa(file, uploadBy);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/pricePeggingUpload")
    public ResponseEntity<CommonResponse> peggingFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(value = "uploadBy",required = false) String uploadBy) {
        CommonResponse commonResponse = service.peggingFileReadData(file, uploadBy);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/pricePeggingData")                               //change data type of toDate and fromDate
    public ResponseEntity<PricePeggingData> exportPeggingData(@RequestParam(name = "zone", required = false) String zone, @RequestParam(name = "fromDate", required = false) Date fromDate, @RequestParam(name = "toDate", required = false) Date toDate, @RequestParam(name = "region", required = false) String region, @RequestParam(name = "pageNo", required = true) int pageNo, @RequestParam(name = "pinCode", required = false) String pinCode, @RequestParam(name = "area", required = false) String area) {
        PricePeggingData pricePeggingData = new PricePeggingData();

        if (fromDate != null && toDate != null) {
            pricePeggingData = service.getAllPricePeggingDataByZonFromDateToRegion(zone, fromDate, toDate, region, pageNo, pinCode, area);
        } else if (fromDate == null && toDate == null) {
            pricePeggingData = service.getAllPricePeggingDataByZoneAndRegion(zone, region, pageNo, pinCode, area);

        } else {
            pricePeggingData.setCode("1111");
            pricePeggingData.setMsg("Please select required field");
        }
        return new ResponseEntity<>(pricePeggingData, HttpStatus.OK);

    }


    @CrossOrigin
    @GetMapping("/exportData")
    public ResponseEntity<DsaDataResponse> exportData(@RequestParam(name = "applicationNo", required = false) String applicationNo /*, @RequestParam(name="uploadDate",required = false) Date uploadDate*/, @RequestParam(name = "zone", required = false) String zone, @RequestParam(name = "region", required = false) String region, @RequestParam(name = "fromDate", required = false) Date fromDate, @RequestParam(name = "toDate", required = false) Date toDate, @RequestParam(name = "pageNo", required = true) Integer pageNo, @RequestParam(name = "pinCode", required = false) String pinCode, @RequestParam(name = "flag", required = false) String flag)      // changes for from to todate
    {
        DsaDataResponse dsaDataResponse = new DsaDataResponse();

        if ((fromDate != null && toDate != null) || (fromDate == null && toDate == null)) {
            dsaDataResponse = service.getAllDsaData(fromDate, toDate, applicationNo, region, zone, pageNo, pinCode, flag);
        } else {
            dsaDataResponse.setCode("1111");
            dsaDataResponse.setMsg("Both From date and To date are required for date range search.");
        }
        return new ResponseEntity<>(dsaDataResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/allZone")
    public List<String> zoneDetail() {
        return service.getAllZone();
    }

//    @CrossOrigin
//    @GetMapping("/region")
//    public List regionDetail()
//    {
//        return service.regionAll();
//    }

    @CrossOrigin
    @GetMapping("/dashboardDistinctDetail")
    DashboardDistinctDetail dashboardDetail() {
        DashboardDistinctDetail dashboardDistinctDetail = new DashboardDistinctDetail();
        dashboardDistinctDetail = service.getAllDashboarDetail();
        return dashboardDistinctDetail;
    }

    @CrossOrigin
    @GetMapping("/dashboardGraphCount")
    DashboardGraph dashboardGraph() {
        return service.graphCount();
    }

    @CrossOrigin
    @GetMapping("/filterOption")
    FilterModel getFilterData() {
        FilterModel filterModel = new FilterModel();

        filterModel = service.getAllFilterData();
        return filterModel;
    }

    @CrossOrigin
    @GetMapping("/lineChartForPricePegging/{zone}/{location}")
    public CommonResponseForLineChart getDataForLineChart(@PathVariable String zone, @PathVariable String location) {

        CommonResponseForLineChart commonResponseForLineChart = new CommonResponseForLineChart();
        List<PricePeggingLineChart> pricePeggingLineCharts = service.getDataByZoneLocation(zone, location);
        if (!(pricePeggingLineCharts.isEmpty())) {
            commonResponseForLineChart.setCode("0000");
            commonResponseForLineChart.setMsg("Data found successfully.");
            commonResponseForLineChart.setPricePeggingLineCharts(pricePeggingLineCharts);
        } else {
            commonResponseForLineChart.setCode("1111");
            commonResponseForLineChart.setMsg("Data not found.");
            commonResponseForLineChart.setPricePeggingLineCharts(pricePeggingLineCharts);
        }

        return commonResponseForLineChart;
    }


    @CrossOrigin
    @GetMapping("/getDataForMap")
    public CommonDsaExportData getDsaExportData(@RequestParam(name = "propertyPincode", required = false) String propertyPincode, @RequestParam(name = "region", required = false) String region, @RequestParam(name = "zone", required = false) String zone) {
        CommonDsaExportData commonDsaExportData = new CommonDsaExportData();

        if (propertyPincode != null && region != null && zone != null) {
            List<DsaExportData> dsaExportData = service.getDataByPropertyPinCodeRegionZoneLocation(propertyPincode, region, zone);
            if (!(dsaExportData.isEmpty())) {
                commonDsaExportData.setCode("0000");
                commonDsaExportData.setMsg("Data found successfully");
                commonDsaExportData.setDsaExportData(dsaExportData);
            } else {
                commonDsaExportData.setCode("1111");
                commonDsaExportData.setMsg("Data not found");
                commonDsaExportData.setDsaExportData(null);
            }
            return commonDsaExportData;

        } else {
            commonDsaExportData.setCode("1111");
            commonDsaExportData.setMsg("please select the required field");
            commonDsaExportData.setDsaExportData(null);
        }


        System.out.println("value check of return=" + commonDsaExportData);
        return commonDsaExportData;
    }

    @PostMapping("/addUser")
    public CommonResponse userdetail(@RequestBody User userModel) {

        CommonResponse commonResponse = new CommonResponse();
        String email = userModel.getEmail();

        if (email != null && email.contains
                ("@shubham") && passwordPattern.patternCheck(userModel.getPassword())) {
            commonResponse = service.saveuser(userModel);

        } else {
            commonResponse.setCode("1111");
            commonResponse.setMsg("User-email & password format invalid");

        }
        return commonResponse;
    }

    @GetMapping("/invokeDsaReport/{type}")
    public ResponseEntity<String> invokeDsaReport(@PathVariable String type, HttpServletResponse response) throws IOException {

        List<DsaDataModel> dsaDataModelList = service.readData();
        CommonResponse commonResponse = service.generateReport(dsaDataModelList, type, response);
        return new ResponseEntity<>(commonResponse.getMsg(), HttpStatus.PROCESSING);

    }
}


