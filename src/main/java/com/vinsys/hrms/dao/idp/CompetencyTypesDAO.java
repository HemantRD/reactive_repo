package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.CompetencyTypes;
import com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetencyTypesDAO extends JpaRepository<CompetencyTypes, Integer> {

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo(c.id, c.name) " +
            " from CompetencyTypes c")
    List<CompetencyDDLVo> getCompetencyTypes();
}
