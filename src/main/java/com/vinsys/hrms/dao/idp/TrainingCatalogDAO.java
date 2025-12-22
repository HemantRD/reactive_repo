package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.idp.reports.vo.TopTrainingCourses;
import com.vinsys.hrms.idp.trainingcatalog.vo.SearchTopicsVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TrainingCatalogDAO extends JpaRepository<TrainingCatalog, Long> {

    @Query("select case when count(t) > 0 then true else false end from TrainingCatalog t " +
            " where t.trainingCode=:trainingCode and (:id is null or t.id != :id)")
    boolean trainingCodeAlreadyExist(String trainingCode, Long id);

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo(" +
            " t.id, t.trainingCode, t.topicName, t.competencyTypeId," +
            " t.competencySubTypeId, t.isInternal, t.costPerPersonIndividual, t.costPerPersonGroup," +
            " t.costPerGroup, t.minPersonInGroup, t.maxPersonInGroup, t.isCertificationCourse," +
            " t.priority, t.durationInHours, t.remark, t.isActive)" +
            " from TrainingCatalog t" +
            "    where (:searchParam is null or :searchParam = '' or LOWER(t.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(t.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "    and (:isInternal is null or t.isInternal=:isInternal)" +
            "    and (:isCertificationCourse is null or t.isCertificationCourse=:isCertificationCourse)" +
            "    and (t.isActive=:isActive)")
    Page<TrainingCatalogVo> findTrainingCatalogsByPage(@RequestParam("searchParam") String searchParam,
                                                       Boolean isInternal, Boolean isCertificationCourse,
                                                       String isActive, Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo(" +
            " t.id, t.trainingCode, t.topicName, t.competencyTypeId," +
            " t.competencySubTypeId, t.isInternal, t.costPerPersonIndividual, t.costPerPersonGroup," +
            " t.costPerGroup, t.minPersonInGroup, t.maxPersonInGroup, t.isCertificationCourse," +
            " t.priority, t.durationInHours, t.remark, t.isActive)" +
            " from TrainingCatalog t" +
            "    where (:searchParam is null or :searchParam = '' or LOWER(t.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(t.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "    and (:isInternal is null or t.isInternal=:isInternal)" +
            "    and (:isCertificationCourse is null or t.isCertificationCourse=:isCertificationCourse)" +
            "    and (t.isActive=:isActive)")
    List<TrainingCatalogVo> findTrainingCatalogsByPageExcel(@RequestParam("searchParam") String searchParam,
                                                            Boolean isInternal, Boolean isCertificationCourse,
                                                            String isActive, Sort sort);

    @Query("select new com.vinsys.hrms.idp.reports.vo.TopTrainingCourses(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*) as cnt)" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetails d on d.training.id=a.id" +
            "     inner join Idp e on e.id=d.idp.id and e.recordStatus='Active'" +
            " where (:searchParam is null or :searchParam = '' or LOWER(a.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(a.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name" +
            "  ORDER BY count(*) DESC")
    Page<TopTrainingCourses> getTopTrainingCourses(@RequestParam("searchParam") String searchParam,
                                                   Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.reports.vo.TopTrainingCourses(" +
            " a.topicName, a.trainingCode, b.name, c.name, count(*) as cnt)" +
            " from TrainingCatalog a" +
            "     inner join CompetencyTypes b on b.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes c on c.id=a.competencySubTypeId" +
            "     inner join IdpDetails d on d.training.id=a.id" +
            "     inner join Idp e on e.id=d.idp.id and e.recordStatus='Active'" +
            " where (:searchParam is null or :searchParam = '' or LOWER(a.topicName) LIKE CONCAT('%', :searchParam, '%')" +
            "         or LOWER(a.trainingCode) LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.topicName, a.trainingCode, b.name, c.name" +
            "  ORDER BY count(*) DESC")
    List<TopTrainingCourses> getTopTrainingCoursesExcel(@RequestParam("searchParam") String searchParam);

    boolean existsByTrainingCodeAndIsActive(String trainingCode, String isActive);

    @Query("select count(*) from TrainingCatalog a " +
            "     inner join IdpDetails b on b.training.id=a.id" +
            "     inner join Idp c on c.id=b.idp.id and c.recordStatus='Y'" +
            "   WHERE a.trainingCode =:trainingCode and c.employeeId=:employeeId")
    long isTrainingCodeBindToEmployee(String trainingCode, Long employeeId);

    @Query("select id from TrainingCatalog t where t.trainingCode=:trainingCode")
    Long getIdByTrainingCode(String trainingCode);

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.SearchTopicsVo(" +
            " a.id, a.topicName, a.trainingCode, c.name, d.name, count(*) as cnt)" +
            " from TrainingCatalog a" +
            "     inner join TrainingCatalogKeywords b on b.trainingId=a.id" +
            "     inner join CompetencyTypes c on c.id=a.competencyTypeId" +
            "     inner join CompetencySubTypes d on d.id=a.competencySubTypeId" +
            " where a.isActive='Y' and b.keyword in (:keywords) and " +
            "  (:searchParam is null or :searchParam = '' or a.topicName LIKE CONCAT('%', :searchParam, '%'))" +
            "  group by a.id, a.topicName, a.trainingCode, c.name, d.name")
    Page<SearchTopicsVo> getSearchTrainingTopics(@RequestParam("searchParam") String searchParam,
                                                 Pageable pageable, List<String> keywords);

    @Query("select distinct b.keyword " +
            " from TrainingCatalog a" +
            "     inner join TrainingCatalogKeywords b on b.trainingId=a.id" +
            " where a.isActive='Y'")
    List<String> getDistinctKeywords();

}
