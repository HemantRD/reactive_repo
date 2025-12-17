package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.CompetencySubTypes;
import com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetencySubTypesDAO extends JpaRepository<CompetencySubTypes, Integer> {


    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo(c.id, c.name) " +
            " from CompetencySubTypes c" +
            "  where c.competencyTypeId=:competencyTypeId")
    List<CompetencyDDLVo> getCompetencyTypes(Integer competencyTypeId);
}
