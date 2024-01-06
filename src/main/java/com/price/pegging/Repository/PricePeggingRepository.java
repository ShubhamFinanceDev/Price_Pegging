package com.price.pegging.Repository;

import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.FilterModel;
import com.price.pegging.Model.PricePeggingLineChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricePeggingRepository extends JpaRepository<PricePegging,Long> {

   @Query("select pp from PricePegging pp where (:zone IS NULL OR pp.zone = :zone) AND (:region IS NULL OR pp.region = :region)")
    List<PricePegging> findByZoneAndRegion(String zone,String region);
    @Query("select DISTINCT(pp.zone) pp from PricePegging pp")
    List getUniqeZones();


    // List<PricePegging> findByUpdateddate(String updateddate);
//    @Query("select pp from PricePegging pp where pp.zone=:zone AND pp.uploadDate between :fromDate AND :toDate")
  @Query("SELECT pp FROM PricePegging pp " +
          "WHERE (:fromDate IS NULL OR pp.uploadDate >= :fromDate) " +
          "AND (:toDate IS NULL OR pp.uploadDate <= :toDate) " +
          "AND (:zone IS NULL OR pp.zone = :zone) " +
          "AND (:region IS NULL OR pp.region = :region)")
    List<PricePegging> findByZoneAndFromDateTo(String zone, String fromDate,String toDate,String region);
 @Query("select distinct pp.zone  from PricePegging pp ")
 List getAllDistinctZone();

 @Query("select new com.price.pegging.Model.PricePeggingLineChart(pp.minimumRate,pp.maximumRate,pp.averageRate,pp.uploadDate) from PricePegging pp where pp.zone=:zone AND pp.locations=:location and pp.uploadDate in (select distinct(rr.uploadDate) from  PricePegging rr)")
 List<PricePeggingLineChart> findDataByZoneLocation(String zone,String location);
@Query("select distinct pp.region  from PricePegging pp ")
List getAllDistinctRegion();
}
