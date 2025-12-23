package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TClassifications;
import com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TClassificationsDAO extends JpaRepository<TClassifications, Long> {

    @Query("select case when count(t) > 0 then true else false end from TClassifications t " +
            " where t.name=:name and (:id is null or t.id != :id)")
    boolean nameAlreadyExist(String name, Long id);

    @Query("select new com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo(" +
            " t.id, t.name, t.remark, t.status)" +
            " from TClassifications t" +
            "    where :searchParam is null or :searchParam = '' or " +
            "              LOWER(t.name) LIKE CONCAT('%', :searchParam, '%')")
    Page<TClassificationsVo> findTrainingClassificationsByPage(@RequestParam("searchParam") String searchParam,
                                                               Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.t_classifications.vo.TClassificationsVo(" +
            " t.id, t.name, t.remark, t.status)" +
            " from TClassifications t" +
            "    where :searchParam is null or :searchParam = '' or " +
            "              LOWER(t.name) LIKE CONCAT('%', :searchParam, '%')")
    List<TClassificationsVo> findTrainingClassificationsExcel(@RequestParam("searchParam") String searchParam);

}
