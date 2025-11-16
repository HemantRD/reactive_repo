package com.vinsys.hrms.directonboarding.services;

import com.vinsys.hrms.directonboarding.vo.DirectOnboardingRequest;
import com.vinsys.hrms.directonboarding.vo.DirectOnboardingResponse;

public interface IDirectOnboardingService {

	public DirectOnboardingResponse onboard(DirectOnboardingRequest request);
}
