package com.vinsys.hrms.directonboard.service.processors;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.exception.HRMSException;

public class DirectOnBoardProcessor extends AbstractDirectOnBoardingProcessor {

	private Logger log = LoggerFactory.getLogger(DirectOnBoardProcessor.class);
	private DirectOnBoardingDependencies processorDependencies;

	public DirectOnBoardProcessor(DirectOnBoardingDependencies detailsProcessorDependencies) {
		super(detailsProcessorDependencies);
		this.processorDependencies = detailsProcessorDependencies;
	}

}
