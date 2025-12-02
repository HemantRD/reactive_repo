package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_branding")
public class MasterBranding extends AuditBase  {

	
	@Id
	@Column(name = "id")
	private Long Id;
	@Column(name = "key")
	private String key;
	@Column(name = "image_path")
	private String imagePath;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "copyright")
	private String copyRight;
	@Column(name = "favicon")
	private String favicon;
	@Column(name = "button_color_code")
	private String buttonColorCode;
	@Column(name = "theme_color_code")
	private String themeColorCode;
	@Column(name = "bg_image_path")
	private String bgImagePath;
	
	public Long getId() {
		return Id;
	}
	public String getButtonColorCode() {
		return buttonColorCode;
	}
	public void setButtonColorCode(String buttonColorCode) {
		this.buttonColorCode = buttonColorCode;
	}
	public String getThemeColorCode() {
		return themeColorCode;
	}
	public void setThemeColorCode(String themeColorCode) {
		this.themeColorCode = themeColorCode;
	}
	public String getBgImagePath() {
		return bgImagePath;
	}
	public void setBgImagePath(String bgImagePath) {
		this.bgImagePath = bgImagePath;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCopyRight() {
		return copyRight;
	}
	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}
	public String getFavicon() {
		return favicon;
	}
	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}
	
	
	
	
	
}
