package com.vinsys.hrms.pdf.service;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.vinsys.hrms.exception.HRMSException;

public interface IKpiPdfDownloade {

	byte[] generatePDFFromHTML(String parseHtml) throws DocumentException, IOException, HRMSException;

	String kpiPdfDownload(String string, Long kraid) throws HRMSException, IOException;

}
