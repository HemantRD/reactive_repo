package com.vinsys.hrms.services.hractivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.dao.dashboard.IHRMSEventImagesDAO;
import com.vinsys.hrms.dao.hractivity.IHRMSEventDAO;
import com.vinsys.hrms.datamodel.VOImageUploadResponse;
import com.vinsys.hrms.entity.dashboard.EventImages;
import com.vinsys.hrms.entity.dashboard.Events;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * 
 * @author monika
 *
 */
@Hidden @RestController
@RequestMapping(path = "/hrActivityAction")

public class ImageUploadService {

	Logger logger = LoggerFactory.getLogger(ImageUploadService.class);

	@Autowired
	IHRMSEventImagesDAO eventImageDAO;

	@Autowired
	IHRMSEventDAO eventDAO;

	@Value("${base.webdav.url}")
	private String baseURL;

	@Value("${MaxFileUploadSize}")
	private long MaxSize;
	
	@Value("${rootDirectory}")
	private String rootDirectory;

	private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|jpeg))$)";

	public static boolean patternMatches(String input, String regexPattern) {
		return Pattern.compile(regexPattern).matcher(input).matches();
	}

	@GetMapping("/getAllImages")
	@ResponseBody
	public List<EventImages> getImages() {
		try {
			List<EventImages> eventList = eventImageDAO.findByEventsEventNameAndIsActiveOrderByUpdatedDateDesc("Upload Image",
					IHRMSConstants.isActive);
			if (eventList != null && !eventList.isEmpty()) {
				return eventList;
			} else {
				logger.info("No event images Completion events");
				// throw new HRMSException(IHRMSConstants.DataNotFoundCode,
				// IHRMSConstants.DataNotFoundMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@DeleteMapping("deleteImage")
	@ResponseBody
	public String deleteImage(@RequestParam("imageId") Long imageList, String orgId) throws IOException {
		try {
			//Sardine sardine = null;
			//sardine = SardineFactory.begin();
			//String savePath = "E:" + File.separator+"input" +File.separator+ orgId;
			
			String savePath = rootDirectory + orgId;
			
			
			
			EventImages eventImage = eventImageDAO.findById(imageList).get();
			String imagePath = savePath + File.separator + eventImage.getImagePath();
			
			Path path = Paths.get(imagePath);
			if (Files.exists(path)) {
				Files.delete(path);
				eventImageDAO.deleteById(imageList);
				return HRMSHelper.createJsonString("Delete Image SuccessFully..!!");
			} else {
				return HRMSHelper.createJsonString("Image is Not Present");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/saveImage")
	public String test1(@RequestParam("file") MultipartFile[] request, String orgId, String createdBy)
			throws IOException {
		StringBuffer duplicateMessage = new StringBuffer();
		StringBuffer validation = new StringBuffer();
		StringBuffer response = new StringBuffer();
		VOImageUploadResponse imageResponse = new VOImageUploadResponse();
		int count = 0;
		try {
			if (HRMSHelper.isNullOrEmpty(request)) {
				throw new HRMSException(IHRMSConstants.FileUploadCode, IHRMSConstants.FileUploadErrorMessage);
			}
//			Sardine sardine = null;
//			sardine = SardineFactory.begin();
			String savePath = rootDirectory + orgId + "/gallery";

			// logger.info("BaseURL : " + baseURL);
			logger.info("savepath : " + savePath);

			for (MultipartFile file : request) {
				logger.info("File  " + file.getName());
				logger.info("File name " + file.getOriginalFilename());
				logger.info("File size " + file.getSize());

				byte[] bytes = file.getBytes();

				String str = "";

				if (!(file.getSize() >= 2000000)
						&& ImageUploadService.patternMatches(file.getOriginalFilename(), IMAGE_PATTERN)) {

					str = savePath.concat("/" + file.getOriginalFilename().replaceAll("\\s+", "_"));
					logger.info("str : " + str);

					Path path = Paths.get(str);

					if (!Files.exists(path)) {
//						sardine.put(str, bytes);
						logger.info("str :{}" + str);
						
						logger.info("savePath :{}" + savePath);

						Files.write(Paths.get(savePath, file.getOriginalFilename()), file.getBytes());
						logger.info("str :{}" + str);

						Events event = eventDAO.findByEventNameAndIsActive("Upload Image", IHRMSConstants.isActive);
						EventImages image = new EventImages();
						image.setEvents(event);
						image.setIsActive("Y");
						image.setImagePath("gallery" + "/" + file.getOriginalFilename().replaceAll("\\s+", "_"));
						image.setCreatedBy(createdBy);
						image.setImageTitle("Upload Image");
						// Added Rushikesh
						image.setUpdatedDate(new Date());

						EventImages savedImage = eventImageDAO.save(image);
						if (savedImage.getImagePath() != null) {
							count = count + 1;
						}
					} else {
						duplicateMessage = duplicateMessage
								.append(file.getOriginalFilename().replaceAll("\\s+", "_") + ",");
						logger.error("File Already Exists: " + file.getOriginalFilename().replaceAll("\\s+", "_"));
					}
				} else {
					validation.append(file.getOriginalFilename().replaceAll("\\s+", "_") + ",");
					logger.error("only accept jpg and png file format or can't upload file more than 2mb size");
				}
			}

			if (count > 0) {
				response.append(count + " Images are Uploaded Successfully");
			}
			if (duplicateMessage.length() != 0) {
				String s = new String(duplicateMessage);
				String[] myarray = s.split(",");
				response.append("\nBelow File name Already Exists !");
				for (int i = 0; i < myarray.length; i++) {
					response.append("\n" + myarray[i] + ",");
				}
			}
			if (validation.length() != 0) {
				String s = new String(validation);
				String[] myarray = s.split(",");
				response.append("\nBelow File name is not in png or jpg format");
				for (int i = 0; i < myarray.length; i++) {
					response.append("\n" + myarray[i] + ",");
				}
			}

			imageResponse.setCountOfSuccessFullyImages(count);
			imageResponse.setFailureImageName(duplicateMessage);
			imageResponse.setValidationImageName(validation);
			imageResponse.setResponseCode(IHRMSConstants.successCode);
			imageResponse.setResponseMessage("Image Upload SuccessFully..!!");
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
