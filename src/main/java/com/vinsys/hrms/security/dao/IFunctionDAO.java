package com.vinsys.hrms.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.security.entity.Function;

public interface IFunctionDAO extends JpaRepository<Function, Long> {

}
