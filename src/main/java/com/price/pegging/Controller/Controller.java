package com.price.pegging.Controller;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import com.price.pegging.Service.Service;
<<<<<<< HEAD
<<<<<<< Updated upstream
=======

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections4.map.HashedMap;
>>>>>>> Stashed changes
=======
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Workbook;
>>>>>>> development
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

<<<<<<< HEAD
import java.io.FileNotFoundException;
=======
import java.io.IOException;
import java.sql.Date;
>>>>>>> development
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class Controller {

    @Autowired
    private Service service;

    @CrossOrigin
    @PostMapping("/loginValidation")
    public ResponseEntity<UserDetail> loginAuthentication(@RequestBody User userRequest) {

        User userDetail = new User();
        UserDetail userDetail1 = new UserDetail();
        String userEmail = userRequest.getEmail();
        String userPassword = userRequest.getPassword();

        try {
            if (!userEmail.isEmpty() && userEmail.contains("@shubham") && !userPassword.isEmpty()) {
                userDetail = service.userExist(userEmail);

                if (userDetail !=null) {
                    // System.out.print(userDetail.get(0).getPassword());
                    userDetail1 = service.passwordMatch(userPassword, userDetail);

                } else {
                    System.out.println("Invalid email");
                    userDetail1.setCode("1111");
                    userDetail1.setMsg("User does not exist");
                }
            } else {
                System.out.println("Invalid email");
                userDetail1.setCode("1111");
                userDetail1.setMsg("Invalid user email");
            }
        } catch (Exception e) {
            System.out.println(e);
            userDetail1.setCode("1111");
            userDetail1.setMsg("Technical issue");
        }

        return new ResponseEntity<UserDetail>(userDetail1, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/dsaExportUpload")
    public ResponseEntity<CommonResponse> exportFileUpload(@RequestParam("file") MultipartFile file) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse = service.readDataDsa(file);

        return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/pricePeggingUpload")
    public ResponseEntity<CommonResponse> peggingFileUpload(@RequestParam("file") MultipartFile file) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse = service.peggingFileReadData(file);

        return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/pricePeggingData")
<<<<<<< HEAD
<<<<<<< Updated upstream
    public ResponseEntity<PricePeggingData> exportPeggingData(@RequestParam(name="zone",required = false) String zone,@RequestParam(name="uploadDate",required = false) String uploadDate)
=======
    public ResponseEntity<PricePeggingData> exportPeggingData(@RequestParam(name="pageNumber",required = false) Integer pageNumber,@RequestParam(name="zone",required = false) String zone,@RequestParam(name="fromDate",required = false) String fromDate,@RequestParam(name="toDate",required = false) String toDate,@RequestParam(name="region",required = false) String region)
>>>>>>> Stashed changes
    {
        List<PricePegging> pricePeggingDatas=new ArrayList<>();
        PricePeggingData pricePeggingData= new PricePeggingData();

        pricePeggingDatas =service.getAllPricePeggingData(zone,uploadDate);

        System.out.println(pricePeggingDatas.size());
        if(pricePeggingDatas.isEmpty())
        {
<<<<<<< Updated upstream
            pricePeggingData.setCode("1111");
            pricePeggingData.setMsg("Data not found");
            pricePeggingData.setPricePeggingList(null);
=======
            pricePeggingDatas=service.getAllPricePeggingDataByZonFromDateToRegion(pageNumber,zone,fromDate,toDate,region);
        }
        else if(fromDate==null && toDate ==null)
        {
            pricePeggingDatas=service.getAllPricePeggingDataByZoneAndRegion(pageNumber,zone,region);
>>>>>>> Stashed changes
=======
    public ResponseEntity<PricePeggingData> exportPeggingData(@RequestParam(name = "zone", required = false) String zone, @RequestParam(name = "fromDate", required = false) String fromDate, @RequestParam(name = "toDate", required = false) String toDate, @RequestParam(name = "region", required = false) String region) {
        List<PricePegging> pricePeggingDatas = new ArrayList<>();
        PricePeggingData pricePeggingData = new PricePeggingData();

        if (fromDate != null && toDate != null) {
            pricePeggingDatas = service.getAllPricePeggingDataByZonFromDateToRegion(zone, fromDate, toDate, region);
        } else if (fromDate == null && toDate == null) {
            pricePeggingDatas = service.getAllPricePeggingDataByZoneAndRegion(zone, region);
        } else {
            pricePeggingData.setCode("1111");
            pricePeggingData.setMsg("Please select required field");
>>>>>>> development
        }

        if (pricePeggingData.getCode() == null) {
            if (pricePeggingDatas.isEmpty()) {
                pricePeggingData.setCode("1111");
                pricePeggingData.setMsg("Data not found");
                pricePeggingData.setPricePeggingList(null);
            } else {
                pricePeggingData.setCode("0000");
                pricePeggingData.setMsg("Data found successfully");
                pricePeggingData.setPricePeggingList(pricePeggingDatas);
            }
        }
        return new ResponseEntity<PricePeggingData>(pricePeggingData, HttpStatus.OK);

    }


    @CrossOrigin
    @GetMapping("/exportData")
<<<<<<< HEAD
    public ResponseEntity<ExportModel> exportData(@RequestParam(name="pageNumber",required = false) Integer pageNumber,@RequestParam(name="applicationNo",required = false) String applicationNo,@RequestParam(name="uploadDate",required = false) String uploadDate,@RequestParam(name="zone",required = false) String zone,@RequestParam(name="region",required = false) String region)
=======
    public ResponseEntity<DsaDataResponse> exportData(@RequestParam(name = "applicationNo", required = false) String applicationNo /*, @RequestParam(name="uploadDate",required = false) Date uploadDate*/, @RequestParam(name = "zone", required = false) String zone, @RequestParam(name = "region", required = false) String region, @RequestParam(name = "fromDate", required = false) Date fromDate, @RequestParam(name = "toDate", required = false) Date toDate)      // changes for from to todate
>>>>>>> development
    {

<<<<<<< HEAD
        dsaExports=service.getAllExportData(pageNumber,applicationNo,uploadDate,region,zone);
        System.out.println(dsaExports.size());
        if(dsaExports.isEmpty())
        {
            dsaExportData.setCode("1111");
            dsaExportData.setMsg("Data not found");
            dsaExportData.setDsaExportList(null);
=======
        DsaDataResponse dsaDataResponse = new DsaDataResponse();
        if ((fromDate != null && toDate != null) || (fromDate == null && toDate == null)) {
            dsaDataResponse = service.getAllDsaData(fromDate, toDate, applicationNo, region, zone);
        } else {
            dsaDataResponse.setCode("1111");
            dsaDataResponse.setMsg("Both From date and To date are required for date range search.");

>>>>>>> development
        }

        if (dsaDataResponse.getCode() != "1111") {
            if (dsaDataResponse.getDsaExportList().isEmpty()) {
                dsaDataResponse.setCode("1111");
                dsaDataResponse.setMsg("Data not found");

            } else {
                dsaDataResponse.setCode("0000");
                dsaDataResponse.setMsg("Data found successfully");
            }
        }
        return new ResponseEntity<DsaDataResponse>(dsaDataResponse, HttpStatus.OK);

    }
<<<<<<< HEAD
    
    @CrossOrigin
    @GetMapping("/exportDsa")
    public List<DsaDataComparison> checkRate(@RequestParam(name="applicationNo",required=false) String applicationNo, @RequestParam(name="location",required=false) String location,@RequestParam(name="pinCode",required=false) String pinCode) throws FileNotFoundException, JRException
    {
    	  List<DsaDataComparison> status = new ArrayList<>();
    	  
           status=service.rateRange(location, pinCode);
           return status;
		
    	
    }
    
    @CrossOrigin
    @GetMapping("/flag")
    public List<DsaDataComparison> checkstatus(@RequestParam(name="flag",required=false) String flag) throws FileNotFoundException, JRException
    {
    	  List<DsaDataComparison> status = new ArrayList<>();
    	  
           status=service.checkStatus(flag);
           return status;
		
    	
    }
    
=======

>>>>>>> development
    @CrossOrigin
    @GetMapping("/allZone")
    public List zoneDetail() {
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
        DashboardGraph dashboardGraph = new DashboardGraph();
        dashboardGraph = service.countTotalByDate();
        return dashboardGraph;
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
        List<PricePeggingLineChart> pricePeggingLineCharts = new ArrayList<>();
        pricePeggingLineCharts = service.getDataByZoneLocation(zone, location);
        if (!(pricePeggingLineCharts.isEmpty())) {
            commonResponseForLineChart.setCode("0000");
            commonResponseForLineChart.setMsg("Data found successfully.");
            commonResponseForLineChart.setPricePeggingLineCharts(pricePeggingLineCharts);
        } else {
            commonResponseForLineChart.setCode("1111");
            commonResponseForLineChart.setMsg("Data Not found.");
            commonResponseForLineChart.setPricePeggingLineCharts(pricePeggingLineCharts);
        }


        return commonResponseForLineChart;
    }


    //this get api is updated by shagun.....

    @CrossOrigin
    @GetMapping("/getDataForMap")
    public CommonDsaExportData getDsaExportData(@RequestParam(name = "propertyPincode", required = false) String propertyPincode, @RequestParam(name = "region", required = false) String region, @RequestParam(name = "zone", required = false) String zone) {
        List<DsaExportData> dsaExportData = new ArrayList<>();
        CommonDsaExportData commonDsaExportData = new CommonDsaExportData();

        if (propertyPincode != null && region != null && zone != null) {
            dsaExportData = service.getDataByPropertyPinCodeRegionZoneLocation(propertyPincode, region, zone);
            if(!(dsaExportData.isEmpty())) {
                commonDsaExportData.setCode("0000");
                commonDsaExportData.setMsg("Data found successfully");
                commonDsaExportData.setDsaExportData(dsaExportData);
            }
            else
            {
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
    public CommonResponse userdetail(@RequestBody User usermodel) {

        CommonResponse commonResponse = new CommonResponse();
        String email = usermodel.getEmail();
        if (email != null && email.contains
                ("@shubham")) {
            commonResponse = service.saveuser(usermodel);

        } else {
            commonResponse.setCode("1111");
            commonResponse.setMsg("invalid email");

        }
        return commonResponse;
    }

    @GetMapping("/invokeDsaReport/{type}")
    public ResponseEntity<String> invokeDsaReport(@PathVariable String type,HttpServletResponse response) throws IOException {

        CommonResponse commonResponse=new CommonResponse();
        List<DsaDataModel> dsaDataModelList = service.readData();
        if(!(dsaDataModelList.isEmpty()))
        {
           commonResponse= service.generateReport(dsaDataModelList,type,response);
           if(commonResponse.getCode()=="0000") {
               return new ResponseEntity("Success", HttpStatus.PROCESSING);
           }
           else
           {
               return new ResponseEntity("Technical issue", HttpStatus.OK);

           }
        }

        return new ResponseEntity("Data not found",HttpStatus.NOT_FOUND);
    }

}


