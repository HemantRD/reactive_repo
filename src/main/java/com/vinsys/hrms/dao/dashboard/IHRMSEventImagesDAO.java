package com.vinsys.hrms.dao.dashboard;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.dashboard.EventImages;

public interface IHRMSEventImagesDAO extends JpaRepository<EventImages, Long> {

	@Query(" SELECT eventImages FROM EventImages eventImages JOIN eventImages.events events "
			+ " WHERE eventImages.events.orgId = ?1 AND eventImages.events.startDate <= ?2 "
			+ " AND eventImages.events.endDate >= ?2 AND eventImages.events.isActive= ?3 AND eventImages.isActive= ?3 order by eventImages.createdDate desc")
	List<EventImages> getEventImages(long orgId, LocalDate now, String isActive);
	
	List<EventImages> findByEventsEventNameAndIsActiveOrderByUpdatedDateDesc(String eventName,String isActive);
	
	@Query(value="update tbl_dashboard_gallery_images set is_active='N'\r\n"
			+ "where tbl_dashboard_gallery_images.image_path=?1 \r\n"
			+ "AND tbl_dashboard_gallery_images.is_active=?2",nativeQuery = true)
	void deleteImage(String imagePath,String isActive);

}
