package com.vinsys.hrms.entity.idp;

import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpDetails;
import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.master.entity.YearMaster;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "tbl_trn_idp_approved_trainings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "tbl_trn_idp_approved_trainings_idp_detail_id_uqique_index",
                        columnNames = "idp_detail_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovedTrainings {

    @Id
    @SequenceGenerator(name = "seq_tbl_trn_idp_approved_trainings",
            sequenceName = "seq_tbl_trn_idp_approved_trainings", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tbl_trn_idp_approved_trainings")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private Idp idp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_detail_id", nullable = false, unique = true)
    private IdpDetails idpDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_flow_id", nullable = false)
    private IdpFlowHistory idpFlow;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private TrainingCatalog training;

    @Column(name = "training_name", length = 500)
    private String trainingName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id", nullable = false)
    private YearMaster year;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Column(name = "group_type", nullable = false)
    private String groupType;

    @Column(name = "group_code", nullable = false)
    private String groupCode;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "record_status", length = 20)
    private String recordStatus;
}
