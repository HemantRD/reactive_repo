package com.vinsys.hrms.entity.attendance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.MasterDivision;

@Entity
@Table(name = "tbl_mst_org_div_location_group")
public class MasterOrgDivLocationGroup extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_org_div_location_group", sequenceName = "seq_mst_org_div_location_group", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_org_div_location_group")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division_id")
	private MasterDivision division;

	@Column(name = "group_name")
	private String groupName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MasterDivision getDivision() {
		return division;
	}

	public void setDivision(MasterDivision division) {
		this.division = division;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
