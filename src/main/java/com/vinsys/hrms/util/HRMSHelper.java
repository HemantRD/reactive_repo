package com.vinsys.hrms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSLoginRequest;
import com.vinsys.hrms.datamodel.VOLeaveCalculationRequest;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.employee.vo.LeaveCalculationRequestVO;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.TimeLineRequestVO;

public class HRMSHelper {

	private static final Logger logger = LoggerFactory.getLogger(HRMSHelper.class);
	public static ObjectMapper mapper = new ObjectMapper();
	public static HRMSBaseResponse baseResponse = new HRMSBaseResponse();
	public static Base64 base64 = new Base64();

	/**
	 * This Will Create One Copy Instance Of Object Mapper
	 * 
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	/**
	 * This Generic Method Will Help To Create JSON String Of The Given Object
	 * 
	 * @param Object
	 * @return JSON String
	 */
	public static String createJsonString(Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {

		return mapper.writeValueAsString(obj);

	}

	/**
	 * This Generic Method Will Help To Create JSON String For Error Response
	 * 
	 * @param errorMessage,errorCode
	 * @return JSON String
	 */
	public static String sendErrorResponse(String errorMessage, int errorCode)
			throws JsonGenerationException, JsonMappingException, IOException {

		baseResponse.setResponseMessage(errorMessage);
		baseResponse.setResponseCode(errorCode);
		return createJsonString(baseResponse);

	}

	/**
	 * This Generic Method Will Help To Create JSON String For Success Response
	 * 
	 * @param successMessage,successCode
	 * @return JSON String
	 */
	public static String sendSuccessResponse(String successMessage, int successCode)
			throws JsonGenerationException, JsonMappingException, IOException {

		baseResponse.setResponseMessage(successMessage);
		baseResponse.setResponseCode(successCode);
		return createJsonString(baseResponse);

	}

	/*
	 * public static Map getUserRoleIdAndMenuByType(String msg) { //String
	 * queryString =
	 * "select m.functionName, rfm.roleId from ZenFunctionMaster m, ZenRoleToFunctionMapping rfm, ZenRoleToTypeMapping rtm where rtm.roleId = rfm.roleId and rfm.functionId = m.functionId and rtm.usrType ='"
	 * +msg+"' and m.isActive='Y'"; String queryString =
	 * "select m.functionName, rfm.roleId from ZenFunctionMaster m, ZenRoleToFunctionMapping rfm, ZenRoleToTypeMapping rtm where rtm.roleId = rfm.roleId and rfm.functionId = m.functionId and rtm.usrType ='"
	 * +msg+"'"; List<Object[]> resultList =
	 * ZenEntityBaseHome.executeQuery(queryString); List<String> menuLst = new
	 * ArrayList<String>(); Map<String,Object> resultMap = new HashMap<>();
	 * 
	 * for (Object[] result : resultList){ menuLst.add((String)result[0]);
	 * resultMap.put("roleId", result[1]);}
	 * 
	 * resultMap.put("menuLst", menuLst); return resultMap;
	 * 
	 * }
	 */

	public static String randomNumber() {
		long x = 0;
		do {
			x = System.nanoTime();
			for (int n = 0; n < 3; n++) {
				x ^= (x << 21);
				x ^= (x >>> 35);
				x ^= (x << 4);
			}
			x &= 0xffffffffffL;
		} while (x < 0);
		return String.valueOf(x);

	}

	public static String stringDecoder(String encodedValue) throws UnsupportedEncodingException, Exception {
		String decodedValue = new String(DatatypeConverter.parseBase64Binary(encodedValue));
		return decodedValue;
	}

	public static String createHashCode() {
		String string = "abc@test.com";
		return "";
	}

	public static boolean isNullOrEmpty(Object object) {
		return (object == null);
	}

	/**
	 * Checks the Given value is NULL or EMPTY
	 * 
	 * @param value
	 * @return a boolean value
	 */
	public static boolean isNullOrEmpty(String value) {
		return (value == null) || (value.trim().equals("")) || (value.trim().equals("null"));
	}

	/**
	 * Checks the Given array is NULL or EMPTY
	 * 
	 * @param array
	 * @return a boolean value
	 */
	public static boolean isNullOrEmpty(Object[] array) {
		return (array == null) || array.length == 0;
	}

	/**
	 * Checks the Given list is NULL or EMPTY
	 * 
	 * @param list
	 * @return a boolean value
	 */
	public static boolean isNullOrEmpty(List<?> list) {
		return (list == null) || list.isEmpty();
	}

	/**
	 * Checks the Given long is equals to 0
	 * 
	 * @param long
	 * @return a boolean value
	 */
	public static boolean isLongZero(long longNumber) {
		return longNumber == 0;
	}

	public static boolean isFloatZero(Float number) {
		return number == 0;
	}

	public static double convertFromSession(VOLeaveCalculationRequest leave) {

		// int fromSession = Integer.parseInt(leave.getFromSession());
		int fromSession = HRMSHelper.isNullOrEmpty(leave.getFromSession()) ? 0
				: Integer.valueOf(leave.getFromSession().substring(leave.getFromSession().length() - 1));
		int session = leave.getNumberOfSession();
		// double part = 1/(double)session;
		double val = 0;
		for (int i = 1; i <= session; i++) {

			if (i == fromSession) {
				val = (i - 1) * (1 / (double) session);
			}
		}
		logger.info("*****From Session vaule :: " + val);
		return val;
		/*
		 * if(fSession==1) { return 0; }else if(fSession==2) { return 0.25; }else
		 * if(fSession==3) { return 0.5; }else { return 0.75; }
		 */

	}

	public static double convertToSession(VOLeaveCalculationRequest leave) {

		int toSession = HRMSHelper.isNullOrEmpty(leave.getToSession()) ? 0
				: Integer.valueOf(leave.getToSession().substring(leave.getToSession().length() - 1));
		int session = leave.getNumberOfSession();
		double val = 0;
		for (int i = 1; i <= session; i++) {

			if (i == toSession) {
				val = (session - i) * (1 / (double) session);
			}
		}
		logger.info("*****To Session vaule :: " + val);
		return val;

		/*
		 * if(fromSession==1) { return 0.75; }else if(fromSession==2) { return 0.5;
		 * }else if(fromSession==3) { return 0.25; }else { return 0; }
		 */
	}

	/**
	 * Checks the Given float is equals to 0
	 * 
	 * @param float
	 * @return a boolean value
	 */
	public static boolean isFloatZero(float floatNumber) {
		return floatNumber == 0;
	}
	
	public static boolean isDoubleZero(Double doubleNumber) {
		return doubleNumber == 0;
	}

	public static int getWorkingDays(VOLeaveCalculationRequest leaveCalculation,
			List<OrganizationWeekoff> weekoffList) {
		int numberOfDays = 0;
		Date from = leaveCalculation.getFromDate();
		Date to = leaveCalculation.getToDate();

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(from);
		cal2.setTime(to);
		cal2.add(Calendar.DATE, 1);

		String[] strDays = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY",
				"SATURDAY" };

		cal1.setFirstDayOfWeek(cal1.SUNDAY);

		while (cal1.before(cal2)) {
			int i = cal1.get(Calendar.WEEK_OF_MONTH);

			for (OrganizationWeekoff weekoff : weekoffList) {
				// +weekoff.getWeekoffDays());
				if (i == weekoff.getWeekNumber()) {
					String dbWeekoffDays = weekoff.getWeekoffDays();
					String calenderDay = strDays[cal1.get(Calendar.DAY_OF_WEEK) - 1];
					if (!dbWeekoffDays.contains(calenderDay)) {
						numberOfDays++;
					}
				}
			}
			cal1.add(Calendar.DATE, 1);
		}
		logger.info("*****Total Number of Working Days :: " + numberOfDays);
		return numberOfDays;
	}

