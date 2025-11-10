package com.vinsys.hrms.directonboarding.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.directonboarding.vo.DirectOnboardingRequest;
import com.vinsys.hrms.util.HRMSHelper;

@Component
public class OnboardingRequestValidator {

	private Validator validator;

	public OnboardingRequestValidator(@Autowired Validator validator) {
		this.validator = validator;
	}

	public void validateOnboardingRequest(DirectOnboardingRequest request) {
		Set<ConstraintViolation<DirectOnboardingRequest>> violations = this.validator.validate(request, Default.class);
		if (!HRMSHelper.isNullOrEmpty(violations)) {
			violations.forEach(e -> {
				request.setValidationMessage(e.getMessage());
			});
		}
		if (violations.size() > 0) {
			throw new IllegalArgumentException(request.getValidationMessage());
		}
	}

}
