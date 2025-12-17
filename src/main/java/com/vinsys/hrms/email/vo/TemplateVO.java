package com.vinsys.hrms.email.vo;

import java.util.List;

/**
 * 
 * @author amey.gangakhedkar
 *
 */
public class TemplateVO {

	private String templateName;
	
	private List<PlaceHolderVO> placeHolders;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<PlaceHolderVO> getPlaceHolders() {
		return placeHolders;
	}

	public void setPlaceHolders(List<PlaceHolderVO> placeHolders) {
		this.placeHolders = placeHolders;
	}

	@Override
	public String toString() {
		return "TemplateVO [templateName=" + templateName + ", placeHolders=" + placeHolders + "]";
	}
	
}
