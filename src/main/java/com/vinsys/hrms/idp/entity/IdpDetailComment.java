package com.vinsys.hrms.idp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "tbl_trn_idp_details_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpDetailComment {

    @Id
    @SequenceGenerator(name = "seq_idp_details_comment", sequenceName = "seq_idp_details_comment", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_idp_details_comment")
    private Long id;

    // Foreign keys
    @Column(name = "idp_id", nullable = false)
    private Long idpId;

    @Column(name = "idp_detail_id", nullable = false)
    private Long idpDetailId;

    @Column(name = "idp_flow_id")
    private Long idpFlowId;

    @Column(name = "comment", length = 1000)
    private String comment;
}
