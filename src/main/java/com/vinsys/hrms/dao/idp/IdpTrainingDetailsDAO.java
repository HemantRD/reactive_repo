package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.idp.reports.vo.MemberWiseCost;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import com.vinsys.hrms.idp.reports.vo.TopicWiseCost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface IdpTrainingDetailsDAO extends JpaRepository<TrainingCatalog, Long> {

    @Query("select new com.vinsys.hrms.idp.reports.vo.MemberWiseCost(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*))" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetailEntity d on d.trainingId=a.id" +
            "     inner join IdpEntity e on e.id=d.idp.id and e.recordStatus='Y'" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name")
    Page<MemberWiseCost> getMemberWiseCostDetailed(@RequestParam("searchParam") String searchParam,
                                                   Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.reports.vo.MemberWiseCost(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*))" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetailEntity d on d.trainingId=a.id" +
            "     inner join IdpEntity e on e.id=d.idp.id and e.recordStatus='Y'" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name")
    Page<MemberWiseCost> getMemberWiseCostSummary(@RequestParam("searchParam") String searchParam,
                                                  Pageable pageable);


    @Query("select new com.vinsys.hrms.idp.reports.vo.TopicWiseCost(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*))" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetailEntity d on d.trainingId=a.id" +
            "     inner join IdpEntity e on e.id=d.idp.id and e.recordStatus='Y'" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name")
    Page<TopicWiseCost> getTopicWiseCoseDetailed(@RequestParam("searchParam") String searchParam,
                                                 Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.reports.vo.TopicWiseCost(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*))" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetailEntity d on d.trainingId=a.id" +
            "     inner join IdpEntity e on e.id=d.idp.id and e.recordStatus='Y'" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name")
    Page<TopicWiseCost> getTopicWiseCostSummary(@RequestParam("searchParam") String searchParam,
                                                Pageable pageable);

}
