package com.vinsys.hrms.logo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.logo.entity.Logo;

public interface ILogoDAO extends JpaRepository<Logo, Long>{

}
