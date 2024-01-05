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

   @Query("select pp from PricePegging pp where pp.zone=:zone")
    List<PricePegging> findByZone(String zone);
    @Query("select DISTINCT(pp.zone) pp from PricePegging pp")
    List getUniqeZones();


    // List<PricePegging> findByUpdateddate(String updateddate);
    @Query("select pp from PricePegging pp where pp.zone=:zone AND pp.uploadDate between :fromDate AND :toDate")
    List<PricePegging> findByZoneAndFromDateTo(String zone, String fromDate,String toDate);
    @Query("select pp from PricePegging pp where pp.uploadDate between :fromDate AND :toDate")
    List<PricePegging> findByFromDateTo(String fromDate,String toDate);
 @Query("select distinct pp.zone  from PricePegging pp ")
 List getAllDistictZone();

 @Query("select new com.price.pegging.Model.PricePeggingLineChart(pp.minimumRate,pp.maximumRate,pp.averageRate,pp.uploadDate) from PricePegging pp where pp.zone=:zone AND pp.locations=:location and pp.uploadDate in (select distinct(rr.uploadDate) from  PricePegging rr)")
 List<PricePeggingLineChart> findDataByZoneLocation(String zone,String location);


}
