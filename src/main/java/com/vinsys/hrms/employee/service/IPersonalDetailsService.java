package com.vinsys.hrms.employee.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationVO;
import com.vinsys.hrms.employee.vo.ContactNoVo;
import com.vinsys.hrms.employee.vo.EducationVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.exception.HRMSException;

public interface IPersonalDetailsService {

	HRMSBaseResponse<ProfileVO> getProfileDetails() throws HRMSException;

	HRMSBaseResponse<AddressDetailsRequestVo> getAddress(Long candidateId) throws HRMSException;

	HRMSBaseResponse<EducationVO> getEducationalDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<CertificationVO> getCertificationDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<HealthDetailsVO> getHealthDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<FamilyVO> getFamilyDetails(Long candidateId) throws HRMSException;

	HRMSBaseResponse<?> addEducationDetails(EducationalDetailsVO educationDetails) throws HRMSException;

	HRMSBaseResponse<?> addCertification(CertificationDetailsVO request) throws HRMSException;

	HRMSBaseResponse<?> addfamilyDetails(FamilyDetailsVO familyDetails) throws HRMSException;

	HRMSBaseResponse<?> updateContact(ContactNoVo request) throws HRMSException;

}
