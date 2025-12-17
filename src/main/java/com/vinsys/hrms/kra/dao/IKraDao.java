package com.vinsys.hrms.kra.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.kra.service.impl.CycleCountResult;
import com.vinsys.hrms.kra.service.impl.CycleResult;

public interface IKraDao extends JpaRepository<Kra, Long> {

	public Kra findByIdAndIsActive(Long id, String isActive);

	public Kra findByIdAndIsActiveAndOrgId(Long id, String isActive, Long orgId);

	public Kra findByEmployeeIdAndIsActiveAndKraYear(Long employeeId, String isActive, KraYear year);
	
//	@Query(value = "select * from tbl_trn_kra ttk where ttk.employee_id =?1 and is_active =?2 ", nativeQuery = true)
//	public List<Kra> findByEmployeeIdAndIsActive(Long employeeId, String isActive, Pageable pageable);

//	@Query(value = "select * from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id  where ttk.employee_id =?1 and ttk.is_active =?2 ", nativeQuery = true)
//	public List<Kra> findByEmployeeIdAndIsActive(Long employeeId, String isActive, Pageable pageable);
	
	@Query(value = "select tmky.label ,* from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id JOIN tbl_mst_kra_year tmky ON tmky.id = ttk.year where ttk.employee_id =?1 and ttk.is_active =?2 and tmky.label = ?3 ", nativeQuery = true)
	public List<Kra> findByEmployeeIdAndIsActiveAndYear(Long employeeId, String isActive,String year, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk JOIN tbl_mst_kra_year tmky ON tmky.id = ttk.year where ttk.employee_id =?1 and ttk.is_active =?2 and tmky.year=?3 ", nativeQuery = true)
	public long countByEmployeeIdAndIsActiveAndYear(Long employeeId, String isActive,String year);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3  ", nativeQuery = true)
	public List<Kra> findByEmployeeByManger(Long loggedInEmpId, String isActive, String incomplete, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 ", nativeQuery = true)
	public long countByEmployeeByManger(Long managerId, String isActive, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ " where division =?1 and wf.status =?2 and kra.is_active =?3 ", nativeQuery = true)
	public List<Kra> findByDivIdAndStatus(Long divisionId, String status, String isActive, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra   join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ " where division =?1 and wf.status =?2 and kra.is_active =?3 ", nativeQuery = true)
	public long countByDivIdAndStatus(Long divisionId, String status, String isActive);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ " where division =?1 and wf.status =?2 and kra.is_active =?3 and kra.department =?4 ", nativeQuery = true)
	public List<Kra> findByDivIdAndDeptIdAndStatus(Long divisionId, String status, String isActive, Long deptId,
			Pageable pageable);

	@Query(value = "select count (*) from  tbl_trn_kra kra   join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ " where division =?1 and wf.status =?2 and kra.is_active =?3 and kra.department =?4  ", nativeQuery = true)
	public long countByDivIdAndDeptIdAndStatus(Long divisionId, String status, String isActive, Long deptId);

	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where division in (?1) and department in (?2) and (wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6 OR wf.status = ?7)  and kra.is_active =?8 ", nativeQuery = true)
	public List<Kra> findByDivIdInAndDeptIdInAndStatus(Object[] divisionIds, Object[] deptIds, String approve,
			 String reject, String inprocess,String completed,String incomplte, String isActive, Pageable pageable);

	
	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 ", nativeQuery = true)
	public List<Kra> findByDeptIdInAndStatus( Object[] deptIds, String approve,
			 String reject, String inprocess,String completed,String incomplte, String isActive, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where division in (?1) and department in (?2) and (wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6 OR wf.status = ?7)  and kra.is_active =?8 ", nativeQuery = true)
	public long countByDivIdInAndDeptIdInAndStatus(Object[] divisionIds, Object[] deptIds, String approve,
			 String reject, String inprocess,String completed,String incomplte,
			String isActive);
	
	
	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 ", nativeQuery = true)
	public long countByDeptIdInAndStatus( Object[] deptIds, String approve,
			 String reject, String inprocess,String completed,String incomplte,
			String isActive);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  where ttk.employee_id in (select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?3 and ttkw.status = ?2 ", nativeQuery = true)
	public List<Kra> findByEmployeeByMangerAndIsActive(Long managerId, String status, String isActive);

	@Query(value = "select * from tbl_employee te join tbl_trn_kra ttk on ttk.employee_id = te.id join tbl_trn_kra_wf ttkw on ttkw.kra_id = ttk.id where te.id =?1 and te.is_active =?2 and ttkw.status =?3 ", nativeQuery = true)
	public Kra findByEmployeeId(Long empId, String string, String string1);

	@Query(value = "select * from tbl_trn_kra ttk where ttk.org_id =?1 and ttk.employee_id=?2 and ttk.id =?3  and ttk.is_active =?4 ", nativeQuery = true)
	public Kra findByOrgIdAndEmpidAndIdAndIsActive(Long orgId, Long empId, Long id, String isActive);
	
	
	public Kra findByIdAndIsActiveAndKraYear(Long id, String isActive, KraYear year);

	@Query(value = "select * from tbl_trn_kra ttk where ttk.employee_id=?1 and ttk.mst_kra_cycle_id=?2  and ttk.is_active =?3 ", nativeQuery = true)
	public Kra findByEmpidAndCycleIdAndIsActive( Long empId, Long cycleId, String isActive);

	

//	@Query(value = "select * from tbl_trn_kra ttk where ttk.employee_id=?1 and ttk.mst_kra_cycle_id=?2  and ttk.is_active =?3 and ttk.year=?4", nativeQuery = true)
//	public Kra findByEmpidAndCycleIdAndIsActiveAndYear(Long empId,Long cycleId,String isActive, Long yearId);

	public Kra findByEmployeeIdAndIsActiveAndKraYearAndCycleId(Long employeeId, String isActive, KraYear year, KraCycle cycleId);

	
	@Query(value = "select * from tbl_trn_kra ttk where ttk.employee_id=?1 and ttk.mst_kra_cycle_id=?2 ", nativeQuery = true)
	public Kra findByEmpidAndCycleId( Long empId, Long cycleId);
	public List<Kra> findByIsActiveAndCycleId(String isactive, KraCycle cycle);
	
	@Query(value = "select employee_id from tbl_employee_reporting_manager term where reporting_manager_id =?1", nativeQuery = true)
	public List<Long> findByEmployeeByManager(Long managerId, String isActive );
	
	
	
	@Query(value = "SELECT\r\n"
			+ "    end_date AS \"endDate\",\r\n"
			+ "    cycle_name AS \"cycleName\",\r\n"
			+ "\r\n"
			+ "    -- Total count\r\n"
			+ "    mst_cycle_type AS \"cycleType\",\r\n"
			+ "    cycleid AS \"cycleId\",\r\n"
			+ "    year_id AS \"yearId\",\r\n"
			+ "    COUNT(*) AS \"totalCount\",\r\n"
			+ "\r\n"
			+ "    -- Count of completed records (final stage with HR)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"completedCount\",\r\n"
			+ "\r\n"
			+ "    -- Completion percentage\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"completedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Submitted: Employee has submitted (awaiting manager or HOD), excluding completed\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status = 'APPROVED' AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status IN ('INPROCESS', 'APPROVED')\r\n"
			+ "                 AND NOT (status = 'COMPLETED' AND pending_with = 'HR') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedCount\",\r\n"
			+ "\r\n"
			+ "    -- Submitted percentage\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status = 'APPROVED' AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                     AND status IN ('INPROCESS', 'APPROVED')\r\n"
			+ "                     AND NOT (status = 'COMPLETED' AND pending_with = 'HR') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending: In progress or needs action from employee/manager\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingCount\",\r\n"
			+ "\r\n"
			+ "    -- Pending percentage\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                     AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with HOD\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status = 'APPROVED'\r\n"
			+ "                 AND pending_with = 'HOD' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHod\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                     AND status = 'APPROVED'\r\n"
			+ "                     AND pending_with = 'HOD' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with Line Manager\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithLineManager\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithLineManagerPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with Employee\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithEmployee\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with HR\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED' AND pending_with = 'HR') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHr\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED' AND pending_with = 'HR') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHrPercentage\"\r\n"
			+ "\r\n"
			+ "FROM vw_vision_dashboard\r\n"
			+ "WHERE\r\n"
			+ "    department_id IN (?1)\r\n"
			+ "    AND year = ?2\r\n"
			+ "    AND cycleId = ?3\r\n"
			+ "GROUP BY\r\n"
			+ "    end_date,\r\n"
			+ "    cycle_name,mst_cycle_type,cycleid,year_id;\r\n"
			+ "", nativeQuery = true)
	List<CycleResult> countByDepartmentIdIn(List<Long> departments, String kraYear, Long cycleId);
	
	@Query(value = "SELECT\r\n"
			+ "    MAX(end_date) AS \"endDate\",\r\n"
			+ "    cycle_name AS \"cycleName\",\r\n"
			+ "    cycleid AS \"cycleId\",\r\n"
			+ "    year_id AS \"yearId\",\r\n"
			+ "    mst_cycle_type AS \"cycleType\",\r\n"
			+ "    COUNT(*) AS \"totalCount\",\r\n"
			+ "\r\n"
			+ "    -- Count records completed and with HR\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"completedCount\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"completedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Count employee submissions (any stage beyond initial draft)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND (\r\n"
			+ "                (status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "                OR status IN ('APPROVED','SUBMITTED')\r\n"
			+ "            ) THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status IN ('INPROCESS', 'APPROVED','SUBMITTED') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedCount\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND (\r\n"
			+ "                    (status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "                    OR status IN ('APPROVED','SUBMITTED')\r\n"
			+ "                ) THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','SUBMITTED', 'APPROVED') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Count pending actions (manager review or employee rework)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'REJECTED') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingCount\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'REJECTED') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with HOD (approved by manager, awaiting HOD)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status = 'APPROVED'\r\n"
			+ "                 AND pending_with = 'HOD' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHod\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                     AND status = 'APPROVED'\r\n"
			+ "                     AND pending_with = 'HOD' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with Line Manager\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithLineManager\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithLineManagerPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with Employee (needs to act)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithEmployee\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with HR (in progress for KPI, approved for Year/Mid)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED' AND pending_with = 'HR') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHr\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED' AND pending_with = 'HR') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHrPercentage\"\r\n"
			+ "\r\n"
			+ "FROM vw_vision_dashboard\r\n"
			+ "WHERE year IN (:years)\r\n"
			+ "  AND cycleId IN (:cycleIds)\r\n"
			+ "GROUP BY cycle_name, \"year\", cycleid,mst_cycle_type,year_id;\r\n"
			+ "", nativeQuery = true)
	List<CycleResult> countByHR(@Param("years") List<String> years, @Param("cycleIds") List<Long> cycleIds);

	
	
	@Query(value = "SELECT\r\n"
			+ "    end_date AS \"endDate\",\r\n"
			+ "    cycle_name AS \"cycleName\",\r\n"
			+ "    mst_cycle_type AS \"cycleType\",\r\n"
			+ "    cycleid AS \"cycleId\",\r\n"
			+ "    year_id AS \"yearId\",\r\n"
			+ "\r\n"
			+ "    -- Total count\r\n"
			+ "    COUNT(*) AS \"totalCount\",\r\n"
			+ "\r\n"
			+ "    -- Submitted: Employee has submitted (KPI: action pending at employee; Year/Mid: in process or approved)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status IN ('INPROCESS', 'APPROVED','SUBMITTED') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedCount\",\r\n"
			+ "\r\n"
			+ "    -- Completed: Final status reached\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN status = 'COMPLETED' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"completedCount\",\r\n"
			+ "\r\n"
			+ "    -- Pending: Awaiting input (manager review or employee action)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingCount\",\r\n"
			+ "\r\n"
			+ "    -- Submitted percentage\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status IN ('INPROCESS', 'APPROVED') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Completed percentage\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN status = 'COMPLETED' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"completedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending percentage\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                     AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with HOD\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status = 'APPROVED'\r\n"
			+ "                 AND pending_with = 'HOD' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHod\",\r\n"
			+ "    \r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN mst_cycle_type IN (2, 3)\r\n"
			+ "                     AND status = 'APPROVED'\r\n"
			+ "                     AND pending_with = 'HOD' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with Line Manager\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithLineManager\",\r\n"
			+ "    \r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithLineManagerPercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with Employee\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE') THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithEmployee\",\r\n"
			+ "    \r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE') THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- Pending with HR\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE\r\n"
			+ "            WHEN ((mst_cycle_type = 1 AND status = 'INPROGRESS')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
			+ "                 AND pending_with = 'HR' THEN 1\r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHr\",\r\n"
			+ "    \r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE\r\n"
			+ "                WHEN ((mst_cycle_type = 1 AND status = 'INPROGRESS')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
			+ "                     AND pending_with = 'HR' THEN 1\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHrPercentage\"\r\n"
			+ "\r\n"
			+ "FROM vw_vision_dashboard\r\n"
			+ "WHERE id IN (?1)\r\n"
			+ "  AND year = ?2\r\n"
			+ "  AND cycleId = ?3\r\n"
			+ "GROUP BY end_date, cycle_name,mst_cycle_type,cycleid,year_id;\r\n"
			+ "", nativeQuery = true)
	List<CycleResult> countByEmployeeIdIn(List<Long> list, String kraYear, Long cycleId);

	
	
 
	@Query(value = "SELECT "
	        + "    end_date AS \"endDate\", "
	        + "    cycle_name AS \"cycleName\", "
	        + "    mst_cycle_type AS \"cycleType\",\r\n"
	    	+ "    cycleid AS \"cycleId\",\r\n"
	    	+ "    year_id AS \"yearId\",\r\n"
	        + "    status AS \"status\", "
	        + "    pending_with AS \"pendingWith\", "
	        + "    COUNT(*) AS \"totalCount\", "
	        + "    COUNT(CASE "
	        + "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE') "
	        + "              OR (mst_cycle_type IN (2,3) AND status IN ('INPROCESS', 'APPROVED','SUBMITTED')) "
	        + "            THEN 1 END) AS \"submittedCount\", "
	        + "    ROUND(100.0 * COUNT(CASE "
	        + "                    WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE') "
	        + "                      OR (mst_cycle_type IN (2,3) AND status IN ('INPROCESS', 'APPROVED','SUBMITTED')) "
	        + "                    THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"submittedPercentage\", "

	        + "    COUNT(CASE WHEN status = 'COMPLETED' AND pending_with = 'HR' THEN 1 END) AS \"completedCount\", "
	        + "    ROUND(100.0 * COUNT(CASE WHEN status = 'COMPLETED' AND pending_with = 'HR' THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"completedPercentage\", "
	        + "    COUNT(CASE "
	        + "            WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER') "
	        + "              OR (mst_cycle_type IN (2,3) AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED')) "
	        + "            THEN 1 END) AS \"pendingCount\", "
	        + "    ROUND(100.0 * COUNT(CASE "
	        + "                    WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER') "
	        + "                      OR (mst_cycle_type IN (2,3) AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED')) "
	        + "                    THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"pendingPercentage\", "
	        + "    COUNT(CASE WHEN mst_cycle_type IN (2,3) AND status = 'APPROVED' AND pending_with = 'HOD' THEN 1 END) AS \"pendingWithHod\", "
	        + "    ROUND(100.0 * COUNT(CASE WHEN mst_cycle_type IN (2,3) AND status = 'APPROVED' AND pending_with = 'HOD' THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"pendingWithHodPercentage\", "
	        + "    COUNT(CASE "
	        + "            WHEN ((mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED')) "
	        + "                  OR (mst_cycle_type IN (2,3) AND status = 'INPROCESS')) "
	        + "                 AND pending_with = 'MANAGER' "
	        + "            THEN 1 END) AS \"pendingWithLineManager\", "
	        + "    ROUND(100.0 * COUNT(CASE "
	        + "                    WHEN ((mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED')) "
	        + "                          OR (mst_cycle_type IN (2,3) AND status = 'INPROCESS')) "
	        + "                          AND pending_with = 'MANAGER' "
	        + "                    THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"pendingWithLineManagerPercentage\", "
	        + "    COUNT(CASE "
	        + "            WHEN ((mst_cycle_type = 1 AND status = 'INACTION') "
	        + "                  OR (mst_cycle_type IN (2,3) AND status = 'INCOMPLETE')) "
	        + "                 AND pending_with = 'EMPLOYEE' "
	        + "            THEN 1 END) AS \"pendingWithEmployee\", "
	        + "    ROUND(100.0 * COUNT(CASE "
	        + "                    WHEN ((mst_cycle_type = 1 AND status = 'INACTION') "
	        + "                          OR (mst_cycle_type IN (2,3) AND status = 'INCOMPLETE')) "
	        + "                          AND pending_with = 'EMPLOYEE' "
	        + "                    THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"pendingWithEmployeePercentage\", "
	        + "    COUNT(CASE "
	        + "            WHEN ((mst_cycle_type = 1 AND status = 'INPROGRESS') "
	        + "                  OR (mst_cycle_type IN (2,3) AND status = 'APPROVED')) "
	        + "                 AND pending_with = 'HR' "
	        + "            THEN 1 END) AS \"pendingWithHr\", "
	        + "    ROUND(100.0 * COUNT(CASE "
	        + "                    WHEN ((mst_cycle_type = 1 AND status = 'INPROGRESS') "
	        + "                          OR (mst_cycle_type IN (2,3) AND status = 'APPROVED')) "
	        + "                          AND pending_with = 'HR' "
	        + "                    THEN 1 END) / NULLIF(COUNT(*), 0), 2) AS \"pendingWithHrPercentage\" "

	        + "FROM vw_vision_dashboard "
	        + "WHERE year = ?1 "
	        + "  AND cycleId = ?2 "
	        + "  AND id = ?3 "
	        + "GROUP BY end_date, cycle_name, status, pending_with,mst_cycle_type,cycleid,year_id "
	        + "ORDER BY cycle_name, status, pending_with ",
	        nativeQuery = true)
	List<CycleResult> getEmployeeDetails(String year, Long cycleId, Long empId);


	@Query(value = "select tmky.label ,* from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id JOIN tbl_mst_kra_year tmky ON tmky.id = ttk.year where ttk.employee_id =?1 and ttk.is_active =?2 ", nativeQuery = true)
	public List<Kra> findByEmployeeIdAndIsActive(Long loggedInEmpId, String name, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk where ttk.employee_id =?1 and is_active =?2 ", nativeQuery = true)
	public long countByEmployeeIdAndIsActive(Long loggedInEmpId, String string);
	
	
	@Query(value = "SELECT end_date FROM tbl_mst_kra_cycle_calender WHERE role_id = ?1 AND cycle_id = ?2 AND is_active = ?3", nativeQuery = true)
	public Date getEndDateByRole(Long roleId, Long cycleId, String name);

	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and status =?8", nativeQuery = true)
	public List<Kra> findByDeptIdInAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String status, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and status=?8 ", nativeQuery = true)
	public long countByDeptIdInAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String status);

	boolean existsByEmployeeAndCycleIdAndKraYear(Employee employee, KraCycle cycle, KraYear kraYear);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 ", nativeQuery = true)
	public List<Kra> findByEmployeeByMangerAndYear(Long loggedInEmpId, String name, String name2, String year,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id  where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4", nativeQuery = true)
	public long countByEmployeeByMangerAndYear(Long loggedInEmpId, String name, String name2, String year);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 ", nativeQuery = true)
	public List<Kra> findByEmployeeByMangerAndCycle(Long loggedInEmpId, String name, String name2, String cycle,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id  where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4", nativeQuery = true)
	public long countByEmployeeByMangerAndCycle(Long loggedInEmpId, String name, String name2, String cycle);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  "
			+ " JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id"
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5", nativeQuery = true)
	public List<Kra> findByEmployeeByMangerAndYearAndCycle(Long loggedInEmpId, String name, String name2, String year,
			String cycle, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id "
			+ " JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5", nativeQuery = true)
	public long countByEmployeeByMangerAndYearAndCycle(Long loggedInEmpId, String name, String name2, String year,
			String cycle);

	
	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and ttk.department = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndDeptAndGrade(Long loggedInEmpId, String name, String name2,
			String trim, Long deptid, String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and ttk.department = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndDeptAndGrade(Long loggedInEmpId, String name, String name2,
			String trim, Long deptid, String trim2);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id"
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and ttk.department = ?5 ", nativeQuery = true)	
	public List<Kra> findByEmployeeByManagerAndYearAndDept(Long loggedInEmpId, String name, String name2, String trim,
			Long deptid, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and ttk.department = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndDept(Long loggedInEmpId, String name, String name2, String trim,
			Long deptid);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndGrade(Long loggedInEmpId, String name, String name2, String trim,
			String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndGrade(Long loggedInEmpId, String name, String name2, String trim,
			String grade);

	
	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndCycleAndDeptAndGrade(Long loggedInEmpId, String name, String name2,
			String cycle, Long deptid, String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public long countByEmployeeByManagerAndCycleAndDeptAndGrade(Long loggedInEmpId, String name, String name2,
			String cycle, Long deptid, String trim2);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndCycleAndDept(Long loggedInEmpId, String name, String name2, String trim,
			Long deptid, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndCycleAndDept(Long loggedInEmpId, String name, String name2, String trim,
			Long deptid);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id  "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndCycleAndGrade(Long loggedInEmpId, String name, String name2, String cycle,
			String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmkc.cycle_name = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndCycleAndGrade(Long loggedInEmpId, String name, String name2, String cycle,
			String grade);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6 and tcpd.grade = ?7 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndCycleAndDeptAndGrade(Long loggedInEmpId, String name,
			String name2, String year, String cycle, Long deptid, String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6 and tcpd.grade = ?7 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndCycleAndDeptAndGrade(Long loggedInEmpId, String name, String name2,
			String year, String cyle, Long deptid, String grade);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndCycleAndDept(Long loggedInEmpId, String name, String name2,
			String year, String cycle, Long deptid, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndCycleAndDept(Long loggedInEmpId, String name, String name2,
			String year, String cycle, Long deptid);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5  and tcpd.grade = ?6 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndCycleAndGrade(Long loggedInEmpId, String name, String name2,
			String year, String cycle, String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndCycleAndGrade(Long loggedInEmpId, String name, String name2,
			String year, String cycle, String grade);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and ttk.department = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndDeptAndGrade(Long loggedInEmpId, String name, String name2, Long deptid,
			String grade, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and ttk.department = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndDeptAndGrade(Long loggedInEmpId, String name, String name2, Long deptid,
			String grade);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and ttk.department = ?4 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndDept(Long loggedInEmpId, String name, String name2, Long deptid,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and ttk.department = ?4 ", nativeQuery = true)
	public long countByEmployeeByManagerAndDept(Long loggedInEmpId, String name, String name2, Long deptid);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tcpd.grade = ?4 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndGrade(Long loggedInEmpId, String name, String name2, String grade,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.status != ?3 and tcpd.grade = ?4 ", nativeQuery = true)
	public long countByEmployeeByManagerAndGrade(Long loggedInEmpId, String name, String name2, String grade);

	
	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9  and tcpd.grade = ?10 and wf.status =?11  ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndCycleAndGradeAndStatus(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String cycle, String grade, String status,
			Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9  and tcpd.grade = ?10 and wf.status =?11  ", nativeQuery = true)
	public long countByDeptAndYearAndCycleAndGradeAndStatus(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String cycle, String grade, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9  and tcpd.grade = ?10  ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndCycleAndGrade(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String cycle, String grade, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9  and tcpd.grade = ?10 ", nativeQuery = true)
	public long countByDeptAndYearAndCycleAndGrade(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String cycle, String grade);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9  and wf.status = ?10  ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndCycleAndStatus(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String cycle, String status, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9  and wf.status = ?10  ", nativeQuery = true)
	public long countByDeptAndYearAndCycleAndStatus(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String cycle, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tcpd.grade = ?9  and wf.status = ?10  ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndGradeAndStatus(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String year, String grade, String status, Pageable pageable);
	
	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tcpd.grade = ?9  and wf.status = ?10  ", nativeQuery = true)
	public long countByDeptAndYearAndGradeAndStatus(Object[] array, String name, String name2, String name3,
			String name4, String name5, String name6, String trim, String trim2, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9 ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndCycle(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, String cycle, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tmkc.cycle_name = ?9 ", nativeQuery = true)
	public long countByDeptAndYearAndCycle(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, String cycle);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and tcpd.grade = ?9 ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndGrade(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, String grade, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and wf.status = ?9 ", nativeQuery = true)
	public long countByDeptAndYearAndGrade(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, String grade);

	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and wf.status = ?9 ", nativeQuery = true)
	public List<Kra> findByDeptAndYearAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, String status, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 and wf.status = ?9 ", nativeQuery = true)
	public long countByDeptAndYearAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmkc.cycle_name = ?8 and tcpd.grade = ?9 ", nativeQuery = true)
	public List<Kra> findByDeptAndCycleAndGrade(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String cycle, String grade, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmkc.cycle_name = ?8 and tcpd.grade = ?9 ", nativeQuery = true)
	public long countByDeptAndCycleAndGrade(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String cycle, String grade);

	

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmkc.cycle_name  = ?8 and wf.status = ?9 ", nativeQuery = true)
	public List<Kra> findByDeptAndCycleAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String cycle, String status, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmkc.cycle_name  = ?8 and wf.status = ?9 ", nativeQuery = true)
	public long countByDeptAndCycleAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String cycle, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tcpd.grade  = ?8 and wf.status = ?9 ", nativeQuery = true)
	public List<Kra> findByDeptAndGradeAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String grade, String status, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tcpd.grade  = ?8 and wf.status = ?9 ", nativeQuery = true)
	public long countByDeptAndGradeAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String grade, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 ", nativeQuery = true)
	public List<Kra> findByDeptAndYear(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String year, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmky.year = ?8 ", nativeQuery = true)
	public long countByDeptAndYear(Object[] array, String name, String name2, String name3, String name4, String name5,
			String name6, String year);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmkc.cycle_name = ?8 ", nativeQuery = true)
	public List<Kra> findByDeptAndCycle(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String cycle, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tmkc.cycle_name = ?8 ", nativeQuery = true)
	public long countByDeptAndCycle(Object[] array, String name, String name2, String name3, String name4, String name5,
			String name6, String cycle);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and wf.status = ?8 ", nativeQuery = true)
	public List<Kra> findByDeptAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String status, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and wf.status = ?8 ", nativeQuery = true)
	public long countByDeptAndStatus(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String status);

	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tcpd.grade = ?8 ", nativeQuery = true)
	public List<Kra> findByDeptAndGrade(Object[] array, String name, String name2, String name3, String name4,
			String name5, String name6, String grade, Pageable pageable);

	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id JOIN tbl_mst_kra_year tmky ON kra.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =kra.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =kra.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ "	where  department in (?1) and (wf.status = ?2 OR wf.status = ?3 OR wf.status = ?4 OR wf.status = ?5 OR wf.status = ?6)  and kra.is_active =?7 and tcpd.grade = ?8 ", nativeQuery = true)
	public long countByDeptAndGrade(Object[] array, String name, String name2, String name3, String name4, String name5,
			String name6, String grade);

	@Query(value = "select tmky.label ,* from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id JOIN tbl_mst_kra_year tmky ON tmky.id = ttk.year where ttk.employee_id =?1 and ttk.is_active =?2 and tmky.label = ?3 and ttkw.status=?4 ", nativeQuery = true)
	public List<Kra> findByEmployeeIdAndIsActiveAndYearAndStatus(Long loggedInEmpId, String name, String year,
			String name2, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id where ttk.employee_id =?1 and is_active =?2 and year=?3 and ttkw.status=?4 ", nativeQuery = true)
	public long countByEmployeeIdAndIsActiveAndYearAndStatus(Long loggedInEmpId, String name, String year,
			String name2);

	@Query(value = "select * from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id where ttk.employee_id =?1 and ttk.is_active =?2 and ttkw.status=?3", nativeQuery = true)
	public List<Kra> findByEmployeeIdAndIsActiveAndStatus(Long loggedInEmpId, String name, String name2,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk JOIN tbl_trn_kra_wf ttkw ON ttk.id = ttkw.kra_id JOIN tbl_mst_kra_year tmky ON tmky.id = ttk.year where ttk.employee_id =?1 and ttk.is_active =?2 and ttkw.status=?3 ", nativeQuery = true)
	public long countByEmployeeIdAndIsActiveAndStatus(Long loggedInEmpId, String name, String name2);

	
	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndCycleAndDeptAndGradeByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, Long deptid, String trim2, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 and tcpd.grade = ?6 ", nativeQuery = true)
	public long countByEmployeeByManagerAndCycleAndDeptAndGradeByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, Long deptid, String trim2);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndCycleAndDeptByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, Long deptid, Pageable pageable);

	
	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndCycleAndDeptByKpisubmission(Long loggedInEmpId, String name, String name2,
			String trim, Long deptid);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id  "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndCycleAndGradeByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, String trim2, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndCycleAndGradeByKpisubmission(Long loggedInEmpId, String name, String name2,
			String trim, String trim2);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 ", nativeQuery = true)
	public List<Kra> findByEmployeeByMangerAndCycleByKpisubmission(Long loggedInEmpId, String name, String name2,
			String trim, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id  where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4", nativeQuery = true)
	public long countByEmployeeByMangerAndCycleByKpisubmission(Long loggedInEmpId, String name, String name2,
			String trim);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6 and tcpd.grade = ?7 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndCycleAndDeptAndGradeByKpisubmission(Long loggedInEmpId,
			String name, String name2, String trim, String trim2, Long deptid, String trim3, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6 and tcpd.grade = ?7 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndCycleAndDeptAndGradeByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, String trim2, Long deptid, String trim3);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id"
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5 and ttk.department = ?6", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndCycleAndDeptByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, String trim2, Long deptid, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and ttk.department = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndCycleAndDeptByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, String trim2, Long deptid);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id  "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public List<Kra> findByEmployeeByManagerAndYearAndCycleAndGradeByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, String trim2, String trim3, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id "
			+ " join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id "
			+ " join tbl_employee te on te.id =ttk.employee_id "
			+ " join tbl_candidate_professional_detail tcpd on tcpd.candidate_id =te.candidate_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmkc.cycle_name = ?4 and tcpd.grade = ?5 ", nativeQuery = true)
	public long countByEmployeeByManagerAndYearAndCycleAndGradeByKpisubmission(Long loggedInEmpId, String name,
			String name2, String trim, String trim2, String trim3);

	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  "
			+ " JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id"
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5", nativeQuery = true)
	public List<Kra> findByEmployeeByMangerAndYearAndCycleByKpisubmission(Long loggedInEmpId, String name, String name2,
			String trim, String trim2, Pageable pageable);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id "
			+ " JOIN tbl_mst_kra_year tmky ON ttk.year = tmky.id join tbl_mst_kra_cycle tmkc on tmkc.id =ttk.mst_kra_cycle_id where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 and tmky.year = ?4 and tmkc.cycle_name = ?5", nativeQuery = true)
	public long countByEmployeeByMangerAndYearAndCycleByKpisubmission(Long loggedInEmpId, String name, String name2,
			String trim, String trim2);

	
	List<Kra> findByEmployeeAndKraYearAndIsActive(Employee employee, KraYear kraYear, String isActive);

	
	

	@Query(value = "SELECT * FROM tbl_trn_kra WHERE mst_kra_cycle_id = :cycleId", nativeQuery = true)
	List<Kra> findByCycleId(@Param("cycleId") Long cycleId);

	@Modifying
	@Query(value="DELETE FROM tbl_trn_kra WHERE mst_kra_cycle_id = :cycleId", nativeQuery = true)
	void deleteByCycleId(@Param("cycleId") Long cycleId);
	
	
	@Query(value = "SELECT\r\n"
		    + "    MAX(end_date) AS \"endDate\",\r\n"
		    + "    cycle_name AS \"cycleName\",\r\n"
		    + "    mst_cycle_type AS \"cycleType\",\r\n"
		    + "    cycleId AS \"cycleId\",\r\n"
		    + "    year_id AS \"yearId\",\r\n"
		    + "    year AS \"year\",\r\n"
		    + "    COUNT(*) AS \"totalCount\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Completed Count & Percentage\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "              OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"completedCount\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"completedPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Pending Count & Percentage\r\n"
		    + "    COUNT(\r\n"
		    + "       CASE\r\n"
		    + "    WHEN (\r\n"
		    + "            mst_cycle_type = 1 \r\n"
		    + "            AND (\r\n"
		    + "                    (status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
		    + "                    OR status = 'SUBMITTED'\r\n"
		    + "                )\r\n"
		    + "         )\r\n"
		    + "         OR (\r\n"
		    + "            mst_cycle_type IN (2, 3)\r\n"
		    + "            AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED', 'SUBMITTED')\r\n"
		    + "         )\r\n"
		    + "    THEN 1\r\n"
		    + "END\r\n"
		    + "    ) AS \"pendingCount\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) \r\n"
		    + "                      AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED'))\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"pendingPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Submitted Count & Percentage\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
		    + "              OR (mst_cycle_type IN (2, 3) \r\n"
		    + "                  AND status IN ('INPROCESS', 'APPROVED','SUBMITTED') \r\n"
		    + "                  AND NOT (status = 'COMPLETED' AND pending_with = 'HR'))\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"submittedCount\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) \r\n"
		    + "                      AND status IN ('INPROCESS', 'APPROVED','SUBMITTED') \r\n"
		    + "                      AND NOT (status = 'COMPLETED' AND pending_with = 'HR'))\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"submittedPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Submitted by Employee\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "              OR (mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','COMPLETED','SUBMITTED') AND pending_with IN ('MANAGER','HOD','HR'))\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"submittedByEmployee\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','COMPLETED','SUBMITTED') AND pending_with IN ('MANAGER','HOD','HR'))\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"submittedEmployeePercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Pending with Employee\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
		    + "              OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE')\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"pendingWithEmployee\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE')\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"pendingWithEmployeePercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Pending with Line Manager\r\n"
		    + "    COUNT(\r\n"
		    + "       CASE\r\n"
		    + "    WHEN \r\n"
		    + "        (\r\n"
		    + "            mst_cycle_type = 1 \r\n"
		    + "            AND \r\n"
		    + "            (\r\n"
		    + "                (status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
		    + "                OR (status = 'SUBMITTED' AND pending_with = 'DIVISIONHEAD')\r\n"
		    + "            )\r\n"
		    + "        )\r\n"
		    + "        OR \r\n"
		    + "        (\r\n"
		    + "            mst_cycle_type IN (2, 3)\r\n"
		    + "            AND \r\n"
		    + "            (\r\n"
		    + "                (status = 'INPROCESS' AND pending_with = 'MANAGER')\r\n"
		    + "                OR (status = 'SUBMITTED' AND pending_with = 'DIVISIONHEAD')\r\n"
		    + "            )\r\n"
		    + "        )\r\n"
		    + "    THEN 1\r\n"
		    + "END\r\n"
		    + "    ) AS \"pendingWithLineManager\",\r\n"
		    + "   ROUND(\r\n"
		    + "    100.0 * COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            -- Cycle Type 1 Conditions\r\n"
		    + "            WHEN mst_cycle_type = 1 \r\n"
		    + "                 AND (\r\n"
		    + "                        (status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
		    + "                        OR (status = 'SUBMITTED' AND pending_with = 'DIVISIONHEAD')\r\n"
		    + "                     )\r\n"
		    + "            THEN 1\r\n"
		    + "\r\n"
		    + "            -- Cycle Type 2 & 3 Conditions\r\n"
		    + "            WHEN mst_cycle_type IN (2, 3)\r\n"
		    + "                 AND (\r\n"
		    + "                        (status = 'INPROCESS' AND pending_with = 'MANAGER')\r\n"
		    + "                        OR (status = 'SUBMITTED' AND pending_with = 'DIVISIONHEAD')\r\n"
		    + "                     )\r\n"
		    + "            THEN 1\r\n"
		    + "        END\r\n"
		    + "    ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + ") AS \"pendingWithLineManagerPercentage\",\r\n"
		    + "    -- ✅ Submitted by Line Manager\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN (mst_cycle_type = 1 AND status IN('SUBMITTED','APPROVED','INACTION'))\r\n"
		    + "              OR (mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED') AND pending_with IN ('HOD','HR'))\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"submittedByLineManager\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status IN('SUBMITTED','APPROVED','INACTION'))\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED') AND pending_with IN ('HOD','HR'))\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"submittedByLineManagerPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Pending with HOD\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN mst_cycle_type IN (2, 3) \r\n"
		    + "                 AND status = 'APPROVED' \r\n"
		    + "                 AND pending_with = 'HOD'\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"pendingWithHod\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN mst_cycle_type IN (2, 3) \r\n"
		    + "                     AND status = 'APPROVED' \r\n"
		    + "                     AND pending_with = 'HOD'\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"pendingWithHodPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Submitted by HOD\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN mst_cycle_type IN (2, 3) \r\n"
		    + "                 AND status IN ('APPROVED','COMPLETED') \r\n"
		    + "                 AND pending_with = 'HR'\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"submittedByHod\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN mst_cycle_type IN (2, 3) \r\n"
		    + "                     AND status IN ('APPROVED','COMPLETED') \r\n"
		    + "                     AND pending_with = 'HR'\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"submittedHodPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Pending with HR\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN ((mst_cycle_type = 1 AND status = 'SUBMITTED')\r\n"
		    + "               OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
		    + "               AND pending_with = 'HR'\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"pendingWithHr\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN ((mst_cycle_type = 1 AND status = 'SUBMITTED')\r\n"
		    + "                   OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
		    + "                   AND pending_with = 'HR'\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"pendingWithHrPercentage\",\r\n"
		    + "\r\n"
		    + "    -- ✅ Submitted by HR\r\n"
		    + "    COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN (mst_cycle_type = 1 AND status = 'APPROVED' AND pending_with = 'MANAGER')\r\n"
		    + "              OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "            THEN 1 \r\n"
		    + "        END\r\n"
		    + "    ) AS \"submittedHr\",\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(\r\n"
		    + "            CASE \r\n"
		    + "                WHEN (mst_cycle_type = 1 AND status = 'APPROVED' AND pending_with = 'MANAGER')\r\n"
		    + "                  OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
		    + "                THEN 1 \r\n"
		    + "            END\r\n"
		    + "        ) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS \"submittedHrPercentage\"\r\n"
		    + "\r\n"
		    + "FROM vw_vision_dashboard\r\n"
		    + "WHERE cycleid = ?1\r\n"
		    + "  AND year = ?2\r\n"
		    + "GROUP BY cycle_name, year, cycleId, mst_cycle_type, year_id;\r\n"
		    + "", nativeQuery = true)
		List<CycleResult> countByHRForDonut(Long cycleId, String year);


	
	@Query(value = "SELECT\r\n"
			+ "    MAX(end_date) AS \"endDate\",\r\n"
			+ "    cycle_name AS \"cycleName\",\r\n"
			+ "    cycleid AS \"cycleId\",\r\n"
			+ "    year_id AS \"yearId\",\r\n"
			+ "    mst_cycle_type AS \"cycleType\",\r\n"
			+ "    year AS \"year\",\r\n"
			+ "    COUNT(*) AS \"totalCount\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Completed Count & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"completedCount\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"completedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted Count & Percentage (Employee has initiated)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) \r\n"
			+ "                  AND status IN ('INPROCESS', 'APPROVED','SUBMITTED') \r\n"
			+ "                  AND NOT (status = 'COMPLETED' AND pending_with = 'HR'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedCount\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) \r\n"
			+ "                      AND status IN ('INPROCESS', 'APPROVED','SUBMITTED') \r\n"
			+ "                      AND NOT (status = 'COMPLETED' AND pending_with = 'HR'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending Count & Percentage (Overall pending)\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN ('APPROVED', 'INPROGRESS') AND pending_with = 'MANAGER')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) \r\n"
			+ "                  AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingCount\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('APPROVED', 'INPROGRESS') AND pending_with = 'MANAGER')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) \r\n"
			+ "                      AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'INACTION', 'REJECTED'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending with Employee & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithEmployee\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending with Line Manager & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithLineManager\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INPROCESS' AND pending_with = 'MANAGER')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithLineManagerPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending with HOD & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status = 'APPROVED' AND pending_with = 'HOD'\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHod\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status = 'APPROVED' AND pending_with = 'HOD'\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending with HR & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN ((mst_cycle_type = 1 AND status = 'INPROGRESS')\r\n"
			+ "               OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
			+ "               AND pending_with = 'HR'\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHr\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN ((mst_cycle_type = 1 AND status = 'INPROGRESS')\r\n"
			+ "                   OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
			+ "                   AND pending_with = 'HR'\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"pendingWithHrPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted by Employee & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','COMPLETED') AND pending_with IN ('MANAGER','HOD','HR'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedByEmployee\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','COMPLETED') AND pending_with IN ('MANAGER','HOD','HR'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted by Line Manager & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED') AND pending_with IN ('HOD','HR'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedByLineManager\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INPROGRESS' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED') AND pending_with IN ('HOD','HR'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedByLineManagerPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted by HOD & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED') AND pending_with = 'HR'\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedByHod\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED') AND pending_with = 'HR'\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted to HR & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'APPROVED' AND pending_with = 'MANAGER')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedHr\",\r\n"
			+ "\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'APPROVED' AND pending_with = 'MANAGER')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), 2\r\n"
			+ "    ) AS \"submittedHrPercentage\"\r\n"
			+ "\r\n"
			+ "FROM vw_vision_dashboard\r\n"
			+ "WHERE\r\n"
			+ "    year = ?1\r\n"
			+ "    AND cycleId = ?2\r\n"
			+ "    AND department_id IN (?3)\r\n"
			+ "GROUP BY\r\n"
			+ "    cycle_name,\r\n"
			+ "    year,\r\n"
			+ "    mst_cycle_type,\r\n"
			+ "    cycleid,year_id;\r\n"
			+ "", nativeQuery = true)
	 List<CycleResult> countByDepartmentIdInForDonut( String year, Long cycleId,List<Long> departments);

	
	
	@Query(value = "SELECT\r\n"
			+ "    MAX(end_date) AS \"endDate\",\r\n"
			+ "    cycle_name AS \"cycleName\",\r\n"
			+ "    cycleid AS \"cycleId\",\r\n"
			+ "    year_id AS \"yearId\",\r\n"
			+ "    mst_cycle_type AS \"cycleType\",\r\n"
			+ "    year AS \"year\",\r\n"
			+ "    COUNT(*) AS \"totalCount\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Completed Count & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"completedCount\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"completedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending Count & Percentage\r\n"
			+ "COUNT(\r\n"
		    + "        CASE \r\n"
		    + "            WHEN \r\n"
			+ "                (mst_cycle_type = 1 \r\n"
		    + "                 AND (\r\n"
			+ "                        (status IN ('APPROVED', 'INPROGRESS') AND pending_with = 'MANAGER')\r\n"
			+ "                        OR status = 'SUBMITTED'\r\n"
		    + "                     )\r\n"
		    + "                )\r\n"
			+ "                OR \r\n"
			+ "                (mst_cycle_type IN (2, 3)\r\n"
			+ "                 AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'SUBMITTED', 'INACTION', 'REJECTED')\r\n"
			+ "                )\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
		    + "    ) AS \"pendingCount\",\r\n"
		    + " ROUND(\r\n"
		            + "        100.0 * COUNT(\r\n"
		            + "            CASE \r\n"
		            + "                WHEN \r\n"
		            + "                    (mst_cycle_type = 1 \r\n"
		            + "                     AND (\r\n"
		            + "                            (status IN ('APPROVED', 'INPROGRESS') AND pending_with = 'MANAGER')\r\n"
		            + "                            OR status = 'SUBMITTED'\r\n"
		            + "                         )\r\n"
		            + "                    )\r\n"
		            + "                    OR \r\n"
		            + "                    (mst_cycle_type IN (2, 3)\r\n"
		            + "                     AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'INPROGRESS', 'SUBMITTED', 'INACTION', 'REJECTED')\r\n"
		            + "                    )\r\n"
		            + "                THEN 1 \r\n"
		            + "            END\r\n"
		            + "        ) / NULLIF(COUNT(*), 0),\r\n"
		            + "        2\r\n"
		            + "    ) AS \"pendingPercentage\",\r\n"
		            + "\r\n"

			+ "    -- ✅ Pending with Employee & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithEmployee\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'INCOMPLETE' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"pendingWithEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending with HOD & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) \r\n"
			+ "                 AND status = 'APPROVED' \r\n"
			+ "                 AND pending_with = 'HOD'\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHod\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) \r\n"
			+ "                     AND status = 'APPROVED' \r\n"
			+ "                     AND pending_with = 'HOD'\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"pendingWithHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Pending with HR & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN ((mst_cycle_type = 1 AND status = 'SUBMITTED')\r\n"
			+ "               OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
			+ "               AND pending_with = 'HR'\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"pendingWithHr\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN ((mst_cycle_type = 1 AND status = 'SUBMITTED')\r\n"
			+ "                   OR (mst_cycle_type IN (2, 3) AND status = 'APPROVED'))\r\n"
			+ "                   AND pending_with = 'HR'\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"pendingWithHrPercentage\",\r\n"
			+ "\r\n"
			+ " -- ✅ Pending with Line Manager & Percentage\r\n"
					   + " COUNT(\r\n"
			        + "        CASE \r\n"
			        + "            WHEN \r\n"
			        + "                (mst_cycle_type = 1 \r\n"
			        + "                 AND (\r\n"
			        + "                        (status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			        + "                        OR status = 'SUBMITTED'\r\n"
			        + "                     )\r\n"
			        + "                )\r\n"
			        + "                OR \r\n"
			        + "                (mst_cycle_type IN (2, 3) \r\n"
			        + "                 AND (\r\n"
			        + "                        (status = 'INPROCESS' AND pending_with = 'MANAGER')\r\n"
			        + "                        OR (status = 'SUBMITTED' AND pending_with = 'DIVISIONHEAD')\r\n"
			        + "                     )\r\n"
			        + "                )\r\n"
			        + "            THEN 1 \r\n"
			        + "        END\r\n"
			        + "    ) AS \"pendingWithLineManager\",\r\n"
			        
			        + "    ROUND(\r\n"
			        + "        100.0 * COUNT(\r\n"
			        + "            CASE \r\n"
			        + "                WHEN \r\n"
			        + "                    (mst_cycle_type = 1 \r\n"
			        + "                     AND (\r\n"
			        + "                            (status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER')\r\n"
			        + "                            OR status = 'SUBMITTED'\r\n"
			        + "                         )\r\n"
			        + "                    )\r\n"
			        + "                    OR \r\n"
			        + "                    (mst_cycle_type IN (2, 3) \r\n"
			        + "                     AND (\r\n"
			        + "                            (status = 'INPROCESS' AND pending_with = 'MANAGER')\r\n"
			        + "                            OR (status = 'SUBMITTED' AND pending_with = 'DIVISIONHEAD')\r\n"
			        + "                         )\r\n"
			        + "                    )\r\n"
			        + "                THEN 1 \r\n"
			        + "            END\r\n"
			        + "        ) / NULLIF(COUNT(*), 0),\r\n"
			        + "        2\r\n"
			        + "    ) AS \"pendingWithLineManagerPercentage\",\r\n"
			        + "\r\n"

			+ "    -- ✅ Submitted Count & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) \r\n"
			+ "                  AND status IN ('INPROCESS', 'APPROVED') \r\n"
			+ "                  AND NOT (status = 'COMPLETED' AND pending_with = 'HR'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedCount\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'INACTION' AND pending_with = 'EMPLOYEE')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) \r\n"
			+ "                      AND status IN ('INPROCESS', 'APPROVED') \r\n"
			+ "                      AND NOT (status = 'COMPLETED' AND pending_with = 'HR'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"submittedPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted by Employee & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','COMPLETED','SUBMITTED') AND pending_with IN ('MANAGER','HOD','HR'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedByEmployee\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','COMPLETED','SUBMITTED') AND pending_with IN ('MANAGER','HOD','HR'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"submittedEmployeePercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted by HOD & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN mst_cycle_type IN (2, 3) \r\n"
			+ "                 AND status IN ('APPROVED','COMPLETED') \r\n"
			+ "                 AND pending_with = 'HR'\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedByHod\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN mst_cycle_type IN (2, 3) \r\n"
			+ "                     AND status IN ('APPROVED','COMPLETED') \r\n"
			+ "                     AND pending_with = 'HR'\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"submittedHodPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted by Line Manager & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN( 'SUBMITTED','INACTION','APPROVED'))\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED','SUBMITTED'))\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedByLineManager\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('SUBMITTED','APPROVED','INACTION'))\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status IN ('APPROVED','COMPLETED','SUBMITTED'))\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"submittedByLineManagerPercentage\",\r\n"
			+ "\r\n"
			+ "    -- ✅ Submitted to HR & Percentage\r\n"
			+ "    COUNT(\r\n"
			+ "        CASE \r\n"
			+ "            WHEN (mst_cycle_type = 1 AND status IN ('INACTION', 'APPROVED'))\r\n"
			+ "              OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "            THEN 1 \r\n"
			+ "        END\r\n"
			+ "    ) AS \"submittedHr\",\r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * COUNT(\r\n"
			+ "            CASE \r\n"
			+ "                WHEN (mst_cycle_type = 1 AND status IN ('APPROVED','INACTION'))\r\n"
			+ "                  OR (mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR')\r\n"
			+ "                THEN 1 \r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0),\r\n"
			+ "        2\r\n"
			+ "    ) AS \"submittedHrPercentage\"\r\n"
			+ "\r\n"
			+ "FROM vw_vision_dashboard\r\n"
			+ "WHERE \r\n"
			+ "    id IN (?1) \r\n"
			+ "    AND year = ?2 \r\n"
			+ "    AND cycleid = ?3\r\n"
			+ "GROUP BY \r\n"
			+ "    cycle_name, \r\n"
			+ "    year, \r\n"
			+ "    mst_cycle_type, \r\n"
			+ "    cycleid,year_id;\r\n"
			+ ""
			+ "", nativeQuery = true)
	 List<CycleResult> countByEmployeeIdInForDonut(List<Long> empIds, String year, Long cycleId);


	
	@Query(value = "SELECT \r\n"
			+ "    ROUND(\r\n"
			+ "        100.0 * SUM(\r\n"
			+ "            CASE \r\n"
			+ "                -- ✅ Employee pending (0% completion)\r\n"
			+ "                WHEN (\r\n"
			+ "                    (mst_cycle_type IN (3, 2) \r\n"
			+ "                     AND status = 'INCOMPLETE' \r\n"
			+ "                     AND pending_with = 'EMPLOYEE')\r\n"
			+ "                ) THEN 0.00\r\n"
			+ "\r\n"
			+ "                -- ✅ Manager pending (25% completion)\r\n"
			+ "                WHEN (\r\n"
			+ "                    (mst_cycle_type = 1 \r\n"
			+ "                     AND status = 'INPROGRESS' \r\n"
			+ "                     AND pending_with = 'MANAGER') \r\n"
			+ "                    OR\r\n"
			+ "                    (mst_cycle_type IN (3, 2) \r\n"
			+ "                     AND status = 'INPROCESS' \r\n"
			+ "                     AND pending_with = 'MANAGER')\r\n"
			+ "                ) THEN 0.25\r\n"
			+ "\r\n"
			+ "                -- ✅ HOD pending / HR in KPI Submission (50% completion)\r\n"
			+ "                WHEN (\r\n"
			+ "                    (mst_cycle_type IN (3, 2) \r\n"
			+ "                     AND status = 'APPROVED' \r\n"
			+ "                     AND pending_with = 'HOD') \r\n"
			+ "                    OR\r\n"
			+ "                    (mst_cycle_type = 1 \r\n"
			+ "                     AND status = 'INPROGRESS' \r\n"
			+ "                     AND pending_with = 'HR')\r\n"
			+ "                ) THEN 0.50\r\n"
			+ "\r\n"
			+ "                -- ✅ HR pending / Employee in KPI Submission (75% completion)\r\n"
			+ "                WHEN (\r\n"
			+ "                    (mst_cycle_type IN (3, 2) \r\n"
			+ "                     AND status = 'APPROVED' \r\n"
			+ "                     AND pending_with = 'HR') \r\n"
			+ "                    OR\r\n"
			+ "                    (mst_cycle_type = 1 \r\n"
			+ "                     AND status = 'INACTION' \r\n"
			+ "                     AND pending_with = 'EMPLOYEE')\r\n"
			+ "                ) THEN 0.75\r\n"
			+ "\r\n"
			+ "                -- ✅ Completed (100% completion)\r\n"
			+ "                WHEN (\r\n"
			+ "                    (mst_cycle_type IN (3, 2) \r\n"
			+ "                     AND status = 'COMPLETED' \r\n"
			+ "                     AND pending_with = 'HR') \r\n"
			+ "                    OR\r\n"
			+ "                    (mst_cycle_type = 1 \r\n"
			+ "                     AND status = 'COMPLETED' \r\n"
			+ "                     AND pending_with = 'HR')\r\n"
			+ "                ) THEN 1.00\r\n"
			+ "\r\n"
			+ "                ELSE 0\r\n"
			+ "            END\r\n"
			+ "        ) / NULLIF(COUNT(*), 0), \r\n"
			+ "    2) AS \"employeePercentage\"\r\n"
			+ "FROM vw_vision_dashboard\r\n"
			+ "WHERE \r\n"
			+ "    year = :year\r\n"
			+ "    AND cycleid = :cycleId\r\n"
			+ "    AND id = :loggedInEmpId\r\n"
			+ "", nativeQuery = true)
	List<CycleResult> getEmployeeCycleResult(@Param("year") String year, @Param("cycleId") Long cycleId, @Param("loggedInEmpId") Long loggedInEmpId);

	
	@Query(value = "select * from tbl_trn_kra ttk where employee_id =?1  and year =?2 ;", nativeQuery = true)
	public List<Kra> findByEmployeeIdAndYear(Long id,  Long yearId);

	@Query(value = "select * from tbl_trn_kra ttk where employee_id =?1  and year =?2 and mst_kra_cycle_id =?3 ;", nativeQuery = true)
	public Kra findByEmployeeIdAndYearIdAndCycleId(Long id, Long yearId, Long id2);

	@Query(value = "select * from tbl_trn_kra ttk where employee_id =?1  and year =?2 ", nativeQuery = true)
	public List<Kra> findByEmployeeIdAndKraYear(Long id, KraYear year);


	@Query(value = 
		    "SELECT\r\n"
		    + "    cycle_name AS cycleName,\r\n"
		    + "    cycleid AS cycleId,\r\n"
		    + "    mst_cycle_type AS cycleType,\r\n"
		    + "    year_id AS \"yearId\",\r\n"
		    + "    year AS year,\r\n"
		    + "    COUNT(*) AS totalCount,\r\n"
		    + "\r\n"
		    + "    -- Completed Count & Percentage\r\n"
		    + "    COUNT(CASE\r\n"
		    + "        WHEN mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
		    + "        WHEN mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
		    + "    END) AS completedCount,\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(CASE\r\n"
		    + "            WHEN mst_cycle_type = 1 AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
		    + "            WHEN mst_cycle_type IN (2, 3) AND status = 'COMPLETED' AND pending_with = 'HR' THEN 1\r\n"
		    + "        END) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS completedPercentage,\r\n"
		    + "\r\n"
		    + "    -- Submitted Count & Percentage\r\n"
		    + "    COUNT(CASE\r\n"
		    + "        WHEN mst_cycle_type = 1 AND ((status = 'INPROGRESS' AND pending_with = 'HR') OR status = 'APPROVED') THEN 1\r\n"
		    + "        WHEN mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','SUBMITTED') THEN 1\r\n"
		    + "    END) AS submittedCount,\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(CASE\r\n"
		    + "            WHEN mst_cycle_type = 1 AND ((status = 'INPROGRESS' AND pending_with = 'HR') OR status = 'APPROVED') THEN 1\r\n"
		    + "            WHEN mst_cycle_type IN (2, 3) AND status IN ('INPROCESS','APPROVED','SUBMITTED') THEN 1\r\n"
		    + "        END) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS submittedPercentage,\r\n"
		    + "\r\n"
		    + "    -- Pending Count & Percentage\r\n"
		    + "    COUNT(CASE\r\n"
		    + "        WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
		    + "        WHEN mst_cycle_type IN (2, 3) AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'REJECTED') THEN 1\r\n"
		    + "    END) AS pendingCount,\r\n"
		    + "    ROUND(\r\n"
		    + "        100.0 * COUNT(CASE\r\n"
		    + "            WHEN mst_cycle_type = 1 AND status IN ('INPROGRESS', 'APPROVED') AND pending_with = 'MANAGER' THEN 1\r\n"
		    + "            WHEN mst_cycle_type IN (2, 3) AND status IN ('INCOMPLETE', 'NOTSUBMITTED', 'REJECTED') THEN 1\r\n"
		    + "        END) / NULLIF(COUNT(*), 0), 2\r\n"
		    + "    ) AS pendingPercentage\r\n"
		    + "\r\n"
		    + "FROM vw_vision_dashboard\r\n"
		    + "WHERE year = ?1\r\n"
		    + "  AND cycleid IN (?2)\r\n"
		    + "GROUP BY cycle_name, year,mst_cycle_type, cycleid,year_id\r\n"
		    + "ORDER BY cycle_name ASC;\r\n"
		    + "",
		    nativeQuery = true)
		List<CycleCountResult> cycleWiseCount( String year,  List<Long> cycleIds);
	
	
	
	@Query(value = "select * from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id "
			+ " where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3  ", nativeQuery = true)
	public List<Kra> findEmployeesByManger(Long loggedInEmpId, String isActive, String status);

	@Query(value = "select count(*) from tbl_trn_kra ttk join tbl_trn_kra_wf ttkw on ttk.id =ttkw.kra_id  where ttk.employee_id in(select "
			+ "	distinct term.employee_id from	tbl_employee_reporting_manager term  "
			+ "	join tbl_employee te on term.employee_id =te.id  " + "where "
			+ "	reporting_manager_id = ?1 ) and ttk.is_active =?2 and ttkw.pending_with = ?3 ", nativeQuery = true)
	public long countEmployeesByManger(Long managerId, String isActive, String status);
	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  department in (?1) and wf.pending_with = ?2  and kra.is_active =?3 ", nativeQuery = true)
	public List<Kra> findByDeptIdInAndPendingWith( Object[] deptIds, String status,String isActive);
	
	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  department in (?1) and wf.pending_with = ?2  and kra.is_active =?3 ", nativeQuery = true)
	public long countByDeptIdInAndPendingWith( Object[] deptIds, String status,String isActive);
	
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  wf.pending_with = ?1 and wf.status!=?2  and kra.is_active =?3 ", nativeQuery = true)
	public List<Kra> findByPendingWithStatusAndIsActive(String pendingWith, String status,String isActive);
	
	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where  wf.pending_with = ?1 and wf.status!=?2  and kra.is_active =?3 ", nativeQuery = true)
	public long countByPendingWithStatusAndIsActive(String pendingWith, String status,String isActive);

	public boolean existsByEmployeeAndCycleIdAndKraYearAndIsActive(Employee employee, KraCycle cycle, KraYear kraYear,String isActive);

	
	
	//div head
	@Query(value = "select * from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where division in (?1) and wf.pending_with = ?2  and kra.is_active =?3 ", nativeQuery = true)
	public List<Kra> findByDivIdnAndPendingWith( Object[] divIds, String status,String isActive);
	
	@Query(value = "select count(*) from  tbl_trn_kra kra  join  tbl_trn_kra_wf wf on kra.id =wf.kra_id "
			+ "	where division in (?1) and wf.pending_with = ?2  and kra.is_active =?3 ", nativeQuery = true)
	public long countByDivIdInAndPendingWith( Object[] divIds, String status,String isActive);

	@Query(value = "select * from tbl_trn_kra ttkd  where employee_id =?1 and  mst_kra_cycle_id =?2 ", nativeQuery = true)
	public Kra findByEmployeeIdAndCycleId(Long employeeId, Long targetCycleId);
	
	}


