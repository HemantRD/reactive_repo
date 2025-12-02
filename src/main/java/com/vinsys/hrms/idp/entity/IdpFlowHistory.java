package com.vinsys.hrms.idp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tbl_trn_idp_flow_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpFlowHistory {

    @Id
    @SequenceGenerator(name = "seq_idp_flow_history", sequenceName = "seq_idp_flow_history", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_idp_flow_history")
    private Long id;

    // Reference to IDP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private Idp idp;

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