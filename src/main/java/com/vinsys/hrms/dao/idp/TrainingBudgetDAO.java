package com.vinsys.hrms.dao.idp;

import com.vinsys.hrms.entity.idp.TrainingBudget;
import com.vinsys.hrms.idp.reports.vo.BudgetUtilization;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingBudgetVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingBudgetDAO extends JpaRepository<TrainingBudget, Long> {

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.TrainingBudgetVo(" +
            " a.id, a.year.year, a.budgetAmount, a.idpRequestAmount, a.consumedAmount, a.currencySymbol, a.remark, a.createdDate, a.updatedDate)" +
            " from TrainingBudget a" +
            " where a.isActive='Y' and (:year is null or :year = 0 or a.year.year=:year) " +
            " order by a.year.year desc")
    Page<TrainingBudgetVo> findTrainingBudgetsByPage(Integer year, Pageable pageable);

    @Query("select new com.vinsys.hrms.idp.trainingcatalog.vo.TrainingBudgetVo(" +
            " a.id, a.year.year, a.budgetAmount, a.idpRequestAmount, a.consumedAmount, a.currencySymbol, a.remark, a.createdDate, a.updatedDate)" +
            " from TrainingBudget a" +
            " where a.isActive='Y' and (:year is null or :year = 0 or a.year.year=:year) " +
            " order by a.year.year desc")
    List<TrainingBudgetVo> findTrainingBudgetsExcel(Integer year);

    @Query("select t.id from TrainingBudget t " +
            " where t.year.year=:year and (:id is null or t.id != :id)")
    Long yearAlreadyExist(Long year, Long id);

    @Query("select new com.vinsys.hrms.idp.reports.vo.BudgetUtilization" +
            "   (budgetAmount, idpRequestAmount, consumedAmount, currencySymbol)" +
            "    from TrainingBudget a " +
            " where a.isActive='Y' and a.year.id=:yearId")
    BudgetUtilization getBudgetUtilization(Long yearId);

}
