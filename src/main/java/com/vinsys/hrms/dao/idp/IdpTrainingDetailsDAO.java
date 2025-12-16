package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.idp.entity.IdpDetails;
import com.vinsys.hrms.idp.reports.vo.MemberWiseCost;
import com.vinsys.hrms.idp.reports.vo.TopicWiseCost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface IdpTrainingDetailsDAO extends JpaRepository<IdpDetails, Long> {

    @Query("select new com.vinsys.hrms.idp.reports.vo.MemberWiseCost(" +
            " f.officialEmailId, a.topicName, a.trainingCode, b.name, c.name, CAST(1 AS long), a.costPerPersonIndividual)" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetails d on d.training.id=a.id" +
            "     inner join Idp e on e.id=d.idp.id and e.recordStatus='Y'" +
            "     inner join Employee f on f.id=e.employeeId" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))")
    Page<MemberWiseCost> getMemberWiseCostDetailed(@RequestParam("searchParam") String searchParam,
                                                   Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.reports.vo.MemberWiseCost(" +
            " f.officialEmailId,'NA', 'NA', 'NA', 'NA'," +
            "  count(*), sum(a.costPerPersonIndividual))" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetails d on d.training.id=a.id" +
            "     inner join Idp e on e.id=d.idp.id and e.recordStatus='Y'" +
            "     inner join Employee f on f.id=e.employeeId" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "   group by f.id")
    Page<MemberWiseCost> getMemberWiseCostSummary(@RequestParam("searchParam") String searchParam,
                                                  Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.reports.vo.TopicWiseCost(" +
            " a.topicName, a.trainingCode, b.name, c.name, f.officialEmailId, CAST(1 AS long), a.costPerPersonIndividual)" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetails d on d.training.id=a.id" +
            "     inner join Idp e on e.id=d.idp.id and e.recordStatus='Y'" +
            "     inner join Employee f on f.id=e.employeeId" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))")
    Page<TopicWiseCost> getTopicWiseCostDetailed(@RequestParam("searchParam") String searchParam,
                                                 Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.reports.vo.TopicWiseCost(" +
            " a.topicName, a.trainingCode, b.name, c.name, 'NA', count(*), count(*)*a.costPerPersonIndividual)" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetails d on d.training.id=a.id" +
            "     inner join Idp e on e.id=d.idp.id and e.recordStatus='Y'" +
            "     inner join Employee f on f.id=e.employeeId" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name, a.costPerPersonIndividual")
    Page<TopicWiseCost> getTopicWiseCostSummary(@RequestParam("searchParam") String searchParam,
                                                Pageable pageable);

    @Query("select a " +
            " from IdpDetails a" +
            "     inner join Idp b on b.id=a.idp.id and b.recordStatus='Y'" +
            "     inner join Employee c on c.id=b.employeeId and c.id=:employeeId " +
            " where a.training.id=:trainingId")
    IdpDetails getIdpDetailsData(Long trainingId, Long employeeId);

}
