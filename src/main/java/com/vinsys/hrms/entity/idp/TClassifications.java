package com.vinsys.hrms.entity.idp;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.master.entity.YearMaster;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl_mst_idp_training_classification")
@Data
public class TClassifications extends AuditBase {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_tbl_mst_idp_training_classification",
            sequenceName = "seq_tbl_mst_idp_training_classification", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tbl_mst_idp_training_classification")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
