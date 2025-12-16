package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOMasterMenu {

	private int id;

	private String name;

	private String url;

	private List<VOMasterMenu> childMenu;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<VOMasterMenu> getChildMenu() {
		return childMenu;
	}

	public void setChildMenu(List<VOMasterMenu> childMenu) {
		this.childMenu = childMenu;
	}

}
