package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TrainingBudget;
import com.vinsys.hrms.idp.reports.vo.DashboardVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingBudgetVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrainingBudgetDAO extends JpaRepository<TrainingBudget, Long> {

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.TrainingBudgetVo(" +
            " a.id, a.year.year, a.budgetAmount, a.currencySymbol, a.remark)" +
            " from TrainingBudget a" +
            "    where (:year is null or :year = 0 or a.year.year=:year)")
    Page<TrainingBudgetVo> findTrainingBudgetsByPage(Integer year, Pageable pageable);

    @Query("select case when count(t) > 0 then true else false end from TrainingBudget t " +
            " where t.year.year=:year and (:id is null or t.id != :id)")
    boolean yearAlreadyExist(Long year, Long id);

    @Que ry("select new com.vinsys.hrms.idp.reports.vo.DashboardVo.BudgetUtilization" +
            "   (budgetAmount, consumedAmount, totalRequestedAmount, currencySymbol)" +
            "    from TrainingBudget a " +
            " where a.year.id=:yearId")
    DashboardVo.BudgetUtilization getBudgetUtilization(Long yearId);


}
