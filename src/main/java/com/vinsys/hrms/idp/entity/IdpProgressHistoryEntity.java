package com.vinsys.hrms.idp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.time.Instant;

@Entity
@Table(name = "tbl_trn_idp_progress_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpProgressHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to IDP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private IdpEntity idp;

    // Link to IDP detail
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_detail_id", nullable = false)
    private IdpDetailEntity idpDetail;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // duplicated for quick reference

    @Column(name = "log_date")
    private Instant logDate;

    @Column(name = "progress_value")
    private Integer progressValue;

    @Column(name = "progress_unit", length = 100)
    private String progressUnit; // e.g., %

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "status", length = 50)
    private String status; // Pending / In Progress / Completed

    @Column(name = "record_status", length = 20)
    private String recordStatus; // Active / Inactive
}