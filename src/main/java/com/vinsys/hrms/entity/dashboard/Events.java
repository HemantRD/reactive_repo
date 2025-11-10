package com.vinsys.hrms.entity.dashboard;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_dashboard_events")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Events extends AuditBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_dashboard_events", sequenceName = "seq_dashboard_events", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dashboard_events")
	private long id;
	
	@Column(name = "event_name")
	private String eventName;
	
	@Column(name = "event_desc")
	private String eventDescription;

	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	@Column(name = "branch_id")
	private long branchId;
	@Column(name = "division_id")
	private long divisionId;
//	@Column(name = "org_id")
//	private long orgId;
	
	@OneToMany(mappedBy = "events", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<EventImages> eventImages;
	
	
	
	
}
