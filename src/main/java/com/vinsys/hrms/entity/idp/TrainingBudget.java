package com.vinsys.hrms.entity.idp;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.master.entity.YearMaster;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_idp_training_budget_config")
@Getter
@Setter
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

    @Column(name = "idp_request_amount")
    private Double idpRequestAmount;

    @Column(name = "consumed_amount")
    private Double consumedAmount;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    @Column(name = "created_date")
    private java.util.Date createdDate;

    @Column(name = "updated_date")
    private java.util.Date updatedDate;
}
