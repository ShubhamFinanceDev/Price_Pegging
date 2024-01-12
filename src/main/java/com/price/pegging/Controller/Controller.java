package com.price.pegging.Controller;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.*;
import com.price.pegging.Entity.User;
import com.price.pegging.Service.Service;
<<<<<<< Updated upstream
=======

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections4.map.HashedMap;
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private Service service;
@CrossOrigin
    @PostMapping("/loginValidation")
    public ResponseEntity<UserDetail> loginAuthentication(@RequestBody User userRequest)
    {

        List<User> userDetail=new ArrayList<>();
        UserDetail commonResponse= new UserDetail();
        String userEmail=userRequest.getEmail();
        String userPassword=userRequest.getPassword();

try {
    if (!userEmail.isEmpty() && userEmail.contains("@shubham") && !userPassword.isEmpty()) {
        userDetail = service.userExist(userEmail);

        if (!CollectionUtils.isEmpty(userDetail)) {
            // System.out.print(userDetail.get(0).getPassword());
            commonResponse = service.passwordMatch(userPassword, userDetail.get(0));

        } else {
            System.out.println("Invalid email");
            commonResponse.setCode("1111");
            commonResponse.setMsg("User does not exist");
        }
    } else {
        System.out.println("Invalid email");
        commonResponse.setCode("1111");
        commonResponse.setMsg("Invalid user email");
    }
}
catch (Exception e)
{
    System.out.println(e);
}


        return new ResponseEntity<UserDetail>(commonResponse, HttpStatus.OK);
    }

@CrossOrigin
    @PostMapping("/dsaExportUpload")
    public ResponseEntity<CommonResponse> exportFileUpload(@RequestParam("file") MultipartFile file)
    {
        CommonResponse commonResponse=new CommonResponse();
        commonResponse=service.readDataDsa(file);

        return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.OK);
    }
@CrossOrigin
    @PostMapping("/pricePeggingUpload")
    public ResponseEntity<CommonResponse> peggingFileUpload(@RequestParam("file") MultipartFile file)
    {
        CommonResponse commonResponse=new CommonResponse();
        commonResponse=service.peggingFileReadData(file);

        return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.OK);
    }
@CrossOrigin
    @GetMapping("/pricePeggingData")
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
        }
        else
        {
            pricePeggingData.setCode("0000");
            pricePeggingData.setMsg("Data found successfully");
            pricePeggingData.setPricePeggingList(pricePeggingDatas);
        }
        return new ResponseEntity<PricePeggingData>(pricePeggingData, HttpStatus.OK);

    }






@CrossOrigin
    @GetMapping("/exportData")
    public ResponseEntity<ExportModel> exportData(@RequestParam(name="pageNumber",required = false) Integer pageNumber,@RequestParam(name="applicationNo",required = false) String applicationNo,@RequestParam(name="uploadDate",required = false) String uploadDate,@RequestParam(name="zone",required = false) String zone,@RequestParam(name="region",required = false) String region)
    {
        List<DsaExport> dsaExports= new ArrayList<>();
        ExportModel dsaExportData= new ExportModel();

        dsaExports=service.getAllExportData(pageNumber,applicationNo,uploadDate,region,zone);
        System.out.println(dsaExports.size());
        if(dsaExports.isEmpty())
        {
            dsaExportData.setCode("1111");
            dsaExportData.setMsg("Data not found");
            dsaExportData.setDsaExportList(null);
        }
        else
        {
            dsaExportData.setCode("0000");
            dsaExportData.setMsg("Data found successfully");
            dsaExportData.setDsaExportList(dsaExports);
        }
        return new ResponseEntity<ExportModel>(dsaExportData, HttpStatus.OK);

    }
    
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
    
    @CrossOrigin
    @GetMapping("/allZone")
    public List zoneDetail()
    {
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
    DashboardDistinctDetail dashboardDetail()
    {
        DashboardDistinctDetail dashboardDistinctDetail=new DashboardDistinctDetail();
        dashboardDistinctDetail=service.getAllDashboarDetail();

        return dashboardDistinctDetail;
    }

    @CrossOrigin
    @GetMapping("/dashboardGraphCount")
    DashboardGraph dashboardGraph()
    {
        DashboardGraph dashboardGraph=new DashboardGraph();

        dashboardGraph=service.countTotalByDate();
        return dashboardGraph;
    }

    @CrossOrigin
    @GetMapping("/filterOption")
    FilterModel getFilterData()
    {
        FilterModel filterModel=new FilterModel();

        filterModel=service.getAllFilterData();
        return filterModel;
    }

}


