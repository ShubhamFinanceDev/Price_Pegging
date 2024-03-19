package com.price.pegging.Repository;

import com.price.pegging.Entity.PricePegging;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PricePeggingRepository extends JpaRepository<PricePegging, Long> {

    @Query("select pp from PricePegging pp where (:zone IS NULL OR pp.zoneDist = :zone) AND (:region IS NULL OR pp.region = :region)AND (:pinCode IS NULL OR pp.pinCode = :pinCode)")
    List<PricePegging> findByZoneAndRegion(String zone, String region, Pageable pageable,String pinCode);
    @Query("select count(pp) from PricePegging pp where (:zone IS NULL OR pp.zoneDist = :zone) AND (:region IS NULL OR pp.region = :region)AND (:pinCode IS NULL OR pp.pinCode = :pinCode)")
    long findByZoneAndRegion(String zone, String region,String pinCode);

    @Query("select DISTINCT(pp.zone) pp from PricePegging pp")
    List getUniqeZones();


    // List<PricePegging> findByUpdateddate(String updateddate);
//    @Query("select pp from PricePegging pp where pp.zone=:zone AND pp.uploadDate between :fromDate AND :toDate")
    @Query("SELECT pp FROM PricePegging pp " +
            "WHERE (:fromDate IS NULL OR pp.uploadDate >= :fromDate) " +
            "AND (:toDate IS NULL OR pp.uploadDate <= :toDate) " +
            "AND (:zone IS NULL OR pp.zoneDist = :zone) " +
            "AND (:region IS NULL OR pp.region = :region)" +
            "AND (:pinCode IS NULL OR pp.pinCode = :pinCode)")
    List<PricePegging> findByZoneAndFromDateToRegion(String zone, Date fromDate, Date toDate, String region, Pageable pageable,String pinCode);  //change dataType toDate and fromDate

    @Query("SELECT count(pp) FROM PricePegging pp " +
            "WHERE (:fromDate IS NULL OR pp.uploadDate >= :fromDate) " +
            "AND (:toDate IS NULL OR pp.uploadDate <= :toDate) " +
            "AND (:zone IS NULL OR pp.zoneDist = :zone) " +
            "AND (:region IS NULL OR pp.region = :region)" +
            "AND (:pinCode IS NULL OR pp.pinCode = :pinCode)")
    long findByZoneAndFromDateToRegion(String zone, Date fromDate, Date toDate, String region,String pinCode);  //change dataType toDate and fromDate

    @Query("select distinct pp.zoneDist  from PricePegging pp ")
    List getAllDistinctZone();

    @Query("SELECT DISTINCT date_format(p.uploadDate,'%d-%b'), max(p.minimumRate),max (p.maximumRate),max(p.averageRate) FROM PricePegging p WHERE p.zoneDist = :zone AND p.locations = :location group by p.uploadDate")
    List<Object[]> findDataByZoneLocation(String zone, String location);

    @Query("select distinct pp.region  from PricePegging pp ")
    List getAllDistinctRegion();

}