	public static int getHolidays(VOLeaveCalculationRequest leaveCalculation, List<OrganizationHoliday> holidayList,
			List<OrganizationWeekoff> weekoffList) throws ParseException {
		int numberOfHolidays = 0;
		Date from = leaveCalculation.getFromDate();
		Date to = leaveCalculation.getToDate();

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(from);
		cal2.setTime(to);
		cal2.add(Calendar.DATE, 1);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		while (cal1.before(cal2)) {

			for (OrganizationHoliday holiday : holidayList) {

				String calDate = format.format(cal1.getTime());
				String holidayDate = format.format(holiday.getHolidayDate());
				if (calDate.equalsIgnoreCase(holidayDate)) {
					leaveCalculation.setFromDate(holiday.getHolidayDate());
					leaveCalculation.setToDate(holiday.getHolidayDate());

					int workingday = getWorkingDays(leaveCalculation, weekoffList);

					if (workingday != 0)
						numberOfHolidays++;
				}
			}
			cal1.add(Calendar.DATE, 1);
		}
		logger.info("*****Total Number Of HoliDays :: " + numberOfHolidays);
		return numberOfHolidays;
	}

	/**
	 * 
	 * @return integer value of current calendar year
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * Dynamically replaces the values provided in map as key with its values
	 * 
	 * @param Map<String,String>,String
	 * @return String
	 */
	public static String replaceString(Map<String, String> replacmentMap, String string) {

		if (!HRMSHelper.isNullOrEmpty(replacmentMap) && !isNullOrEmpty(string)) {
			for (String key : replacmentMap.keySet()) {
				if (!isNullOrEmpty(replacmentMap.get(key))) {
					string = string.replace(key, replacmentMap.get(key));
				} else {
					string = string.replace(key, " ");
				}
			}
		}
		// logger.info(" Final string With Replaced Value :: " + string);
		return string;
	}

