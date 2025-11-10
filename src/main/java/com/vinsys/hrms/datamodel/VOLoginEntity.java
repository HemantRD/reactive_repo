package com.vinsys.hrms.datamodel;

import java.util.List;
import java.util.Set;

import com.vinsys.hrms.entity.MasterMenu;

public class VOLoginEntity extends HRMSBaseResponse {

	private static final long serialVersionUID = 1L;
	private long id;

	private VOOrganization organization;
	private String loginEntityName;
	private String username;
	private String primaryEmail;
	private String superUser;
	private String noOrg;
	private String firstPassword;
	private String password;
	private String isFirstLogin;
	private String loginHashcode;
	private VOCandidate candidate;
	//private VOLoginEntityType loginEntityType;
	private Set<VOMasterMenu> menuList;
	private Set<VOLoginEntityType> loginEntityTypes;
	private String applicationVersion;
	private boolean isOrgLevel;
	private boolean isApprovalofHandoverChecklist;
	
	
	private boolean HRTravelDesk;
	private boolean ManagerTravelDesk;
	private boolean driverTravelDesk;
	private boolean isRemoteLocationAttendanceAllowed;
	private boolean isImeiNumberPresent;
	private long noOfDaysToConfirm;
	private String token;
	
	private List<MasterMenu> menu;
	
	public List<MasterMenu> getMenu() {
		return menu;
	}
	public void setMenu(List<MasterMenu> menu) {
		this.menu = menu;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}

	public String getLoginEntityName() {
		return loginEntityName;
	}

	public void setLoginEntityName(String loginEntityName) {
		this.loginEntityName = loginEntityName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getSuperUser() {
		return superUser;
	}

	public void setSuperUser(String superUser) {
		this.superUser = superUser;
	}

	public String getNoOrg() {
		return noOrg;
	}

	public void setNoOrg(String noOrg) {
		this.noOrg = noOrg;
	}

	public String getFirstPassword() {
		return firstPassword;
	}

	public void setFirstPassword(String firstPassword) {
		this.firstPassword = firstPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getLoginHashcode() {
		return loginHashcode;
	}

	public void setLoginHashcode(String loginHashcode) {
		this.loginHashcode = loginHashcode;
	}

	public VOCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(VOCandidate candidate) {
		this.candidate = candidate;
	}

//	public VOLoginEntityType getLoginEntityType() {
//		return loginEntityType;
//	}
//
//	public void setLoginEntityType(VOLoginEntityType loginEntityType) {
//		this.loginEntityType = loginEntityType;
//	}

	public Set<VOMasterMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(Set<VOMasterMenu> menuList) {
		this.menuList = menuList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<VOLoginEntityType> getLoginEntityTypes() {
		return loginEntityTypes;
	}

	public void setLoginEntityTypes(Set<VOLoginEntityType> loginEntityTypes) {
		this.loginEntityTypes = loginEntityTypes;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public boolean getIsOrgLevel() {
		return isOrgLevel;
	}

	public void setIsOrgLevel(boolean isOrgLevel) {
		this.isOrgLevel = isOrgLevel;
	}

	public boolean isApprovalofHandoverChecklist() {
		return isApprovalofHandoverChecklist;
	}

	public void setApprovalofHandoverChecklist(boolean isApprovalofHandoverChecklist) {
		this.isApprovalofHandoverChecklist = isApprovalofHandoverChecklist;
	}

	public boolean isHRTravelDesk() {
		return HRTravelDesk;
	}

	public void setHRTravelDesk(boolean hRTravelDesk) {
		HRTravelDesk = hRTravelDesk;
	}

	public boolean isManagerTravelDesk() {
		return ManagerTravelDesk;
	}

	public void setManagerTravelDesk(boolean managerTravelDesk) {
		ManagerTravelDesk = managerTravelDesk;
	}

	public void setOrgLevel(boolean isOrgLevel) {
		this.isOrgLevel = isOrgLevel;
	}

	public boolean isDriverTravelDesk() {
		return driverTravelDesk;
	}

	public void setDriverTravelDesk(boolean driverTravelDesk) {
		this.driverTravelDesk = driverTravelDesk;
	}

	public boolean isRemoteLocationAttendanceAllowed() {
		return isRemoteLocationAttendanceAllowed;
	}

	public void setRemoteLocationAttendanceAllowed(boolean isRemoteLocationAttendanceAllowed) {
		this.isRemoteLocationAttendanceAllowed = isRemoteLocationAttendanceAllowed;
	}

	public boolean isImeiNumberPresent() {
		return isImeiNumberPresent;
	}

	public void setImeiNumberPresent(boolean isImeiNumberPresent) {
		this.isImeiNumberPresent = isImeiNumberPresent;
	}
	
	public long getNoOfDaysToConfirm() {
		return noOfDaysToConfirm;
	}
	
	public void setNoOfDaysToConfirm(long noOfDaysToConfirm) {
		this.noOfDaysToConfirm = noOfDaysToConfirm;
	}
}
