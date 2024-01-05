package logic;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import pojo.AlrNotifyLog;
import pojo.DateInfo;
import pojo.Output;

public class Logic {

    public Logic() {
    }

    public static boolean execute(DateInfo dateInfo, String customerName, String cctId) {

	try {
	    String reportFile = "";
	    String fileName = "";
	    Notifier sm = new Notifier();
	    ConfigurationUtil.initLog();
	    Properties config = ConfigurationUtil.loadConfig();
	    DbCalls dbc = new DbCalls();
	    dbc.startConnection(config);
	    List<AlrNotifyLog> records = dbc.getAlrNotifyLog(dateInfo, customerName, cctId);
	    if (records != null && records.size() > 0) {
		reportFile = generateCSVReport(records);
	    }
	    dbc.closeConnection();
	    fileName = reportFile;
	    reportFile = ConfigurationUtil.getParentDirectory() + "\\reports\\" + reportFile;
	    sendNotification(config, reportFile,fileName, dateInfo, sm);

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return true;
    }

    private static String generateCSVReport(List<AlrNotifyLog> records) {

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
	LocalDateTime now = LocalDateTime.now();
	String csvFile = dtf.format(now) + "_notify_report.csv";
	CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("CUSTOMER NAME", "SERVICE TYPE", "SITE NAME", "SERVICE ID",
		"RBS ID", "MODEL HANDLE", "FAULT ID", "ALARM TITLE", "START DATE", "END DATE", "ROOT CAUSE",
		"FAULT DURATION", "STATUS", "CUSTOMER NOTIFIED", "PROACTIVE TICKET", "ACTIVE ALARM AGEING");

	try (FileWriter fileWriter = new FileWriter(ConfigurationUtil.getParentDirectory() + "\\reports\\" + csvFile);
		CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)) {
	    for (AlrNotifyLog record : records) {
		csvPrinter.printRecord(record.getCustomer(), record.getService(), record.getSite(), record.getCctId(),
			record.getRbsId(), record.getMh(), record.getFaultId(), record.getAlarm(), record.getAlrDate(),
			record.getClrDate(), record.getRca(), record.getAge(), record.getStatus(),
			record.getCustNotify(), record.getProactiveTkt(), record.getActiveAlarmAgeing());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return csvFile;
    }

    private static boolean sendNotification(Properties config, String attachment, String fileDisplayName, DateInfo dateInfo, Notifier sm) {
	Output out = new Output();
	out.setBcc(config.getProperty("email.support"));
	out.setTo(config.getProperty("email.requestor"));
	out.setSubject("Alarm Notification Report - [" + dateInfo.getStartDate() + " to " + dateInfo.getStartDate() + "]");
	out.setHighlight("");
	out.setMessage("<p class=\"MsoNormal\">Hello,</p><br>"
		+ "<p class=\"MsoNormal\">Attached is the requested report.</p><br>");

	return sm.notifyRequester(config, out, attachment, fileDisplayName);
    }

}
