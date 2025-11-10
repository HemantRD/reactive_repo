package com.vinsys.hrms.entity.attendance;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_map_group_to_location")
public class MapGroupToLocation extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_map_group_to_location", sequenceName = "seq_map_group_to_location", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_group_to_location")
	private long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "mst_loc_group_id")
	private MasterOrgDivLocationGroup mstLocationGroup;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "mst_loc_detail_id")
	private MasterOrgDivLocationDetail mstLocationDetail;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MasterOrgDivLocationGroup getMstLocationGroup() {
		return mstLocationGroup;
	}

	public void setMstLocationGroup(MasterOrgDivLocationGroup mstLocationGroup) {
		this.mstLocationGroup = mstLocationGroup;
	}

	public MasterOrgDivLocationDetail getMstLocationDetail() {
		return mstLocationDetail;
	}

	public void setMstLocationDetail(MasterOrgDivLocationDetail mstLocationDetail) {
		this.mstLocationDetail = mstLocationDetail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
