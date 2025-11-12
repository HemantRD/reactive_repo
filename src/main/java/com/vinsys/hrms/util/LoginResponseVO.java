package com.vinsys.hrms.util;

import java.util.List;

import com.vinsys.hrms.entity.MasterMenu;

public class LoginResponseVO {

	private String token;
	private long expiresIn;
	private List<MasterMenu> menu;
	private String empKraActionEnable;
	private String rmKraActionEnable;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<MasterMenu> getMenu() {
		return menu;
	}

	public void setMenu(List<MasterMenu> menu) {
		this.menu = menu;
	}

	public String getEmpKraActionEnable() {
		return empKraActionEnable;
	}

	public void setEmpKraActionEnable(String empKraActionEnable) {
		this.empKraActionEnable = empKraActionEnable;
	}

	public String getRmKraActionEnable() {
		return rmKraActionEnable;
	}

	public void setRmKraActionEnable(String rmKraActionEnable) {
		this.rmKraActionEnable = rmKraActionEnable;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	

}
