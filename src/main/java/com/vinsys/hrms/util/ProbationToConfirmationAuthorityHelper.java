package com.vinsys.hrms.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.dao.confirmation.IHRMSMasterParameterName;
import com.vinsys.hrms.employee.vo.ProbationFeedbackVO;
import com.vinsys.hrms.employee.vo.ProbationParameterVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;
import com.vinsys.hrms.exception.HRMSException;

@Service
public class ProbationToConfirmationAuthorityHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IHRMSMasterParameterName parameterDao;

	public void saveProbationFeedbackInputValidation(ProbationFeedbackVO request, List<String> roles,
			Employee loggedInEmployee, Employee empOnProbation) throws HRMSException {

		int probationParameterCount = 0;

		List<MasterEvaluationParameter> parameterList = parameterDao.findAllParameterNames(IHRMSConstants.isActive,
				loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId());

		log.info("::" + parameterList.size());
		if (HRMSHelper.isNullOrEmpty(request)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		if (HRMSHelper.isNullOrEmpty(request.getProbationParameter())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		if ((HRMSHelper.isRolePresent(roles, ERole.EMPLOYEE.name())
				|| HRMSHelper.isRolePresent(roles, ERole.MANAGER.name()))
				&& loggedInEmployee.getId().equals(empOnProbation.getId())) {

			if (!HRMSHelper.isNullOrEmpty(request.getProbationParameter())) {
				for (ProbationParameterVO parameter : request.getProbationParameter()) {
					if (HRMSHelper.isLongZero(parameterList.stream()
							.filter(e -> e.getId().equals(parameter.getParameterValue().getId())).count())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
					}

					// check maxRating
					if (HRMSHelper.isLongZero(
							parameterList.stream().filter(e -> e.getMaxRating() >= parameter.getEmpRating()).count())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Employee rating");
					}

					if (parameter.getEmpRating() <= 0) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Employee rating");
					}

					if (HRMSHelper.isNullOrEmpty(parameter.getEmpRating())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Employee rating");
					}
					if (HRMSHelper.isNullOrEmpty(parameter.getEmployeeComment())) {
						throw new HRMSException(1500,
								ResponseCode.getResponseCodeMap().get(1501) + " Employee comment");
					}
					if (!HRMSHelper.isNullOrEmpty(parameter.getEmployeeComment())) {
						if (!HRMSHelper.validateComment(parameter.getEmployeeComment())) {
							throw new HRMSException(1500,
									ResponseCode.getResponseCodeMap().get(1501) + " Employee comment");
						}
					}
					probationParameterCount++;
				}
			}

			if (probationParameterCount < parameterList.size()) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Parameter mismatch");
			}

		} else if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {

			if (!HRMSHelper.isNullOrEmpty(request.getProbationParameter())) {
				for (ProbationParameterVO parameter : request.getProbationParameter()) {

					if (HRMSHelper.isLongZero(parameterList.stream()
							.filter(e -> e.getId().equals(parameter.getParameterValue().getId())).count())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Parameter Id");
					}
					// check maxRating
					if (HRMSHelper.isLongZero(parameterList.stream()
							.filter(e -> e.getMaxRating() >= parameter.getManagerRating()).count())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + "Manager rating");
					}

					if (parameter.getManagerRating() <= 0) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + "Manager rating");
					}

					if (HRMSHelper.isNullOrEmpty(parameter.getManagerRating())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Manager rating");
					}
					if (HRMSHelper.isNullOrEmpty(parameter.getManagerComment())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Manager comment");
					}
					probationParameterCount++;
				}
			}
			if (HRMSHelper.isNullOrEmpty(request.getManagerComment())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " : Comment field is mandatory.");
			}
			if (HRMSHelper.isNullOrEmpty(request.getEmployeeId()) || HRMSHelper.isLongZero(request.getEmployeeId())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " RO submitted");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getManagerComment())) {
				if (!HRMSHelper.validateComment(request.getManagerComment())) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " : Comment field input is invalid.");
				}
			}
			if (probationParameterCount < parameterList.size()) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Parameter mismatch");
			}

		} else if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			if (HRMSHelper.isNullOrEmpty(request.getHrComment())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " HR comment");
			}
			if (HRMSHelper.isNullOrEmpty(request.getExtendedBy())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Extended by");
			}
			if (!HRMSHelper.isNullOrEmpty(request.getHrComment())) {
				if (!HRMSHelper.validateComment(request.getHrComment())) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Manager comment");
				}

			}

		}

	}

}
