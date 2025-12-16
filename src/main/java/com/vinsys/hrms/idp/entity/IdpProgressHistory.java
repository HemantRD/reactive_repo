package com.vinsys.hrms.idp.entity;

import com.vinsys.hrms.entity.AuditBase;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_trn_idp_progress_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpProgressHistory  extends AuditBase {

    @Id
    @SequenceGenerator(name = "seq_tbl_trn_idp_progress_history",
            sequenceName = "seq_tbl_trn_idp_progress_history", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tbl_trn_idp_progress_history")
    private Long id;

    // Link to IDP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private Idp idp;

    // Link to IDP detail
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_detail_id", nullable = false)
    private IdpDetails idpDetail;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // duplicated for quick reference

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "progress_value")
    private Integer progressValue;

    @Column(name = "progress_unit", length = 100)
    private String progressUnit; // e.g., %

    @Column(name = "status", length = 50)
    private String status; // Pending / In Progress / Completed
}