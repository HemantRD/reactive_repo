package com.vinsys.hrms.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.vinsys.hrms.entity.Employee;

public class HRMSFileuploadUtil {
	
	@Value("${rootDirectory}")
	private String rootDirectory;

	public static String directoryCreationForSeparationusingEmployeeId(String rootDirectory, Employee empEntity) {
		/*
		 * This is use to create directory using Employee Id This will create directory
		 * ORG->Division->Branch
		 * 
		 */
		
		Logger logger = LoggerFactory.getLogger(HRMSFileuploadUtil.class);

		if (!HRMSHelper.isNullOrEmpty(empEntity)) {
			try {
				// Sardine sardine = null;
				// sardine = SardineFactory.begin();

				long orgId = empEntity.getCandidate().getLoginEntity().getOrganization().getId();
				long divisionId = empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				long branchId = empEntity.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
				long candidateId = empEntity.getCandidate().getId();

				//String savePath = rootDirectory + orgId;
				String savePath = rootDirectory+ orgId;
				logger.info("savepath : " + savePath);
				String str = savePath;// .concat("/gallery") + "/" + file.getOriginalFilename().replaceAll("\\s+",
										// "_");
				Path path = Paths.get(savePath);

				if (!Files.exists(path)) {
					Files.createDirectory(Paths.get(path.toUri()));
				}

				str = path + File.separator + divisionId;
				Path p = Paths.get(str);
				logger.info("str : " + str);
				logger.info("path : " + p);

				if (!Files.exists(p)) {
					// Files.createDirectory(p);
					Files.createDirectory(Paths.get(p.toUri()));
					// str= p + "/" + divisionId;
				} // else {
					// str = p + "/" + divisionId;
					// }

				str = str + File.separator + branchId;
				Path p1 = Paths.get(str);
				logger.info("str : " + str);
				logger.info("path : " + p1);

				if (!Files.exists(p1)) {
					// Files.createDirectory();

					Files.createDirectory(Paths.get(p1.toUri()));

				}

				str = str + File.separator + candidateId;
				Path p2 = Paths.get(str);
				logger.info("str : " + str);
				logger.info("path : " + p2);

				if (!Files.exists(p2)) {

					Files.createDirectory(Paths.get(p2.toUri()));

				}
				str = p2 + File.separator + IHRMSConstants.SEPARATIONFOLDERNAME;
				Path p3 = Paths.get(str);
				logger.info("str : " + str);
				logger.info("path : " + p3);

				if (!Files.exists(p3)) {

					Files.createDirectory(Paths.get(p3.toUri()));

				}

				/*
				 * if (!sardine.exists(savePath.concat("/" +
				 * IHRMSConstants.SEPARATIONFOLDERNAME))) {
				 * sardine.createDirectory(savePath.concat("/" +
				 * IHRMSConstants.SEPARATIONFOLDERNAME)); savePath = savePath + "/" +
				 * IHRMSConstants.SEPARATIONFOLDERNAME; } else { savePath = savePath + "/" +
				 * IHRMSConstants.SEPARATIONFOLDERNAME; }
				 */
				return str;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String directorycreationforTravelusingEmployeeId(String baseURL, Employee empEntity, String travelId,
			String travelType) {

		/**
		 * This is use to create directory using Employee Id This will create directory
		 * ORG->Division->Branch
		 * 
		 */

		if (!HRMSHelper.isNullOrEmpty(empEntity)) {
			try {
				// Sardine sardine = null;
				// sardine = SardineFactory.begin();

				long orgId = empEntity.getCandidate().getLoginEntity().getOrganization().getId();
				// long
				// divisionId=empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				// long
				// branchId=empEntity.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
				// long candidateId=empEntity.getCandidate().getId();

				String savePath = "E:" +File.separator +"input"+File.separator+ orgId;
				
				String str = savePath;
				Path path = Paths.get(str);

				if (!Files.exists(path)) {
					Files.createDirectory(Paths.get(path.toUri()));
				}
				str = path + File.separator + IHRMSConstants.TRAVELFOLDERNAME;
				Path p = Paths.get(str);

				if (!Files.exists(p)) {
					Files.createDirectory(Paths.get(p.toUri()));
				}

				str = p + File.separator + travelId;
				Path p1 = Paths.get(str);
				
				if (!Files.exists(p1)) {
					Files.createDirectory(Paths.get(p1.toUri()));
				}
				
				str = p1 + File.separator + travelType;
				Path p2 = Paths.get(str);
				
				if (!Files.exists(p2)) {
					Files.createDirectory(Paths.get(p2.toUri()));
				}
				
				
				return str;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
