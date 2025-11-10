package com.vinsys.hrms.services;

import java.io.File;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.dashboard.IHRMSEventImagesDAO;
import com.vinsys.hrms.datamodel.Events;
import com.vinsys.hrms.entity.dashboard.EventImages;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.JWTTokenHelper;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/dashboard")

public class DashboardSevices {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	@Value("${rootDirectory}")
	private String rootDirectory;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	
	@Autowired
	IHRMSEventImagesDAO eventImagesDAO;

	@RequestMapping(value = "birthdays/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getBirthdayEvents(@PathVariable("orgId") long orgId) {

		List<Object[]> result = employeeDAO.upcomingBirthdays(orgId);	
		List<Events> events = new ArrayList<>();
		try {
		if (result != null && !result.isEmpty()) {
			logger.info("Birthday events Found ");
			for (Object[] resultSet : result) {
				Events event = new Events();
				event.setFirstName(String.valueOf(resultSet[0]));
				event.setLastName(String.valueOf(resultSet[1]));
				event.setActualDate(String.valueOf(resultSet[2]));
				event.setEventDate(String.valueOf(resultSet[3]));
				event.setEmailId(String.valueOf(resultSet[4]));
				event.setPhoto(String.valueOf(resultSet[5]));
				event.setCandidateId(String.valueOf(resultSet[6]));
				event.setBranchId(String.valueOf(resultSet[7]));
				event.setDivisionId(String.valueOf(resultSet[8]));
				event.setOrgId(String.valueOf(resultSet[9]));
				event.setEmpId(String.valueOf(resultSet[10]));
				
				events.add(event);
			}
			
			return HRMSHelper.createJsonString(events);
		} else {
			logger.info("No Birthday events");
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		}catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}
	

	@RequestMapping(value = "servicecompletion/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getServiceCompletionEvents(@PathVariable("orgId") long orgId) {

		List<Object[]> result = employeeDAO.upcomingServiceCompletions(orgId);	
		List<Events> events = new ArrayList<>();
		try {
		if (result != null && !result.isEmpty()) {
			logger.info("Service Completion events Found");
			for (Object[] resultSet : result) {
				Events event = new Events();
				event.setFirstName(String.valueOf(resultSet[0]));
				event.setLastName(String.valueOf(resultSet[1]));
				event.setActualDate(String.valueOf(resultSet[2]));
				event.setEventDate(String.valueOf(resultSet[3]));
				event.setEmailId(String.valueOf(resultSet[4]));
				event.setPhoto(String.valueOf(resultSet[5]));
				event.setCandidateId(String.valueOf(resultSet[6]));
				event.setBranchId(String.valueOf(resultSet[7]));
				event.setDivisionId(String.valueOf(resultSet[8]));
				event.setOrgId(String.valueOf(resultSet[9]));
				event.setEmpId(String.valueOf(resultSet[10]));

				events.add(event);
			}
			
			return HRMSHelper.createJsonString(events);
		} else {
			logger.info("No Service Completion events");
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		}catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	

	@RequestMapping(value = "gallery/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getGalleryImages(@PathVariable("orgId") long orgId) {
		try {
		List<EventImages> evtImages = new ArrayList<>();
		evtImages =	eventImagesDAO.getEventImages(orgId,LocalDate.now(),"Y");	
		
		if (evtImages != null && !evtImages.isEmpty()) {
			
			return HRMSHelper.createJsonString(evtImages);
		} else {
			logger.info("No event images Completion events");
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		}catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	
	//*********************
	
	
	@RequestMapping(value = "/imageView/{letterUrl}", method = RequestMethod.GET)
	public ResponseEntity<Resource> imageView(@RequestParam(required = true) String letterUrl,
			HttpServletRequest servletRequest) throws HRMSException, MalformedURLException {

		String token = JWTTokenHelper.parseJwt(servletRequest);
		Claims loggedInEmpDetails;
		if (!HRMSHelper.isNullOrEmpty(token)) {
			loggedInEmpDetails = JWTTokenHelper.getLoggedInEmpDetail(token);
		} else {
			throw new HRMSException(IHRMSConstants.failedCode, IHRMSConstants.TOKEN_NOT_VALID);
		}

		if (!HRMSHelper.isNullOrEmpty(letterUrl)) {
            int orgId=1;
			String url = rootDirectory + orgId +  letterUrl;
			File file = new File(url);
			logger.info("Document Path:" + url);
			
			
			if (file.exists()) {
				Resource resource = new UrlResource(file.toURI());
	            HttpHeaders headers = new HttpHeaders();
	            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
	            headers.add(HttpHeaders.PRAGMA, "no-cache");
	            headers.add(HttpHeaders.EXPIRES, "0");

	          
	            MediaType mediaType = MediaType.ALL; 
	            headers.setContentType(mediaType);

	            return ResponseEntity.ok().headers(headers).body(resource);

				
			}

		} else {

			new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		return ResponseEntity.badRequest().build();
	}

}
