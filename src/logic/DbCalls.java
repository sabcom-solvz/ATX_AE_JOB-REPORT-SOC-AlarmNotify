package logic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.tinylog.Logger;

import pojo.AlrNotifyLog;
import pojo.DateInfo;

public class DbCalls {
    private Connection conn = null;
    
    public void startConnection(Properties config) {
	try {
	    Class.forName(config.getProperty("db.driver"));
	    conn = DriverManager.getConnection(config.getProperty("db.link"), config.getProperty("db.user"),
		    config.getProperty("db.pwd"));
	    System.out.println("CONNECT TO PGSQL DB : [OK]");
	    Logger.info("CONNECT TO PGSQL DB : [OK]");
	} catch (Exception e) {
	    System.out.println(" : [1022] PGSQL DB CONNECTION FAILED");
	    Logger.error("CONNECT TO PGSQL DB : [1022] PGSQL DB CONNECTION FAILED");
	    StringWriter err = new StringWriter();
	    e.printStackTrace(new PrintWriter(err));
	    Logger.debug(err.toString());
	}
    }

    public void closeConnection() {
	try {
	    if (conn != null) {
		conn.close();
		conn = null;
	    }
	    Logger.info("DISCONNECTED FROM PGSQL DB");
	} catch (Exception e) {
	    Logger.warn("PGSQL DB DISCONNECTION FAILURE");
	    Logger.debug(e.getMessage());
	}
    }

    public List<AlrNotifyLog> getAlrNotifyLog(DateInfo dateInfo, String customerName, String cctId) {

	List<AlrNotifyLog> records = new ArrayList<AlrNotifyLog>();
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	PreparedStatement smt = null;
	ResultSet rs = null;
	String sql = "";
	String baseSql = "SELECT customer, service, site, cct_id, rbs_id, mh, fault_id, alarm, alr_date, clr_date, rca, age,"
		+ "status, cust_notify, proactive_tkt, LPAD(EXTRACT(YEAR FROM AGE(current_timestamp, alr_date))::text,2,'0') || ':' ||"
		+ "LPAD(EXTRACT(MONTH FROM AGE(current_timestamp, alr_date))::text,2,'0') || ':' ||"
		+ "LPAD(EXTRACT(DAY FROM AGE(current_timestamp, alr_date))::text,2,'0') || ':' ||"
		+ "LPAD(EXTRACT(HOUR FROM AGE(current_timestamp, alr_date))::text,2,'0') || ':' ||"
		+ "LPAD(EXTRACT(MINUTE FROM AGE(current_timestamp, alr_date))::text,2,'0') || ':' ||"
		+ "LPAD((ROUND(EXTRACT(SECOND FROM AGE(current_timestamp, alr_date)),0))::text,2,'0') as active_alarm_aging FROM sanm_alr_notify_log "
		+ "WHERE alr_date BETWEEN TO_TIMESTAMP(?,'DD/MM/YYYY HH24:MI:SS') "
		+ "AND TO_TIMESTAMP(?,'DD/MM/YYYY HH24:MI:SS')";
	if(customerName !=null) {
	  sql = baseSql + " AND customer = ? ORDER BY cct_id";  
	} else if (cctId != null) {
	  sql = baseSql + " AND cct_id = ? ORDER BY cct_id";
	} else {
	  sql = baseSql + " ORDER BY cct_id";
	}
	try {
	    smt = conn.prepareStatement(sql, 1);
	    smt.setString(1, dateInfo.getStartDate());
	    smt.setString(2, dateInfo.getEndDate());
	    if (customerName != null) {
		smt.setString(3, customerName);
	    }
	    if (cctId != null) {
		smt.setString(3, cctId);
	    }
	    rs = smt.executeQuery();
	    while (rs.next()) {
		AlrNotifyLog alrNotify = new AlrNotifyLog();
		alrNotify.setCustomer(rs.getString("customer"));
		alrNotify.setService(rs.getString("service"));
		alrNotify.setSite(rs.getString("site"));
		alrNotify.setCctId(rs.getString("cct_id"));
		alrNotify.setRbsId(rs.getString("rbs_id"));
		alrNotify.setMh(rs.getString("mh"));
		alrNotify.setFaultId(rs.getString("fault_id"));
		alrNotify.setAlarm(rs.getString("alarm"));
		alrNotify.setAlrDate(dateFormat.format(rs.getTimestamp("alr_date")));
		alrNotify.setClrDate(rs.getTimestamp("clr_date") != null ? dateFormat.format(rs.getTimestamp("clr_date")) : null);
		alrNotify.setRca(rs.getString("rca"));
		alrNotify.setAge(rs.getString("age"));
		alrNotify.setStatus(rs.getString("status"));
		alrNotify.setCustNotify(rs.getBoolean("cust_notify") == true ? "YES" : "NO");
		alrNotify.setProactiveTkt(rs.getString("proactive_tkt"));
		if (null == alrNotify.getClrDate()) {
		    alrNotify.setActiveAlarmAgeing(rs.getString("active_alarm_aging"));
		}

		records.add(alrNotify);
	    }
	    System.out.println("GET ALARM NOTIFICATION RECORDS: [1005] SQL SELECT QUERY [OK]");
	    Logger.warn("GET ALARM NOTIFICATION RECORDS : [1005] SQL SELECT QUERY [OK]");
	    rs.close();
	    smt.close();
	} catch (Exception e) {
	    System.out.println("GET ALARM NOTIFICATION RECORDS: [1005] SQL SELECT QUERY FAILED");
	    Logger.warn("GET ALARM NOTIFICATION RECORDS : [1005] SQL SELECT QUERY FAILED");
	    StringWriter err = new StringWriter();
	    e.printStackTrace(new PrintWriter(err));
	    Logger.debug(err.toString());
	}
	return records;
    }

}
