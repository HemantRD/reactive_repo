package com.vinsys.hrms.directonboard.service.processors;

import com.vinsys.hrms.security.SecurityFilter;

public class DirectOnBoardProcessorFactory {

	public IDirectOnboardProcessor getDirectOnBoardProcessor(Long orgid,
			DirectOnBoardingDependencies dependencies) {
		if (orgid.equals(SecurityFilter.TL_CLAIMS.get().getOrgId())) {
			return new DirectOnBoardProcessor(dependencies);
		} else if (orgid == 2) {
			return new LryptDirectOnBoardingProcessor(dependencies);
		} else {
			throw new RuntimeException("No Such Organization Processor Exists");
		}
	}

	
}
