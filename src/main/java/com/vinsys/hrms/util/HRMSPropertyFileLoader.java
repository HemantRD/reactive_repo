package com.vinsys.hrms.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class to load property file
 * 
 * @author shome.nitin
 */ 
public class HRMSPropertyFileLoader {

	private static final Logger logger = LoggerFactory.getLogger(HRMSPropertyFileLoader.class);
	private static HashMap<String, Properties> propMap = null;
	private static HRMSPropertyFileLoader zenSingleton = new HRMSPropertyFileLoader();

	/**
	 * Constructor to load map single copy
	 */
	private HRMSPropertyFileLoader() {
		propMap = new HashMap<String, Properties>();
	}

	/**
	 * To load property file based on property file name
	 * 
	 * @param String
	 *            property file name
	 * @return Properties
	 */
	private synchronized Properties getInstance(String propFileName) throws FileNotFoundException, IOException {
		if (propMap.get(propFileName) != null)
			return propMap.get(propFileName);

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(IHRMSConstants.PropertyFileRootDirectory + propFileName);
		prop.load(fis);
		// prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/"
		// + propFileName));
		propMap.put(propFileName, prop);

		return propMap.get(propFileName);
	}

	/**
	 * To get the property file
	 * 
	 * @param String
	 *            prop file name
	 */
	public static Properties getPropertyFile(String propFileName) throws FileNotFoundException, IOException {
		return zenSingleton.getInstance(propFileName);
	}

	/**
	 * To refresh the property file map
	 */
	public static void refreshConfig() {
		logger.info("To refresh property configuration ");
		propMap.clear();
	}

}
