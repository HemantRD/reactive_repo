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
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tbl_trn_idp_1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    // Snapshot of employee info at IDP creation time
    @Column(name = "position")
    private String position;

    @Column(name = "function")
    private String function;

    @Column(name = "grade")
    private String grade;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "status", length = 50)
    private String status; // e.g., Draft / Submitted / Approved / Completed

    @Column(name = "record_status", length = 20)
    private String recordStatus; // Active / Inactive

    @Column(name = "active_flow_id")
    private Long activeFlowId;

    // Bi-directional mapping to details
    @OneToMany(mappedBy = "idp", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<IdpDetailEntity> details;
}