package com.price.pegging.Service;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface Service {

    public List<User> userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse      readDataDsa(MultipartFile file);

    CommonResponse peggingFileReadData(MultipartFile file);

    List<DsaExport> getAllExportData(String applicationNo,String uploadDate);

    List<PricePegging> getAllPricePeggingData(String zone,String uploadDate);

    List getAllZone();
}
