package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface TrainingCatalogDAO extends JpaRepository<TrainingCatalog, Long> {

    @Query("select case when count(t) > 0 then true else false end from TrainingCatalog t " +
            " where t.trainingCode=:trainingCode and (:id is null or t.id != :id)")
    boolean trainingCodeAlreadyExist(String trainingCode, Long id);

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo(" +
            " t.id, t.trainingCode, t.topicName, t.competencyTypeId," +
            " t.competencySubTypeId, t.isInternal, t.costPerPersonIndividual, t.costPerPersonGroup," +
            " t.costPerGroup, t.minPersonInGroup, t.maxPersonInGroup, t.isCertificationCourse," +
            " t.priority, t.remark, t.isActive)" +
            " from TrainingCatalog t" +
            "    where (:searchParam is null or :searchParam = '' or t.topicName LIKE CONCAT('%', :searchParam, '%'))")
    Page<TrainingCatalogVo> findTrainingCatalogsByPage(@RequestParam("searchParam") String searchParam,
                                                       Pageable pageable);


    @Query("select new com.vinsys.hrms.idp.reports.vo.TopTrainingCourses(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*))" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetailEntity d on d.trainingId=a.id" +
            "     inner join IdpEntity e on e.id=d.idp.id and e.recordStatus='Y'" +
            " where (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name")
    Page<TopTrainingCourses> getTopTrainingCourses(@RequestParam("searchParam") String searchParam,
                                                   Pageable pageable);

    boolean existsByTrainingCodeAndIsActive(String trainingCode, String isActive);

}
