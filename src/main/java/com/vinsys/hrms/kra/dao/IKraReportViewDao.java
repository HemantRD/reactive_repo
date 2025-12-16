//package com.vinsys.hrms.kra.dao;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import com.vinsys.hrms.kra.entity.KraReportView;
//
//public interface IKraReportViewDao extends JpaRepository<KraReportView, Long> {
//
//	@Query(value = "select * from vw_kra_report vkr where kra_id =?1 and vkr.employee_id=?2",nativeQuery = true)
//	List<KraReportView> getKraDetailsByKraIdandEmpId(Long kraId, Long empId);
//
//}
