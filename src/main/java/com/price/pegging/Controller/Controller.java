package com.price.pegging.Controller;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.ExportModel;
import com.price.pegging.Model.PricePeggingData;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import com.price.pegging.Service.Service;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.JavaScriptUtils;

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
    public ResponseEntity<PricePeggingData> exportPeggingData(@RequestParam(name="zone",required = false) String zone,@RequestParam(name="uploadDate",required = false) String uploadDate)
    {
        List<PricePegging> pricePeggingDatas=new ArrayList<>();
        PricePeggingData pricePeggingData= new PricePeggingData();

        pricePeggingDatas =service.getAllPricePeggingData(zone,uploadDate);

        System.out.println(pricePeggingDatas.size());
        if(pricePeggingDatas.isEmpty())
        {
            pricePeggingData.setCode("1111");
            pricePeggingData.setMsg("Data not found");
            pricePeggingData.setPricePeggingList(null);
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
    public ResponseEntity<ExportModel> exportData(@RequestParam(name="applicationNo",required = false) String applicationNo,@RequestParam(name="uploadDate",required = false) String uploadDate)
    {
        List<DsaExport> dsaExports= new ArrayList<>();
        ExportModel dsaExportData= new ExportModel();

        dsaExports=service.getAllExportData(applicationNo,uploadDate);
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
    @GetMapping("/allZone")
    public List zoneDetail()
    {
        return service.getAllZone();
    }

}

