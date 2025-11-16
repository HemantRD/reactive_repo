package com.vinsys.hrms.pdf.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.pdf.service.IKpiPdfDownloade;
import com.vinsys.hrms.pdf.service.impl.KpiPdfDownloadImpl;

@RestController
@RequestMapping("/pdf")
public class KpiPdfDownload {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	KpiPdfDownloadImpl kpidownload;
	
	@GetMapping(value = "/generatekpisummary")
	public void kpiPdfSummary(@RequestParam Long kraid,HttpServletResponse response) {
		try {
			String parseHtml = kpidownload.kpiPdfDownload("kpidownload.mustache", kraid);
			byte[] b = kpidownload.generatePDFFromHTML(parseHtml);
			response.getOutputStream().write(b);
			response.setContentType("application/pdf");

		} catch (Exception e) {
	        log.error("Error generating student merit list", e);
	    }
	}

}