package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.ApprovedTrainings;
import com.vinsys.hrms.idp.reports.vo.GroupVsIndividualCostSummary;
import com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ApprovedTrainingsDAO extends JpaRepository<ApprovedTrainings, Long> {

    @Query("select new com.vinsys.hrms.idp.reports.vo.GroupVsIndividualCostSummary" +
            "   (  sum(case when groupType='Individual' then cost else 0 end)," +
            "      sum(case when groupType='Group' then cost else 0 end)" +
            "   )" +
            "    from ApprovedTrainings a where a.year.id=:yearId")
    GroupVsIndividualCostSummary groupIndividualCostSummary(Long yearId);

    @Query("select count(distinct groupCode) " +
            "    from ApprovedTrainings a where a.year.id=:yearId")
    Integer getTotalTraining(Long yearId);

    @Query("select count(distinct groupCode) " +
            "    from ApprovedTrainings a where a.groupType='Group' and a.year.id=:yearId")
    Integer getTotalGroupTraining(Long yearId);

    @Query("select count(distinct groupCode) " +
            "    from ApprovedTrainings a where a.groupType='Individual' and a.year.id=:yearId")
    Integer getTotalIndividualTraining(Long yearId);

    @Query("select new com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo(" +
            " b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost, count(*), " +
            "        a.cost * count(*))" +
            " from ApprovedTrainings a" +
            "     inner join TrainingCatalog b on b.id=a.training.id" +
            " where a.year.id=:yearId and " +
            "   (:searchParam is null or :searchParam = '' or lower(b.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(b.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "    and (:isInternal is null or b.isInternal=:isInternal)" +
            "    and (:priority is null or b.priority=:priority)" +
            "    and (:trainingType is null or b.trainingType=:trainingType)" +
            "  group by b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost")
    Page<ParticipantsClustersVo> getParticipantClusters(@RequestParam("searchParam") String searchParam,
                                                        Boolean isInternal, Integer priority, String trainingType,
                                                        Pageable pageable, Long yearId);

    @Query("select new com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo(" +
            " b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost, count(*), " +
            "        a.cost * count(*))" +
            " from ApprovedTrainings a" +
            "     inner join TrainingCatalog b on b.id=a.training.id" +
            " where a.year.id=:yearId and " +
            "   (:searchParam is null or :searchParam = '' or lower(b.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(b.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "    and (:isInternal is null or b.isInternal=:isInternal)" +
            "    and (:priority is null or b.priority=:priority)" +
            "    and (:trainingType is null or b.trainingType=:trainingType)" +
            "  group by b.id, b.trainingCode, b.topicName, b.isInternal, b.priority, a.groupType, a.groupCode, a.cost")
    Page<ParticipantsClustersVo> getParticipantClustersExcel(@RequestParam("searchParam") String searchParam,
                                                             Boolean isInternal, Integer priority, String trainingType,
                                                             Sort sort, Long yearId);

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

    @Query("select a.employeeId from ApprovedTrainings a where a.groupCode=:groupCode and a.year.id=:yearId")
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
