package com.vinsys.hrms.kra.dao;

import com.vinsys.hrms.kra.entity.KraAggregateScores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IKraAggregateScoresDao extends JpaRepository<KraAggregateScores, Long> {

    public List<KraAggregateScores> findByKraCycleIdAndLevelOfAggregation(Long kraCycleId, String levelOfAggregation);

    public List<KraAggregateScores> findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(Long kraCycleId, String levelOfAggregation, Long departmentId);

    public List<KraAggregateScores> findByKraCycleIdAndLevelOfAggregationAndDepartment_IdAndDivision_Id(Long kraCycleId, String levelOfAggregation, Long departmentId, Long divisionId);

}
