package com.vinsys.hrms.master.vo;

public class MasterTitleVo {
	private Long id;
	private String title;
	private String titleDescription;
	private String isActive;

	public MasterTitleVo() {
	}

//	public MasterTitleVo(long id, String title, String titleDescription) {
//		super();
//		this.id = id;
//		this.title = title;
//		this.titleDescription = titleDescription;
//	}


	public MasterTitleVo(Long id, String title, String titleDescription) {
		super();
		this.id = id;
		this.title = title;
		this.titleDescription = titleDescription;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleDescription() {
		return titleDescription;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
}