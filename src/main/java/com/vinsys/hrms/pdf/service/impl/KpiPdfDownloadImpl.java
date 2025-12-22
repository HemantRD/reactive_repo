package com.vinsys.hrms.pdf.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.vinsys.hrms.constants.ELogo;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraWfDao;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraWf;
import com.vinsys.hrms.kra.service.IKraService;
import com.vinsys.hrms.kra.vo.KraCategoryVo;
import com.vinsys.hrms.kra.vo.KraObjectiveVo;
import com.vinsys.hrms.kra.vo.KraSubcategoryVo;
import com.vinsys.hrms.kra.vo.NewKraResponse;
import com.vinsys.hrms.logo.entity.Logo;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.pdf.service.IKpiPdfDownloade;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class KpiPdfDownloadImpl implements IKpiPdfDownloade {

	@Autowired
	IKraService kraService;
	
	@Autowired
	LogoService logoService;
	
	@Autowired
	IKraDao kraDAO;
	
	@Autowired
	IKraWfDao kraWfDAO;
	
	@Autowired
	IHRMSEmployeeDAO employeeDAO;


	 // =================== PDF Generation ===================

	public void generatePDFFromHTML(InputStream inputStream, OutputStream outputStream)
			throws DocumentException, IOException {
		Document document = new Document();
		document.setPageSize(PageSize.A4);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, bos);
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);
		document.close();
		ByteArrayOutputStream newPDF = new ByteArrayOutputStream();
		manipulatePdf(newPDF, bos);
		outputStream.write(newPDF.toByteArray());

	}

	//Add watermark/background image to each page
	 public void manipulatePdf(ByteArrayOutputStream out, ByteArrayOutputStream in) 
	            throws IOException, DocumentException {

	        PdfReader reader = new PdfReader(new ByteArrayInputStream(in.toByteArray()));
	        int n = reader.getNumberOfPages();
	        PdfStamper stamper = new PdfStamper(reader, out);

	        // Load image using ResourceLoader
	      //  Resource resource = resourceLoader.getResource("classpath:images/watermark.png"); // adjust path
	        byte[] imgBytes;
	       // Image img = Image.getInstance(imgBytes);

	       // float w = img.getScaledWidth();
	        //float h = img.getScaledHeight();

	        PdfGState gs1 = new PdfGState();
	        gs1.setFillOpacity(0.5f);  // Set transparency

	        PdfContentByte over;
	        Rectangle pagesize;

	        for (int i = 1; i <= n; i++) {
	            pagesize = reader.getPageSizeWithRotation(i);
	            over = stamper.getOverContent(i); // overlay watermark
	            over.saveState();
	            over.setGState(gs1);

	            // Center the watermark
	            float x = (pagesize.getLeft() + pagesize.getRight()) / 2;
	            float y = (pagesize.getTop() + pagesize.getBottom()) / 2;

	          //  over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
	            over.restoreState();
	        }

	        stamper.close();
	    }
	
	 
		public String kpiPdfDownload(String template, Long kraid) throws HRMSException {


		    MustacheFactory mf = new DefaultMustacheFactory();
		    Mustache m = mf.compile(template);

		    HRMSBaseResponse<NewKraResponse> response = kraService.getNewKraById(kraid);
		    if (HRMSHelper.isNullOrEmpty(response)) {
		        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		    }

		    NewKraResponse kra = response.getResponseBody();
		    String base64Logo = "";

		    try {
		        Path path = Paths.get("C:/Projects/ImageLogo/LogoImg.jpg");
		        byte[] imageBytes = Files.readAllBytes(path);
		        base64Logo = "data:image/jpeg;base64," 
		                        + Base64.getEncoder().encodeToString(imageBytes);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    // --------------------------
		    // Prepare context
		    // --------------------------
		    Map<String, Object> context = new HashMap<>();

		    context.put("cycleType", valueOrDash(kra.getCycleType() != null ? kra.getCycleType().toString() : "-"));
		    context.put("fdhAiComment", htmlToPlain(kra.getFdhAiComment()));
		    context.put("functionSpecific", htmlToPlain(kra.getFunctionSpecific()));
		    context.put("coreCompetencies", htmlToPlain(kra.getCoreCompetencies()));
		    context.put("leadershipCompetencies", htmlToPlain(kra.getLeadershipCompetencies()));
		    context.put("overallSummary", htmlToPlain(kra.getOverallSummary()));
		    context.put("categoryWeight", valueOrDash(kra.getCategoryWeight()));
		    context.put("formCycleName", valueOrDash(kra.getFormCycleName()));
		    //context.put("totalSelfRating", valueOrDash(kra.getTotalSelfRating()));
		    //context.put("HcdAndCalibration", valueOrDash(kra.getFinalRating()));
		    context.put("logoBase64", base64Logo);
		    
		    

		    // --------------------------
		    // Workflow status
		    // --------------------------
		    KraWf workFlow = kraWfDAO.findByKraId(kraid);
		    String workflowStatus = (workFlow != null && workFlow.getStatus() != null) 
		        ? workFlow.getStatus() 
		        : "";

		    if (IHRMSConstants.COMPLETED.equalsIgnoreCase(workflowStatus)) {
		        context.put("totalSelfRating", valueOrDash(kra.getTotalSelfRating()));
		    } else {
		        context.put("totalSelfRating", "-");
		    }
		    
		    if (IHRMSConstants.COMPLETED.equalsIgnoreCase(workflowStatus)) {
		        context.put("totalManagerRating", valueOrDash(kra.getTotalManagerRating()));
		    } else {
		        context.put("totalManagerRating", "-");
		    }
		    
		    if (IHRMSConstants.COMPLETED.equalsIgnoreCase(workflowStatus)) {
		        context.put("HcdAndCalibration", valueOrDash(kra.getFinalRating()));
		    } else {
		        context.put("HcdAndCalibration", "-");
		    }

		    // --------------------------
		    // Employee Name
		    // --------------------------
		    Kra kpi = kraDAO.findByIdAndIsActive(kraid, ERecordStatus.Y.name());
		    if (!HRMSHelper.isNullOrEmpty(kpi)) {
		        Long employeeId = kpi.getEmployeeId();
		        if (employeeId != null) {
		            Employee employee = employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
		            context.put("teamMemberName",
		                valueOrDash(
		                    employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName()
		                )
		            );
		        }
		    }

		    // --------------------------
		    // Flatten Goals
		    // --------------------------
		    List<Map<String, Object>> goals = new ArrayList<>();

		    if (kra.getCategory() != null && !kra.getCategory().isEmpty()) {

		        for (KraCategoryVo category : kra.getCategory()) {

		            String categoryName = valueOrDash(category.getName());
		            String categoryWeight = valueOrDash(category.getCategoryweight());

		            if (category.getSubcategory() != null && !category.getSubcategory().isEmpty()) {

		                for (KraSubcategoryVo sub : category.getSubcategory()) {

		                    String subCategoryName = valueOrDash(sub.getName());

		                    if (sub.getObjectives() != null && !sub.getObjectives().isEmpty()) {

		                        for (KraObjectiveVo obj : sub.getObjectives()) {

		                            Map<String, Object> goalMap = new HashMap<>();
		                            goalMap.put("categoryName", categoryName);
		                            goalMap.put("categoryWeight", categoryWeight);
		                            goalMap.put("subCategoryName", subCategoryName);
		                            goalMap.put("goalTitle", valueOrDash(obj.getName()));
		                            goalMap.put("goalWeight", valueOrDash(obj.getObjectiveweight()));
		                            goalMap.put("metric", htmlToPlain(obj.getMetric()));

		                            // -------- Self Rating --------

									goalMap.put("selfRating",
											(obj.getSelfrating() != null && obj.getSelfrating().getLabel() != null)
													? valueOrDash(obj.getSelfrating().getLabel())
													: "-");

									goalMap.put("selfComments", htmlToPlain(obj.getSelfqualitativeassessment()));
		                           
		                            // -------- Manager Rating --------
		                            if (IHRMSConstants.COMPLETED.equalsIgnoreCase(workflowStatus)) {

		                                goalMap.put("managerRating",
		                                    (obj.getManagerrating() != null && obj.getManagerrating().getLabel() != null)
		                                        ? valueOrDash(obj.getManagerrating().getLabel())
		                                        : "-"
		                                );

		                                goalMap.put("managerComments",
		                                    htmlToPlain(obj.getManagerqaulitativeassisment())
		                                );

		                            } else {
		                                goalMap.put("managerRating", "-");
		                                goalMap.put("managerComments", "-");
		                            }

		                            goals.add(goalMap);
		                        }
		                    }
		                }
		            }
		        }
		    }

		    context.put("goals", goals);

		    // --------------------------
		    // Render Mustache Template
		    // --------------------------
		    StringWriter out = new StringWriter();
		    m.execute(out, context);
		    out.flush();

		    return out.toString();
		}

		private String htmlToPlain(String html) {
		    if (html == null)
		        return "-";

		    // ---------- LIST HANDLING ----------
		    html = html.replaceAll("(?i)\\s*<li[^>]*>", "\n• ");
		    html = html.replaceAll("(?i)</li>", "");

		    html = html.replaceAll("(?i)<ul[^>]*>", "");
		    html = html.replaceAll("(?i)</ul>", "");

		    // FORCE NEWLINE AFTER "My comments:"
		    html = html.replaceAll("(?i)my comments[:]*", "My comments:\n");

		    // ---------- PARAGRAPH HANDLING ----------
		    html = html.replaceAll("(?i)<br */?>", "\n");
		    html = html.replaceAll("(?i)</p>", "\n");
		    html = html.replaceAll("(?i)<p[^>]*>", "");

		    // ---------- REMOVE FORMAT TAGS ----------
		    html = html.replaceAll("(?i)</?b>", "");
		    html = html.replaceAll("(?i)</?strong>", "");
		    html = html.replaceAll("(?i)</?i>", "");
		    html = html.replaceAll("(?i)</?em>", "");

		    // ---------- REMOVE ANY OTHER TAGS ----------
		    html = html.replaceAll("<[^>]*>", "");

		    return html.trim();
		}


	 
	 
//		private String htmlToPlain(String html) {
//		    if (html == null) return "-";
//
//		    // Convert line breaks
//		    html = html.replaceAll("(?i)<br */?>", "\n");
//		    html = html.replaceAll("(?i)</p>", "\n");
//		    html = html.replaceAll("(?i)<p[^>]*>", "");
//
//		    // Remove all remaining HTML tags
//		    html = html.replaceAll("<[^>]*>", "");
//
//		    return html.trim();
//		}


	private String valueOrDash(Object obj) {
	    return (obj != null && !obj.toString().trim().isEmpty()) ? obj.toString() : "-";
	}


	// Generate PDF bytes from HTML content
	public byte[] generatePDFFromHTML(String html) throws DocumentException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		generatePDFFromHTML(new ByteArrayInputStream(html.getBytes()), outputStream);
		return outputStream.toByteArray();
	}
	
	private void addLogoToFirstPage(PdfStamper stamper) throws HRMSException, IOException, DocumentException {
		Logo logo = logoService.getConfig(ELogo.LOGO.name());
		if (ObjectUtils.isEmpty(logo)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		File logoFile = new File(logo.getValue());
		if (!logoFile.exists()) {
			throw new HRMSException(1201, "Logo file not found at path: " + logo.getValue());
		}

		try (InputStream logoStream = new FileInputStream(logoFile)) {
			byte[] logoBytes = IOUtils.toByteArray(logoStream);
			Image img = Image.getInstance(logoBytes);

			PdfContentByte canvas = stamper.getOverContent(1);
			Rectangle pageSize = stamper.getReader().getPageSize(1);

			// Top-right corner with 20px margin
			float x = pageSize.getRight() - img.getScaledWidth() - 20;
			float y = pageSize.getTop() - img.getScaledHeight() - 20;
			img.setAbsolutePosition(x, y);
			canvas.addImage(img);
		}
	}

}
