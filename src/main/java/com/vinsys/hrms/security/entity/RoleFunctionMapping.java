package com.vinsys.hrms.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_role_function")
public class RoleFunctionMapping {

	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "role_id")
	private Long roleId;
	@Column(name = "function_id")
	private Long functionId;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "function_id", insertable = false, updatable = false)
	private List<Function> functions;

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

}
