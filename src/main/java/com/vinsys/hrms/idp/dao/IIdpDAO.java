package com.vinsys.hrms.idp.dao;

import com.vinsys.hrms.idp.entity.Idp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IIdpDAO extends JpaRepository<Idp, Long> {
    List<Idp> findByEmployeeId(Long employeeId);

    List<Idp> findByEmployeeIdIn(List<Long> empIds);

    Idp findByIdAndActiveFlowIdAndRecordStatus(Long id, Long activeFlowId, String recordStatus);

    //TODO : Don't use findAll(), instead use findAllByYear()
    List<Idp> findAll();

    List<Idp> findAllByYear(Long yearId);

    @Modifying
    @Query(value = "update\n" +
                    "    tbl_mst_idp_training_budget_config\n" +
                    "set\n" +
                    "    idp_request_amount = \n" +
                    "    (\n" +
                    "    select \n" +
                    "    coalesce(sum(tmitc.cost_per_person_individual), 0) as idp_request_amount\n" +
                    "    from \n" +
                    "    tbl_trn_idp tti \n" +
                    "    inner join tbl_trn_idp_flow_history ttifh on tti.record_status = 'Active' \n" +
                    "            and tti.year_id = :yearId\n" +
                    "            and ttifh.idp_id = tti.id\n" +
                    "            and ttifh.employee_role  = 'TDHEAD' \n" +
                    "            and ttifh.action_type in ('Pending', 'Approve') \n" +
                    "    inner join tbl_trn_idp_details ttid on ttid.idp_id = tti.id\n" +
                    "    inner join tbl_mst_idp_training_cateloge tmitc on tmitc.id = ttid.training_id\n" +
                    "    )\n" +
                    "where\n" +
                    "    year_id = :yearId", nativeQuery = true)
    void updateIdpRequestAmountByYear(Long yearId);


}
