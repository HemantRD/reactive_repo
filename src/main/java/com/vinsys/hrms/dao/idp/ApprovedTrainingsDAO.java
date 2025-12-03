package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.ApprovedTrainings;
import com.vinsys.hrms.idp.reports.vo.DashboardVo;
import com.vinsys.hrms.idp.reports.vo.ParticipantsClustersVo;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface ApprovedTrainingsDAO extends JpaRepository<ApprovedTrainings, Long> {

    @Query("select new com.vinsys.hrms.idp.reports.vo.DashboardVo.GroupVsIndividualCostSummary" +
            "   (  sum(case when groupType='Individual' then cost else 0 end)," +
            "      sum(case when groupType='Group' then cost else 0 end)" +
            "   )" +
            "    from ApprovedTrainings a where a.year.id=:yearId")
    DashboardVo.GroupVsIndividualCostSummary groupIndividualCostSummary(Long yearId);


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
            " b.id, b.trainingCode, b.topicName, b.isInternal,b.priority, a.groupType, a.cost, count(*), " +
            "        a.cost * count(*))" +
            " from ApprovedTrainings a" +
            "     inner join TrainingCatalog b on b.id=a.training.id" +
            " where a.year.id=:yearId and " +
            "   (:searchParam is null or :searchParam = '' or b.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by b.id, b.trainingCode, b.topicName, b.isInternal, b.priority,a.groupType, a.cost" +
            "   ORDER BY count(*) DESC")
    Page<ParticipantsClustersVo> getParticipantClusters(@RequestParam("searchParam") String searchParam,
                                                        Pageable pageable);

}
