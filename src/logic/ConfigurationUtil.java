package logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

import pojo.DateInfo;

public class ConfigurationUtil {

	public ConfigurationUtil() {
	}

	public static void initLog() {
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		String logFile = getParentDirectory() + "\\log\\"+dtf.format(now) + "_REPORT.log";
	    
	        Configuration.set("writer", "file");
		Configuration.set("writer.file", logFile);
		Configuration.set("writer.append", "true");
		Configuration.set("writer.buffered", "false");
		Configuration.set("writingthread", "true");
		Configuration.set("writer.format", "[{date: dd-MM-yyyy HH:mm:ss}], [{class}.{method}], [{level}], {message}");
	}
	
	public static Properties loadConfig() {
		Properties config = new Properties();

		try (InputStream is = new FileInputStream(new File(getParentDirectory() + "\\config\\cfg_support.properties"))) {
			config.load(is);

			int maxIndentLevel = 40;
			// Get an enumeration of all the property keys
			Enumeration<?> propertyNames = config.propertyNames();
			StringBuilder logText = new StringBuilder("CONFIG FILE LOADED\r\n");
			// Iterate through the keys and get the properties with indentation
			while (propertyNames.hasMoreElements()) {
			    String key = (String) propertyNames.nextElement();
			    String value = config.getProperty(key);
			    int actualIndentLevel = maxIndentLevel - key.length();
			    String indentation = " ".repeat(actualIndentLevel);
			    logText.append(key + ": " + indentation + value);
			    logText.append("\r\n");
			}
			Logger.trace(logText.toString());
			System.out.println(logText.toString());

		} catch (IOException ex) {
			Logger.error("UNABLE TO READ CONFIG FILE");
		}
		return config;
	}
	
	public static String getParentDirectory() {
	    Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
	    Path parentDirectory = currentWorkingDirectory.getParent();
	    if (parentDirectory != null) {
		return parentDirectory.toAbsolutePath().toString();
	    } else {
		System.out.println("No parent directory found (already at the root).");
		return "";
	    }
	}

	public static DateInfo getPreviousMonthsEndDate() {
	    DateInfo di = new DateInfo();
	    LocalDate today = LocalDate.now();
	    // Get the YearMonth for the current month
	    YearMonth currentYearMonth = YearMonth.from(today);
	    // Get the YearMonth for the previous month
	    YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
	    // Calculate the first and last days of the previous month
	    LocalDate firstDayOfPreviousMonth = previousYearMonth.atDay(1);
	    LocalDate lastDayOfPreviousMonth = previousYearMonth.atEndOfMonth();
	    
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    
	    di.setStartDate(firstDayOfPreviousMonth.format(dtf)+ " 00:00:00");
	    di.setEndDate(lastDayOfPreviousMonth.format(dtf)+ " 23:59:59");
	    return di;
	}
	
	public static boolean validateDate(DateInfo dateInfo) {
	    boolean flag = false;
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    sdf.setLenient(false);
	    try {
		sdf.parse(dateInfo.getStartDate());
		sdf.parse(dateInfo.getEndDate());
		flag = true;
	    } catch (ParseException e) {
		flag = false;
		System.out.println("INVALID INPUT ARGUMENTS: Invalid datetime format");
	    }
	    return flag;
	}
}
