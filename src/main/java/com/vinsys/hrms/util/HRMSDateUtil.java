package com.vinsys.hrms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class HRMSDateUtil {

	private static final long MILLI_SEC_PER_DAY = 24l * 60l * 60l * 1000l;

	/**
	 * Gets the date for given year, month and day
	 * 
	 * @param year  the year of the date
	 * @param month the month of the date
	 * @param day   the day of the date
	 * @return a date with the given year, month and day
	 */
	public static Date getDate(int year, int month, int day) {
		// TimeZone zone = TimeZone.getTimeZone("GMT+0");
		Calendar cal = Calendar.getInstance(); // (zone);
		cal.clear();
		cal.set(year, month, day);
		return cal.getTime();
	}

	/**
	 * Gets the Current date
	 * 
	 * @return just the current date without the month and year
	 */
	public static int getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);

	}

	/**
	 * Gets the Current day
	 * 
	 * @return the current day
	 */
	public static int getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Gets the Current year
	 * 
	 * @return the current year
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Today's date (without the time portion, thus as of midnight this morning).
	 * 
	 * @return the current date without time portion
	 */
	public static Date getToday() {
		return getToday(0);
	}

	/**
	 * Today's date plus or minus the given number of days (without the time
	 * portion, thus as of midnight of the specified morning). 0 means today. -1
	 * means yesterday. +1 means tomorrow.
	 * 
	 * @param incrementDays - how many days to be added
	 * @return the incremented date
	 */
	public static Date getToday(int incrementDays) {
		// TimeZone zone = TimeZone.getTimeZone("GMT+0");
		Calendar cal = Calendar.getInstance(); // (zone);
		cal.add(Calendar.DATE, incrementDays);
		clearTimePortion(cal);
		return cal.getTime();
	}

	/**
	 * Clears the time portion for the given calendar
	 * 
	 * @param cal - for which calendar the action should be done
	 */
	private static void clearTimePortion(Calendar cal) {
		cal.set(Calendar.AM_PM, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Converts the java.sql date to java.util date
	 * 
	 * @param sqldate
	 * @return java.util.Date
	 */
	public static Date dateFromSqlToUtil(java.sql.Date sqldate) {
		return (sqldate == null) ? null : new Date(sqldate.getTime());
	}

	/**
	 * Converts the java.util date to java.sql date
	 * 
	 * @param utildate
	 * @return
	 */
	public static java.sql.Date dateFromUtilToSql(Date utildate) {
		return (utildate == null) ? null : new java.sql.Date(utildate.getTime());
	}

	/**
	 * Converts the java.util date to java.sql date
	 * 
	 * @param utildate
	 * @return
	 */
	public static boolean isSqlDate(Object object) {
		return (object instanceof java.sql.Date);
	}

	/**
	 * Checks whether two given dates are within one minute
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean
	 */
	public static boolean areWithinOneMinute(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);

		// Make sure that c1 is earlier of the two
		if (c1.after(c2)) {
			c1.setTime(date2);
			c2.setTime(date1);
		}

		// If we can add a minute to c1 and it's now later than c2, then the
		// two dates were less than a minute apart.
		c1.add(Calendar.MINUTE, 1);
		return c1.after(c2);
	}

	/**
	 * Increments the given date with given days
	 * 
	 * @param baseDate
	 * @param incrementDays
	 * @return
	 */
	public static Date incDate(Date baseDate, int incrementDays) {
		// TimeZone zone = TimeZone.getTimeZone("GMT+0");
		Calendar cal = Calendar.getInstance(); // (zone);
		cal.setTime(baseDate);
		cal.add(Calendar.DATE, incrementDays);
		return cal.getTime();
	}
	
	/**
	 * Increments the given date with given days
	 * 
	 * @param baseDate
	 * @param incrementDays
	 * @return
	 */
	public static Date incByMonth(Date baseDate, int incrementMonth) {
		// TimeZone zone = TimeZone.getTimeZone("GMT+0");
		Calendar cal = Calendar.getInstance(); // (zone);
		cal.setTime(baseDate);
		cal.add(Calendar.MONTH, incrementMonth);
		return cal.getTime();
	}

	/**
	 * Parses the a string which is in given format into java.util.Date.<br/>
	 * Format can be for example MM/dd/yyyy (uses format given in
	 * SimpleDateFormat).<br/>
	 * Default format: <b>'yyyy-MM-dd'</b><br/>
	 * 
	 * @param date
	 * @param format
	 * @return date, returns null if the date string is null.
	 */
	public static Date parse(String date, String format) {
		if (date == null)
			return null;
		try {
			if (format == null)
				format = "yyyy-MMM-dd";

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			simpleDateFormat.setLenient(false);
			return simpleDateFormat.parse(date); // just for throwing the
													// exception if it's an
													// invalid date
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * formats the date to the given format<br/>
	 * Format can be for example MM/dd/yyyy (uses format given in
	 * SimpleDateFormat).<br/>
	 * Default format: <b>'yyyy-MM-dd'</b><br/>
	 * 
	 * @param date
	 * @param format
	 * @return date, returns null if the given date is null.
	 */
	public static String format(Date date, String format) {
		if (date == null)
			return null;

		try {
			if (format == null)
				format = "yyyy-MM-dd";

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			// simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			simpleDateFormat.setLenient(false);
			return simpleDateFormat.format(date); // just for throwing the
													// exception if it's an
													// invalid date

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Formats the given date (in String) from given fromformat to given toformat
	 * 
	 * @param date
	 * @param formatFrom
	 * @param formatTo
	 * @return
	 */
	public static String format(String date, String formatFrom, String formatTo) {
		if (date == null)
			return null;
		try {
			return format(parse(date, formatFrom), formatTo);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Checks the given date is valid or not<br/>
	 * <b>Note:</b>Give some format for checking the validity of the date
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static boolean isValidDate(String date, String format) {
		if (date == null)
			return false;
		try {
			if (format == null)
				format = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(date); // just for throwing the exception
											// if it's an invalid date
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the difference between the given dates
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDifferenceInDays(Date startDate, Date endDate) {
		long diffInMillisec = endDate.getTime() - startDate.getTime();
		return diffInMillisec / MILLI_SEC_PER_DAY;
	}
	
	/**
	 * Gets the difference between the given dates in Months
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDifferenceInMonths(Date startDate, Date endDate) {
		
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDate);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(endDate);
		
		int diffYear = calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.MONTH);
		
		return diffMonth;
	}

	/**
	 * Set time stamp to 00:00:00
	 * 
	 * @param date
	 * @return
	 * @author devendra
	 */
	public static Date setTimeStampToZero(Date date) {

		try {
			SimpleDateFormat sf = new SimpleDateFormat(IHRMSConstants.POSTGRE_DATE_FORMAT);
			String strDate = HRMSDateUtil.format(date, IHRMSConstants.POSTGRE_DATE_FORMAT);
			return sf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Convert util date to local date
	 * 
	 * @param date
	 * @return local date
	 */
	public static LocalDate convertDateToLocalDate(Date date) {
		if (date != null)
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		else
			return null;
	}

	/**
	 * Convert util local date to date
	 * 
	 * @param local date
	 * @return date
	 */
	public static Date convertLocalDateToDate(LocalDate localDate) {
		if (localDate != null)
			return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		else
			return null;
	}
	
	/**
	 * get day from given date
	 * 
	 * @return Number of day of given date
	 */
	public static int getDay(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//int year = cal.get(Calendar.YEAR);
		//int month = cal.get(Calendar.MONTH);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * get month from given date
	 * 
	 * @return Number of month of given date
	 */
	public static int getMonth(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//int year = cal.get(Calendar.YEAR);
		return  cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * get year from given date
	 * 
	 * @return Number of year of given date
	 */
	public static int getYear(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return  cal.get(Calendar.YEAR);
	}
	
/////////added for week wise attendnace date
	
	public static Date getStartOfWeekInCurrentMonth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		setToStartOfDay(calendar);
		return calendar.getTime();
	}

	public static Date getEndOfWeekInCurrentMonth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Assuming a 7-day week
		setToEndOfDay(calendar);
		return calendar.getTime();
	}

	public static void setToStartOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public static void setToEndOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}
	
	
	 public static Date formatDate(Date date, String format) throws ParseException {
	        DateFormat formatter = new SimpleDateFormat(format);
	        String formattedDate = formatter.format(date);
	        return formatter.parse(formattedDate);
	    }
}
