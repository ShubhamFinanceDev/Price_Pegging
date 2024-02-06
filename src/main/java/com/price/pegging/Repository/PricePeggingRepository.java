package com.price.pegging.Repository;

import com.price.pegging.Entity.PricePegging;
import com.price.pegging.Model.FilterModel;
import com.price.pegging.Model.PricePeggingLineChart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface PricePeggingRepository extends JpaRepository<PricePegging, Long> {

<<<<<<< HEAD
<<<<<<< Updated upstream
   @Query("select pp from PricePegging pp where pp.zone=:zone")
    List<PricePegging> findByZone(String zone);
=======
   @Query("select pp from PricePegging pp where (:zone IS NULL OR pp.zoneDist = :zone) AND (:region IS NULL OR pp.region = :region)")
    List<PricePegging> findByZoneAndRegion(String zone, String region, Pageable pageable);
>>>>>>> development
    @Query("select DISTINCT(pp.zone) pp from PricePegging pp")
    List getUniqeZones();
=======
	@Query("select pp from PricePegging pp where (:zone IS NULL OR pp.zoneDist = :zone) AND (:region IS NULL OR pp.region = :region)")
	List<PricePegging> findByZoneAndRegion(String zone, String region, Pageable pageable);
>>>>>>> Stashed changes

	@Query("select DISTINCT(pp.zone) pp from PricePegging pp")
	List getUniqeZones();

<<<<<<< Updated upstream
    // List<PricePegging> findByUpdateddate(String updateddate);
<<<<<<< HEAD
    @Query("select pp from PricePegging pp where pp.zone=:zone AND pp.uploadDate=:updateddate")
    List<PricePegging> findByZoneAndUpdatedDate(String zone, String updateddate);
    @Query("select pp from PricePegging pp where pp.uploadDate=:updateddate")
    List<PricePegging> findByUpdatedDate(String updateddate);
 @Query("select distinct pp.zone  from PricePegging pp ")
 List getAllDistictZone();
=======
	// List<PricePegging> findByUpdateddate(String updateddate);
//    @Query("select pp from PricePegging pp where pp.zone=:zone AND pp.uploadDate between :fromDate AND :toDate")
	@Query("SELECT pp FROM PricePegging pp " + "WHERE (:fromDate IS NULL OR pp.uploadDate >= :fromDate) "
			+ "AND (:toDate IS NULL OR pp.uploadDate <= :toDate) " + "AND (:zone IS NULL OR pp.zoneDist = :zone) "
			+ "AND (:region IS NULL OR pp.region = :region)")
	List<PricePegging> findByZoneAndFromDateToRegion(String zone, String fromDate, String toDate, String region,
			Pageable pageable);

	@Query("select distinct pp.zoneDist  from PricePegging pp ")
	List getAllDistinctZone();

	@Query("select new com.price.pegging.Model.PricePeggingLineChart(pp.minimumRate,pp.maximumRate,pp.averageRate,pp.uploadDate) from PricePegging pp where pp.zoneDist=:zone AND pp.locations=:location and pp.uploadDate in (select distinct(rr.uploadDate) from  PricePegging rr)")
	List<PricePeggingLineChart> findDataByZoneLocation(String zone, String location);

	@Query("select distinct pp.region  from PricePegging pp ")
	
	List getAllDistinctRegion();

	@Query("SELECT d FROM PricePegging d WHERE d.locations = :location AND d.pinCode = :pinCode")
	PricePegging findByLocationAndpropertyPincode(String location, String pinCode);
>>>>>>> Stashed changes
=======
//    @Query("select pp from PricePegging pp where pp.zone=:zone AND pp.uploadDate between :fromDate AND :toDate")
  @Query("SELECT pp FROM PricePegging pp " +
          "WHERE (:fromDate IS NULL OR pp.uploadDate >= :fromDate) " +
          "AND (:toDate IS NULL OR pp.uploadDate <= :toDate) " +
          "AND (:zone IS NULL OR pp.zoneDist = :zone) " +
          "AND (:region IS NULL OR pp.region = :region)")
    List<PricePegging> findByZoneAndFromDateToRegion(String zone, String fromDate,String toDate,String region,Pageable pageable);
 @Query("select distinct pp.zoneDist  from PricePegging pp ")
 List getAllDistinctZone();

 @Query("SELECT DISTINCT date_format(p.uploadDate,'%Y-%M'), max(p.minimumRate),max (p.maximumRate),max(p.averageRate) FROM PricePegging p WHERE p.zoneDist = :zone AND p.locations = :location group by p.uploadDate")
  List<Object[]> findDataByZoneLocation(String zone, String location);
@Query("select distinct pp.region  from PricePegging pp ")
List getAllDistinctRegion();
>>>>>>> development
}
