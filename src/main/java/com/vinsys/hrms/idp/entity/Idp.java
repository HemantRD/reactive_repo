package com.vinsys.hrms.idp.entity;

import com.vinsys.hrms.master.entity.YearMaster;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tbl_trn_idp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Idp {

    @Id
    @SequenceGenerator(name = "seq_idp", sequenceName = "seq_idp", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_idp")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_tbl_trn_idp_year_id"))
    private YearMaster year;

    // Note: departmentId and divId will be fetched from view when needed
    // Removed columns: department_id, div_id

    // Bi-directional mapping to details
    @OneToMany(mappedBy = "idp", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<IdpDetails> details;
}
