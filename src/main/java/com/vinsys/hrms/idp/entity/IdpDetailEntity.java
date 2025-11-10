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

@Entity
@Table(name = "tbl_trn_idp_details_1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Parent IDP reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private IdpEntity idp;

    @Column(name = "competency_type_id", nullable = false)
    private Long competencyTypeId;

    @Column(name = "competency_sub_type_id", nullable = false)
    private Long competencySubTypeId;

    @Column(name = "dev_goals", length = 1000)
    private String devGoals;

    @Column(name = "dev_actions", length = 1000)
    private String devActions;

    @Column(name = "training_id")
    private Long trainingId;

    @Column(name = "training_name", length = 500)
    private String trainingName;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "completion_certificate_file_path", length = 1000)
    private String completionCertificateFilePath;
}
