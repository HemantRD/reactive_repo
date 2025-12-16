package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TrainingCatalogKeywords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingCatalogKeywordsDAO extends JpaRepository<TrainingCatalogKeywords, Long> {
    @Modifying
    @Query("delete from TrainingCatalogKeywords u where u.trainingId = ?1")
    void deleteByTrainingId(Long trainingId);

    @Query("select keyword from TrainingCatalogKeywords t where t.trainingId=:trainingId")
    List<String> findAllByTrainingId(Long trainingId);
}
