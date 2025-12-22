package com.vinsys.hrms.security;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.security.dao.IWhiteListingDAO;
import com.vinsys.hrms.security.entity.WhiteListing;

@Component
public class URLWhiteListingHelper implements ApplicationContextAware {
	private URLWhiteListingHelper helper;
	private static List<WhiteListing> WHITELISTINGS = null;

	public URLWhiteListingHelper getInstance() {
		return helper;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		IWhiteListingDAO whiteListingDAO = applicationContext.getBean(IWhiteListingDAO.class);
		WHITELISTINGS = whiteListingDAO.findByIsActive();
		this.helper = this;
	}

	public boolean isWhiteListedV2(String requestedURI) {
		List<WhiteListing> whiteListings = WHITELISTINGS.stream().filter(e -> {
			Pattern pattern = Pattern.compile(e.getUrl());
			Matcher matcher = pattern.matcher(requestedURI);
			return matcher.find();
		}).collect(Collectors.toList());
		return !whiteListings.isEmpty();

	}
}
