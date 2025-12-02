package com.vinsys.hrms.directonboard.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CurrentEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.DeleteFileRequestVO;
import com.vinsys.hrms.directonboard.vo.EmailCheckVo;
import com.vinsys.hrms.directonboard.vo.FileUploadRequestVO;
import com.vinsys.hrms.directonboard.vo.PreviousEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailListVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.directonboard.vo.ValidationVO;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.EmpIdVo;

public interface IDirectOnBoardService {

	HRMSBaseResponse<?> addProfileDetails(ProfileDetailsRequestVO request) throws HRMSException, ParseException, NoSuchAlgorithmException;
	
	HRMSBaseResponse<List<ProfileDetailListVO>> getAllEmployeeList(String keyword, Pageable pageable)throws HRMSException;
//	HRMSBaseResponse<List<ProfileDetailListVO>> getAllEmployeeList(Pageable pageable) throws HRMSException;
	

	HRMSBaseResponse<ProfileDetailVO> getProfileDetail(Long candidateId)throws HRMSException;	

	HRMSBaseResponse<?> saveBankDetail(BankDetailsVO bankDetail) throws HRMSException;

	HRMSBaseResponse<?> addEducationDetails(EducationalDetailsVO educationDetailsVo)
			throws HRMSException, ParseException;

	HRMSBaseResponse<?> addCertificationDetails(CertificationDetailsVO certificationDetails) throws HRMSException;

	HRMSBaseResponse<?> addPreviousEmployment(PreviousEmploymentRequestVO request) throws HRMSException, ParseException;

	HRMSBaseResponse<?> saveFamilyDetail(FamilyDetailsVO familyDetails) throws HRMSException;

	HRMSBaseResponse<?> addCurrentEmployment(CurrentEmploymentRequestVO request)
			throws HRMSException, ParseException, NoSuchAlgorithmException;

	HRMSBaseResponse<?> saveHealthDetail(HealthDetailsVO healthDetail) throws HRMSException;

	HRMSBaseResponse<?> addAddressDetails(AddressDetailsRequestVo addressVo)throws HRMSException;

	
	HRMSBaseResponse<?> deleteProfile(Long candidateId)throws HRMSException;
	
	HRMSBaseResponse<ValidationVO> getDirectOnboardingValidationFlags(Long candidateId) throws HRMSException;

	HRMSBaseResponse<?> uploadFile(MultipartFile[] request, FileUploadRequestVO requestVO) throws HRMSException, IOException;
	
	HRMSBaseResponse<?> deleteFile(DeleteFileRequestVO request) throws HRMSException,IOException;

	

	HRMSBaseResponse<?> uploadExcelFile(MultipartFile file) throws HRMSException, IOException, NoSuchAlgorithmException;

	HRMSBaseResponse<?> candidateEmailIdCheck(EmailCheckVo request) throws HRMSException, IOException;

	HRMSBaseResponse<?> employeeEmailIdCheck(EmailCheckVo request) throws HRMSException, IOException;

	HRMSBaseResponse<?> employeeCodeCheck(EmailCheckVo request) throws HRMSException, IOException;

	HRMSBaseResponse<?> deleteEmployee(EmpIdVo request) throws HRMSException, IOException;
	
}
