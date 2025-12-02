package com.vinsys.hrms.idp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tbl_map_employee_idp_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapEmployeeIdpStatus {

    @Id
    @SequenceGenerator(name = "seq_map_employee_idp_status", sequenceName = "seq_map_employee_idp_status", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_employee_idp_status")
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "record_status", length = 20)
    private String recordStatus; // Active / Inactive

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @OneToMany(mappedBy = "employeeIdpStatus", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MapEmployeeIdpStatusHistory> history;
}
