package com.price.pegging.Repository;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Model.FilterModel;
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
        "WHERE (:uploadDate IS NULL OR d.uploadDate = :uploadDate) " +
        "AND (:zone IS NULL OR d.zone = :zone) " +
        "AND (:region IS NULL OR d.region = :region) " +
        "AND (:applicationNo IS NULL OR d.applicationNo = :applicationNo)")
    List<DsaExport> findByAll(String applicationNo, String uploadDate, String region, String zone);
}
