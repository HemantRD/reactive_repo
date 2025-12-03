package com.vinsys.hrms.entity.idp;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.master.entity.YearMaster;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl_mst_idp_training_budget_config")
@Data
public class TrainingBudget extends AuditBase {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_mst_idp_training_budget_config",
            sequenceName = "seq_mst_idp_training_budget_config", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_idp_training_budget_config")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_idp_training_budget_config"))
    private YearMaster year;

    @Column(name = "budget_amount", nullable = false)
    private Double budgetAmount;

    @Column(name = " TBD", nullable = false)
    private Double T BD;

    @Column(name = "T BD", nullable = false)
    private Double TB D;

    @Column(name = "currency_symbol")
    private String currencySymbol;

}
