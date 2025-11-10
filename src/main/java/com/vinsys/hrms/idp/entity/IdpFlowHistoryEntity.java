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
@Table(name = "tbl_trn_idp_flow_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpFlowHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to IDP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private IdpEntity idp;

    @Column(name = "employee_role", length = 100)
    private String employeeRole; // Team Member / Line Manager / Head TD

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // FK tbl_employee or tbl_login

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "action_type", length = 50)
    private String actionType; // Pending / Submitted / Approved / Rejected

    @Column(name = "action_date")
    private Instant actionDate;

    @Column(name = "status", length = 50)
    private String status; // Pending / Closed / Draft / Submitted

    @Column(name = "record_status", length = 20)
    private String recordStatus; // Active / Inactive
}