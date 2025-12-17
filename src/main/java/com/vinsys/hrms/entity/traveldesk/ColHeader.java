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
@Table(name = "tbl_trn_col_header")
public class ColHeader extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_trn_col_header", sequenceName = "seq_trn_col_header", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_col_header")
	private long id;
	@Column(name = "property")
	private String property;
	@Column(name = "header")
	private String header;
	@Column(name = "is_resizable")
	private boolean resiazable;
	@Column(name = "sortable")
	private boolean sortable;
	@Column(name = "screen_name")
	private String screenName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public boolean isResiazable() {
		return resiazable;
	}

	public void setResiazable(boolean resiazable) {
		this.resiazable = resiazable;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}
