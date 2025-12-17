package com.vinsys.hrms.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.vinsys.hrms.exception.HRMSException;

/**
 * PDF Generation utility class
 * 
 * @author shome.nitin
 */
@Component
public class PDFUtility {

	private static final Logger logger = LoggerFactory.getLogger(PDFUtility.class);

	/**
	 * This method will create PDF file from given HTML templates
	 * 
	 * @param String,String,String
	 * @return True for successful creation ,False if PDF Creation failed
	 * @author shome.nitin
	 * @throws HRMSException
	 */
	public static boolean createPdf(String templateName, String path, String fileName) {

		boolean result = false;
		try {
			logger.info(" == >> Processing template and Creating PDF << == ");
			logger.info("File Name : " + fileName);
			logger.info("Path : " + path);

			Document document = new Document();
			File file = new File(path);
			file.mkdirs();
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(file.getPath() + File.separator + fileName + ".pdf"));
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			worker.parseXHtml(writer, document, new StringReader(templateName));

			document.close();
			logger.info("PDF Succesfully Created at location : " + file.getPath() + File.separator + fileName + ".pdf");
			return result = true;
		} catch (Exception ee) {
			ee.printStackTrace();
			logger.info(" == >> PDF Generation Failed << == ");
			return result;
		}

	}

	/**
	 * This method will create PDF file from given HTML templates this method does
	 * not save pdf on server
	 * 
	 * @param htmlTemplate
	 * @return ByteArrayOutputStream
	 * @author shinde.devendra
	 * @throws HRMSException
	 */

	public static ByteArrayOutputStream createPdfWithoutSave(String htmlTemplate) {

		try {

			// step 1
			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			// step 3
			document.open();
			// step 4
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			worker.parseXHtml(writer, document, new StringReader(htmlTemplate));
			// step 5
			document.close();

			return baos;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;

	}

}