	/**
	 * To encode a String in base 64
	 * 
	 * @param String
	 * @return Base64 Encoded String
	 * @author shome.nitin
	 */
	public static String encodeString(String string) {

		logger.info("Orignal  String : " + string);
		String encodedString = null;
		if (!isNullOrEmpty(string)) {
			encodedString = new String(Base64.encodeBase64URLSafe(string.getBytes()));
			logger.info("Encoded String : " + encodedString);
		}
		return encodedString;
	}

	/**
	 * To Decode and base 64 encode string
	 * 
	 * @param Base 64 Encoded String
	 * @return Base64 Decoded String
	 * @author shome.nitin
	 */
	public static String decodeString(String string) {

		logger.info("Encoded String : " + string);
		String decodedString = null;
		if (!isNullOrEmpty(string)) {
			decodedString = new String(base64.decode(string.getBytes()));
			logger.info("Decoded String : " + decodedString);
		}
		return decodedString;
	}

	/**
	 * @param birthDate
	 * @return int calculated age
	 * @author Devendra
	 */
	public static int calculateAge(Date birthDate) {
		if (!HRMSHelper.isNullOrEmpty(birthDate)) {
			Calendar now = Calendar.getInstance();
			Calendar dob = Calendar.getInstance();
			dob.setTime(birthDate);

			int dobYear = dob.get(Calendar.YEAR);
			int currentYear = now.get(Calendar.YEAR);
			int age = currentYear - dobYear;
			logger.info("DOB " + age);
			return age;

		} else {
			return 0;
		}
	}

	/***
	 * @param void
	 * @return random generated string
	 * @author shome.nitin
	 */
	public static String randomString() {
		StringBuilder b = new StringBuilder();
		Random r = new Random();
		String strSet = "0123456789abcdefghijklmnopqrstuvwxyz!@#";
		for (int i = 0; i < 10; i++) {
			b.append(strSet.charAt(r.nextInt(strSet.length())));
		}
		return b.toString();
	}

	/**
	 * Checks and converts a string from null to empty
	 * 
	 * @param string
	 * @return a string
	 * 
	 * @author shome.nitin
	 */
	public static String convertNullToEmpty(String string) {
		if (isNullOrEmpty(string)) {
			string = "";
		}
		return string;
	}

