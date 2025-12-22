package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.ApprovedTrainings;
import com.vinsys.hrms.idp.reports.vo.GroupVsIndividualCostSummary;
import com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ApprovedTrainingsDAO extends JpaRepository<ApprovedTrainings, Long> {

    @Query("select new com.vinsys.hrms.idp.reports.vo.GroupVsIndividualCostSummary" +
            "   (  sum(case when LOWER(groupType)='individual' then cost else 0 end)," +
            "      sum(case when LOWER(groupType)='group' then cost else 0 end)" +
            "   )" +
            "    from ApprovedTrainings a where a.year.id=:yearId")
    GroupVsIndividualCostSummary groupIndividualCostSummary(Long yearId);

    @Query("select count(distinct groupCode) " +
            "    from ApprovedTrainings a where a.year.id=:yearId")
    Integer getTotalTraining(Long yearId);

    @Query("select count(distinct groupCode) " +
            "    from ApprovedTrainings a where LOWER(a.groupType)='group' and a.year.id=:yearId")
    Integer getTotalGroupTraining(Long yearId);

    @Query("select count(distinct groupCode) " +
            "    from ApprovedTrainings a where LOWER(a.groupType)='individual' and a.year.id=:yearId")
    Integer getTotalIndividualTraining(Long yearId);

    String QUERY = "select new com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo(" +
            " b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost, count(*) as memberCount, " +
            "        a.cost * count(*) as totalCost)" +
            " from ApprovedTrainings a" +
            "     inner join TrainingCatalog b on b.id=a.training.id" +
            " where a.year.id=:yearId and " +
            "   (:searchParam is null or :searchParam = '' or LOWER(b.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(b.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "    and (:isInternal is null or b.isInternal=:isInternal)" +
            "    and (:priority is null or b.priority=:priority)" +
            "    and (:trainingType is null or a.groupType=:trainingType)" +
            "  group by b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost";

    @Query(QUERY +
            "    order by 9")
    Page<ParticipantsClustersVo> getParticipantClustersCountAsc(@RequestParam("searchParam") String searchParam,
                                                                Boolean isInternal, Integer priority, String trainingType,
                                                                Pageable pageable, Long yearId);

    @Query(QUERY +
            "    order by 9 desc")
    Page<ParticipantsClustersVo> getParticipantClustersCountDesc(@RequestParam("searchParam") String searchParam,
                                                                 Boolean isInternal, Integer priority, String trainingType,
                                                                 Pageable pageable, Long yearId);


    @Query(QUERY +
            "    order by 10")
    Page<ParticipantsClustersVo> getParticipantClustersCostAsc(@RequestParam("searchParam") String searchParam,
                                                               Boolean isInternal, Integer priority, String trainingType,
                                                               Pageable pageable, Long yearId);

    @Query(QUERY +
            "    order by 10 desc")
    Page<ParticipantsClustersVo> getParticipantClustersCostDesc(@RequestParam("searchParam") String searchParam,
                                                                Boolean isInternal, Integer priority, String trainingType,
                                                                Pageable pageable, Long yearId);

    @Query("select new com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo(" +
            " b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost, count(*) as memberCount, " +
            "        a.cost * count(*) as totalCost)" +
            " from ApprovedTrainings a" +
            "     inner join TrainingCatalog b on b.id=a.training.id" +
            " where a.year.id=:yearId and " +
            "   (:searchParam is null or :searchParam = '' or LOWER(b.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(b.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "    and (:isInternal is null or b.isInternal=:isInternal)" +
            "    and (:priority is null or b.priority=:priority)" +
            "    and (:trainingType is null or a.groupType=:trainingType)" +
            "  group by b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost" +
            "  order by 9 desc")
    List<ParticipantsClustersVo> getParticipantClustersExcel(@RequestParam("searchParam") String searchParam,
                                                             Boolean isInternal, Integer priority, String trainingType,
                                                             Long yearId);

    @Query(value =
            "select t1.training_id as trainingId, " +
                    "       count(t1.training_id) as participantCount, " +
                    "       max(t1.idp_detail_id) as maxIdpDetailId " +
                    "from tbl_trn_idp_approved_trainings t1 " +
                    "inner join tbl_mst_idp_training_cateloge tmitc on tmitc.id = t1.training_id " +
                    "where t1.year_id = :yearId " +
                    "group by t1.training_id, tmitc.min_person_in_group " +
                    "having count(t1.training_id) >= tmitc.min_person_in_group",
            nativeQuery = true)
    Object[][] getGroupEligibleTrainings(Long yearId);

    @Modifying
    @Query(value =
            "update tbl_trn_idp_approved_trainings " +
                    "set group_code = :groupCode, group_type = 'Group' " +
                    "where training_id = :trainingId and year_id = :yearId",
            nativeQuery = true)
    void updateGroupCode(String groupCode, Long trainingId, Long yearId);

    @Query(value = "select\n" +
                    "    tti.employee_id\n" +
                    "from\n" +
                    "    tbl_trn_idp_approved_trainings ttiat\n" +
                    "inner join tbl_trn_idp tti on\n" +
                    "    ttiat.idp_id = tti.id\n" +
                    "where \n" +
                    "    ttiat.group_code = :groupCode\n" +
                    "    and ttiat.year_id = :yearId",
            nativeQuery = true)
    List<Long> getAllEmployeeIdsByGroupCode(String groupCode, Long yearId);

    @Modifying
    @Query(value = "update\n" +
                    "    tbl_mst_idp_training_budget_config\n" +
                    "set\n" +
                    "    consumed_amount = \n" +
                    "    (\n" +
                    "    select\n" +
                    "        coalesce(sum(cost), 0) as consumed_amount\n" +
                    "    from\n" +
                    "        tbl_trn_idp_approved_trainings\n" +
                    "    where\n" +
                    "        year_id = :yearId)\n" +
                    "where\n" +
                    "    year_id = :yearId",
            nativeQuery = true)
    void updateTotalConsumedAmountByYearId(Long yearId);

}
