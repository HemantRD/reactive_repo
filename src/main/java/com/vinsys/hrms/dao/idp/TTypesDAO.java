package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TTypes;
import com.vinsys.hrms.idp.t_types.vo.TTypesVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TTypesDAO extends JpaRepository<TTypes, Long> {

    @Query("select case when count(t) > 0 then true else false end from TTypes t " +
            " where t.name=:name and (:id is null or t.id != :id)")
    boolean nameAlreadyExist(String name, Long id);

    @Query("select new com.vinsys.hrms.idp.t_types.vo.TTypesVo(" +
            " t.id, t.name, t.remark, t.status)" +
            " from TClassifications t" +
            "    where :searchParam is null or :searchParam = '' or " +
            "              LOWER(t.name) LIKE CONCAT('%', :searchParam, '%')")
    Page<TTypesVo> findTrainingTypesByPage(@RequestParam("searchParam") String searchParam,
                                           Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.t_types.vo.TTypesVo(" +
            " t.id, t.name, t.remark, t.status)" +
            " from TClassifications t" +
            "    where :searchParam is null or :searchParam = '' or " +
            "              LOWER(t.name) LIKE CONCAT('%', :searchParam, '%')")
    List<TTypesVo> findTrainingTypesExcel(@RequestParam("searchParam") String searchParam);

}
