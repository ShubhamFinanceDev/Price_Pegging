package com.price.pegging.Repository;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Model.DsaExportData;
import com.price.pegging.Model.FilterModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DsaExportRepository extends JpaRepository<DsaExport,Long> {
   @Query("select ds from DsaExport ds where ds.applicationNo=:applicationNo")
    List<DsaExport> findByApplicationNo(String applicationNo);

    @Query("select pp from DsaExport pp where pp.applicationNo=:applicationNo AND pp.uploadDate=:updateddate")
    List<DsaExport> findByApllicationAndUpdatedDate(String applicationNo, String updateddate);
    @Query("select pp from DsaExport pp where pp.uploadDate=:updateddate")
    List<DsaExport> findByUpdatedDate(String updateddate);
 @Query("select distinct pp.zone from DsaExport pp ")
    List getAllDistinctZone();
 @Query("select distinct pp.region   from DsaExport pp ")
 List getAllDistinctRegion();

@Query("SELECT d FROM DsaExport d " +
        "WHERE (:disbursalDate IS NULL OR d.disbursalDate = :disbursalDate) " +
        "AND (:zone IS NULL OR d.zone = :zone) " +
        "AND (:region IS NULL OR d.region = :region) " +
        "AND (:applicationNo IS NULL OR d.applicationNo = :applicationNo)")
    List<DsaExport> findByAll(String applicationNo, String disbursalDate, String region, String zone, Pageable pageable);
    @Query("select new com.price.pegging.Model.DsaExportData(d.property_address,d.lattitude,d.longitude) from DsaExport d where d.location = :location and d.propertyPincode = :propertyPincode and d.region = :region and d.zone = :zone")
    List<DsaExportData> findByPropertyPinCodeRegionZoneLocation(String propertyPincode, String region, String zone, String location);
}