	/**
	 * To convert image into a base 64 String
	 * 
	 * @param string
	 * @return a string
	 * 
	 * @author shome.nitin
	 */
	public static String base64ImageConverte(String imagePath) {

		File imageFile = new File(imagePath);
		String encodedfile = null;
		try {

			FileInputStream fileInputStreamReader = new FileInputStream(imageFile);
			byte[] bytes = new byte[(int) imageFile.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
			fileInputStreamReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encodedfile;
	}

	public static String encryptToSHA256(String inputStr) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] result = mDigest.digest(inputStr.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	/**
	 * This method will convert boolean value to human readable value i.e. Yes Or No
	 */
	public static String toConvertBooleanToHumanReadable(boolean value) {
		String response = "No";
		if (value)
			response = "Yes";
		return response;
	}

	public static boolean isDigit(String str) {
		String regex = "[0-9]+";

		return str.matches(regex);
	}

	/**
	 * This Method will validate Server Side Password by setting the pattern reguler
	 * expression.
	 * 
	 * @author Omkar Agalave & Ritesh Kolhe
	 * @date 17-01-2022
	 */
	public static boolean isValidPassword(String password, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * @author Akanksha Gaikwad added method for server side validation of add/
	 *         modify candidate
	 */

	public static boolean regexMatcher(String inputStr, String regex) {
		boolean result = false;
		if (!isNullOrEmpty(inputStr) && !isNullOrEmpty(regex)) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(inputStr);
			result = matcher.matches();
		}
		return result;

	}

	/**
	 * @author Akanksha Gaikwad Added method for date validation of add/ modify
	 *         candidate
	 */

	public static boolean isDatePast(CharSequence date, final String dateFormat) {
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate inputDate = LocalDate.parse(date, dtf);

		return inputDate.isBefore(localDate);
	}

	// added by Monika
	public static boolean pincodeCheck(int pincode) {
		if (pincode == 0 || pincode < 0) {
			return true;
		} else {
			return false;
		}
	}

//		**************added by Rushikesh for Length of username are not validated server side validation*************************

	public static void loginValidation(HRMSLoginRequest request) throws HRMSException {
		boolean checkUsername = HRMSHelper.regexMatcher(request.getUsername(),
				"^[a-zA-Z0-9+_.-]{1,100}+@[a-zA-Z0-9.-;]{1,100}+$");
		if (checkUsername) {
		} else {
			if (!checkUsername)
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Username Invalid");
		}
	}

//	public static void loginValidation(LoginRequestVO request) throws HRMSException {
//		boolean checkUsername = HRMSHelper.regexMatcher(request.getUsername(),
//				"^[a-zA-Z0-9+_.-]{1,320}+@[a-zA-Z0-9.-;]{1,320}+$");
//		if (checkUsername) {
//		} else {
//			if (!checkUsername)
//				throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Username Invalid");
//		}
//	}

	public static boolean validateDateFormate(String date) {
		return date.matches("^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$");
	}

	public static void checkSessionTiming(Date fromDate, Date toDate, long fSession, long tSession,
			int numberOfSessions) throws HRMSException {
		if (fromDate.equals(toDate) && fSession > tSession) {
			throw new HRMSException(1503, ResponseCode.getResponseCodeMap().get(1503));
		}
		if (fSession > numberOfSessions) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " From Session");
		}

		if (tSession > numberOfSessions) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Session");
		}
	}

	public static boolean validateString(String string) {
		return string.matches("[a-zA-Z_ ]+");
	}

	public static boolean validateEmail(String email) {
		return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
	}
	
	public static boolean validateEmailFormat(String email) {
		return email.matches("^[\\w\\.]+@([\\w-]+\\.)+[\\w-]+$");
	}
	
	

	public static boolean validateSession(String session) {
		String regex = "^([a-zA-Z]+|[0-9]+)$";
		return regexMatcher(session, regex);
	}

	public static boolean validateNumber(String number) {
		String regex = "[0-9]{1,3}";
		return regexMatcher(number, regex);
	}
	public static boolean validateNumberFormat(String number) {
		String regex = "[0-9]{1,250}$";
		return regexMatcher(number, regex);
	}

	public static boolean validateAdharFormat(String number) {
		String regex = "[0-9]{12,12}";
		return regexMatcher(number, regex);
	}
	public static boolean validateFloatNO(String number) {
		String regex = "[0-9.]";
		return regexMatcher(number, regex);
	}

	
	public static boolean validateDoubleNO(Double number) {
		String regex = "[0-9.]{0,10}";
		return regexMatcher(number.toString(), regex);
	}
	/**
	 * Start--- Method overload for calculate leave because request vo changed date
	 * to string
	 * 
	 */
	public static int getWorkingDays(LeaveCalculationRequestVO leaveCalculation,
			List<OrganizationWeekoff> weekoffList) {
		int numberOfDays = 0;
		Date from = HRMSDateUtil.parse(leaveCalculation.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date to = HRMSDateUtil.parse(leaveCalculation.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(from);
		cal2.setTime(to);
		cal2.add(Calendar.DATE, 1);

		String[] strDays = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY",
				"SATURDAY" };

		cal1.setFirstDayOfWeek(cal1.SUNDAY);

		while (cal1.before(cal2)) {
			int i = cal1.get(Calendar.WEEK_OF_MONTH);
			for (OrganizationWeekoff weekoff : weekoffList) {
				if (i == weekoff.getWeekNumber()) {
					String dbWeekoffDays = weekoff.getWeekoffDays();
					String calenderDay = strDays[cal1.get(Calendar.DAY_OF_WEEK) - 1];
					if (!dbWeekoffDays.contains(calenderDay)) {
						numberOfDays++;
					}
				}
			}
			cal1.add(Calendar.DATE, 1);
		}
		logger.info("*****Total Number of Working Days :: " + numberOfDays);
		return numberOfDays;
	}

	public static int getHolidays(LeaveCalculationRequestVO leaveCalculation, List<OrganizationHoliday> holidayList,
			List<OrganizationWeekoff> weekoffList) {
		int numberOfHolidays = 0;
		Date from = HRMSDateUtil.parse(leaveCalculation.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date to = HRMSDateUtil.parse(leaveCalculation.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(from);
		cal2.setTime(to);
		cal2.add(Calendar.DATE, 1);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		while (cal1.before(cal2)) {

			for (OrganizationHoliday holiday : holidayList) {

				String calDate = format.format(cal1.getTime());
				String holidayDate = format.format(holiday.getHolidayDate());

				if (calDate.equalsIgnoreCase(holidayDate)) {
					leaveCalculation.setFromDate(
							HRMSDateUtil.format(holiday.getHolidayDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
					leaveCalculation.setToDate(
							HRMSDateUtil.format(holiday.getHolidayDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

					int workingday = getWorkingDays(leaveCalculation, weekoffList);

					if (workingday != 0)
						numberOfHolidays++;
				}
			}
			cal1.add(Calendar.DATE, 1);
		}
		logger.info("*****Total Number Of HoliDays :: " + numberOfHolidays);
		return numberOfHolidays;
	}

	public static double convertFromSession(LeaveCalculationRequestVO leave) {

		// int fromSession = Integer.parseInt(leave.getFromSession());
		int fromSession = HRMSHelper.isNullOrEmpty(leave.getFromSession()) ? 0
				: Integer.valueOf(leave.getFromSession().substring(leave.getFromSession().length() - 1));
		int session = leave.getNumberOfSession();
		// double part = 1/(double)session;
		double val = 0;
		for (int i = 1; i <= session; i++) {

			if (i == fromSession) {
				val = (i - 1) * (1 / (double) session);
			}
		}
		logger.info("*****From Session vaule :: " + val);
		return val;
		/*
		 * if(fSession==1) { return 0; }else if(fSession==2) { return 0.25; }else
		 * if(fSession==3) { return 0.5; }else { return 0.75; }
		 */

	}

	public static double convertToSession(LeaveCalculationRequestVO leave) {

		int toSession = HRMSHelper.isNullOrEmpty(leave.getToSession()) ? 0
				: Integer.valueOf(leave.getToSession().substring(leave.getToSession().length() - 1));
		int session = leave.getNumberOfSession();
		double val = 0;
		for (int i = 1; i <= session; i++) {

			if (i == toSession) {
				val = (session - i) * (1 / (double) session);
			}
		}
		logger.info("*****To Session vaule :: " + val);
		return val;

		/*
		 * if(fromSession==1) { return 0.75; }else if(fromSession==2) { return 0.5;
		 * }else if(fromSession==3) { return 0.25; }else { return 0; }
		 */
	}

	/**
	 * End--- Method overload for calculate leave because request vo changed date to
	 * string
	 * 
	 */

	/**
	 * 
	 * @param logged in employee roles
	 * @param role
	 * @return
	 */
	public static boolean isRolePresent(List<String> roles, String role) {
		boolean result = false;

		for (String loggedInRole : roles) {
			if (loggedInRole.equalsIgnoreCase(role)) {
				return true;
			}
		}
		return result;
	}

	/**
	 * Only for validate P2C comment
	 * 
	 * @param string
	 * @return
	 */
	public static boolean validateComment(String string) {
		return string.matches("[A-Za-z0-9,. ]{1,250}$");
	}

	public static boolean isNumber(String number) {
		String regex = "[0-9]+";
		return regexMatcher(number, regex);
	}

	public static boolean validateLeaveReason(String comment) {
		return comment.matches("^[A-Za-z0-9+_.@()\"?,\\s-]{1,250}$");
	}

	public static int calculateNumberOfDays(String fromDate, String toDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		LocalDate startDate = LocalDate.parse(fromDate, formatter);
		LocalDate endDate = LocalDate.parse(toDate, formatter);

		// return startDate.until(endDate).getDays();
		return (int) startDate.until(endDate).getDays() + 1;
	}
	public static boolean isNotFutureDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate expenseDate = LocalDate.parse(date, formatter);
            LocalDate currentDate = LocalDate.now();
            return !expenseDate.isAfter(currentDate);
        } catch (Exception e) {
            return false;
        }
    }
	
	// added by swapnil to validate pincode
	
	public static boolean pincodeCheck(Long pincode) {
		if (pincode == 0 || pincode < 0) {
			return true;
		} else {
			return false;
		}
	}
	
	 public static boolean allow18YearEmployeee(ProfileDetailsRequestVO request)throws HRMSException {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			try {
				Date dob = sdf.parse(request.getDateOfBirth());
				Date joiningDate = sdf.parse(request.getDateOfJoining());
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(dob);
				cal.add(Calendar.YEAR, 18);
				
				if (joiningDate.before(cal.getTime())) {
					return false;
				}
			} catch (ParseException e) {
				return false; 
			}
			return true;
		 }
	 
	 public static boolean validatedJoiningDate(ProfileDetailsRequestVO request)throws HRMSException {
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		 
			try {
				Date joiningDate = sdf.parse(request.getDateOfJoining());
				Date currentDate = new Date();
				
				if(joiningDate.after(currentDate)) {
					return false;
				}
			} catch (ParseException e) {
				return false;
			}
			return true;
	 }
	 
	 public static boolean validatedRetirementDate(ProfileDetailsRequestVO request)throws HRMSException {
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		 
			try {
				Date retirement = sdf.parse(request.getRetirementDate());
				Date joiningDate = sdf.parse(request.getDateOfJoining());
				
				if(retirement.before(joiningDate) || retirement.equals(joiningDate)) {
					return false;
				}
			} catch (ParseException e) {
				return false;
			}
			return true;
	 }
	 
	 public static boolean isValidCurrentYear(TimeLineRequestVO request) throws HRMSException {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			try {
				Date inputDate = sdf.parse(request.getDate());
				Calendar inputCal = Calendar.getInstance();
				inputCal.setTime(inputDate);
				
				int  currentYear = Calendar.getInstance().get(Calendar.YEAR);
				int inputYear = inputCal.get(Calendar.YEAR);
				
				return inputYear == currentYear;
			}
			catch (ParseException e) {
		        return false;
			
			
		}
	}

	 public static String safeString(String input) {
		    return input != null ? input : IHRMSConstants.NA;
		}
	 
	 
	 public static double parsePercentage(String value) {
		    if (isNullOrEmpty(value) || !value.contains("%")) return 0.0;
		    return Double.parseDouble(value.replace("%", "").trim());
		}

		public static String formatPercentage(double value) {
		    return String.format("%.1f%%", value);
		}
}
