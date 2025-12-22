package com.vinsys.hrms.entity.traveldesk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_mode_of_travel")
public class MasterModeOfTravel extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_mode_of_travel", sequenceName = "seq_mst_mode_of_travel", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_mode_of_travel")
	private long id;
	@Column(name = "mode_of_travel")
	private String modeOfTravel;
	@Column(name = "mode_of_travel_description")
	private String modeOfTravelDescription;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getModeOfTravel() {
		return modeOfTravel;
	}

	public void setModeOfTravel(String modeOfTravel) {
		this.modeOfTravel = modeOfTravel;
	}

	public String getModeOfTravelDescription() {
		return modeOfTravelDescription;
	}

	public void setModeOfTravelDescription(String modeOfTravelDescription) {
		this.modeOfTravelDescription = modeOfTravelDescription;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
