package com.vinsys.hrms.master.vo;

public class BrandingVO {

	private long id;
	private String imagePath;
	private String companyName;
	private String favicon;
	private String copyRight;
//	private String key;
	private String buttonColorCode;
	private String themeColorCode;
	private String bgImagePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}

	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}

//	public String getKey() {
//		return key;
//	}
//	public void setKey(String key) {
//		this.key = key;
//	}
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

}
