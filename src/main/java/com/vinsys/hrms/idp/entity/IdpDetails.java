package com.vinsys.hrms.idp.entity;

import com.vinsys.hrms.entity.idp.TrainingCatalog;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "tbl_trn_idp_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpDetails {

    @Id
    @SequenceGenerator(name = "seq_idp_details", sequenceName = "seq_idp_details", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_idp_details")
    private Long id;

    // Parent IDP reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private Idp idp;

    @Column(name = "competency_type_id", nullable = false)
    private Long competencyTypeId;

    @Column(name = "competency_sub_type_id", nullable = false)
    private Long competencySubTypeId;

    @Column(name = "dev_goals", length = 1000)
    private String devGoals;

    @Column(name = "dev_actions", length = 1000)
    private String devActions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = true,
                foreignKey = @ForeignKey(name = "fk_tbl_trn_idp_details_training_id"))
    private TrainingCatalog training;

    @Column(name = "training_name", length = 500)
    private String trainingName;

    @Column(name = "priority", length = 500)
    private String priority;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "completion_certificate_file_path", length = 1000)
    private String completionCertificateFilePath;
}
