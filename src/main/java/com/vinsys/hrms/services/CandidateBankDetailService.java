package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSBankDetailsDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.datamodel.BankDetailsVO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/candidateBankDetail")

public class CandidateBankDetailService {
	
	@Autowired
	IHRMSBankDetailsDAO bankDAO;
	
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(CandidateBankDetailService.class);
	
	/**
	 * This REST Service will Create candidate certification details ,
	 * 
	 * @param VOCandidateCertification
	 * @return Success or Error JSON
	 * @author shome.nitin
	 */
	@RequestMapping(method = { RequestMethod.POST }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCandidateBankDetails(@RequestBody BankDetailsVO bankDetail) {

	try {
		if (!HRMSHelper.isNullOrEmpty(bankDetail)) {
			
			if(!HRMSHelper.isNullOrEmpty(bankDetail.getAccountNumber()) &&
			!HRMSHelper.isNullOrEmpty(bankDetail.getBankName())&&
			!HRMSHelper.isNullOrEmpty(bankDetail.getBranchLocation()) &&
			!HRMSHelper.isNullOrEmpty(bankDetail.getFullName())  &&
			!HRMSHelper.isNullOrEmpty(bankDetail.getIfscCode())  &&
			!HRMSHelper.isNullOrEmpty(bankDetail.getMobileNumber())  &&
			!HRMSHelper.isNullOrEmpty(bankDetail.getBankId()))
			{
			
			Candidate candidate = candidateDAO.findById(bankDetail.getCandidateProfessionalDetail().getCandidate().getId()).get();
			
			
			
				BankDetails bank = new BankDetails();
				
				bank.setAccountNumber(bankDetail.getAccountNumber());
				bank.setBankName(bankDetail.getBankName());
				bank.setBranchLocation(bankDetail.getBranchLocation());
				bank.setIfscCode(bankDetail.getIfscCode());
				bank.setNameAsPerBank(bankDetail.getFullName());
				bank.setPhoneNumber(bankDetail.getMobileNumber());
				bank.setCandidate(candidate);
				bankDAO.save(bank);
				
				BaseId response = new BaseId();
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.addedsuccessMessage);
				return HRMSHelper.createJsonString(response);
				
				
				
			}else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		}
	}catch (HRMSException e) {
		e.printStackTrace();
		try {
			return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	} catch (Exception unknown) {
		try {
			unknown.printStackTrace();
			return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	return null;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCandidateBankDetails(@PathVariable("id") long candidateId ) {

		try {
			HRMSListResponseObject response = null;
			Candidate candidate = candidateDAO.findById(candidateId).get();
			BankDetails bank = bankDAO.findById(candidate.getBank().getId()).get();
			
			if (bank != null) {
				
				response = new HRMSListResponseObject();
				Set<BankDetails> bankList = new HashSet<>();
				bankList.add(bank);
				List<Object> bankListMiodel = new ArrayList<Object>();

				for (BankDetails bankVO : bankList) {
					BankDetailsVO model = HRMSEntityToModelMapper.convertToBankDetailsToModel(bankVO);
					bankListMiodel.add(model);

				}
				response.setListResponse(bankListMiodel);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
			} else {

				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

			return HRMSHelper.createJsonString(response);

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
}
