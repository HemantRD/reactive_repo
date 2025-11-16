package com.vinsys.hrms.idp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "tbl_trn_idp_email_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdpEmailLog {

    @Id
    @SequenceGenerator(name = "seq_tbl_trn_idp_email_log",
            sequenceName = "seq_tbl_trn_idp_email_log", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tbl_trn_idp_email_log")
    private Long id;

    // Reference to IDP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_id", nullable = false)
    private Idp idp;

    // Reference to flow history
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_flow_id", nullable = false)
    private IdpFlowHistory idpFlow;

    @Column(name = "status", length = 50)
    private String status; // Pending / Sending / Sent

    @Column(name = "action_type", length = 50)
    private String actionType; // Pending / Sending / Sent

    @Column(name = "log_message", length = 2000)
    private String logMessage;
}