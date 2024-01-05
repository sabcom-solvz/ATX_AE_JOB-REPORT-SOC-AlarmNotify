package pojo;

import java.sql.Date;

public class AlrNotifyLog {

    private String customer;
    private String service;
    private String site;
    private String cctId;
    private String rbsId;
    private String mh;
    private String faultId;
    private String alarm;
    private String alrDate;
    private String clrDate;
    private String rca;
    private String age;
    private String status;
    private String custNotify;
    private String proactiveTkt;
    private String activeAlarmAgeing;

    /**
     * @return the customer
     */
    public String getCustomer() {
	return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(String customer) {
	this.customer = customer;
    }

    /**
     * @return the service
     */
    public String getService() {
	return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
	this.service = service;
    }

    /**
     * @return the site
     */
    public String getSite() {
	return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
	this.site = site;
    }

    /**
     * @return the cctId
     */
    public String getCctId() {
	return cctId;
    }

    /**
     * @param cctId the cctId to set
     */
    public void setCctId(String cctId) {
	this.cctId = cctId;
    }

    /**
     * @return the rbsId
     */
    public String getRbsId() {
	return rbsId;
    }

    /**
     * @param rbsId the rbsId to set
     */
    public void setRbsId(String rbsId) {
	this.rbsId = rbsId;
    }

    /**
     * @return the mh
     */
    public String getMh() {
	return mh;
    }

    /**
     * @param mh the mh to set
     */
    public void setMh(String mh) {
	this.mh = mh;
    }

    /**
     * @return the faultId
     */
    public String getFaultId() {
	return faultId;
    }

    /**
     * @param faultId the faultId to set
     */
    public void setFaultId(String faultId) {
	this.faultId = faultId;
    }

    /**
     * @return the alarm
     */
    public String getAlarm() {
	return alarm;
    }

    /**
     * @param alarm the alarm to set
     */
    public void setAlarm(String alarm) {
	this.alarm = alarm;
    }

    
    /**
     * @return the alrDate
     */
    public String getAlrDate() {
        return alrDate;
    }

    /**
     * @param alrDate the alrDate to set
     */
    public void setAlrDate(String alrDate) {
        this.alrDate = alrDate;
    }

    /**
     * @return the clrDate
     */
    public String getClrDate() {
        return clrDate;
    }

    /**
     * @param clrDate the clrDate to set
     */
    public void setClrDate(String clrDate) {
        this.clrDate = clrDate;
    }

    /**
     * @return the rca
     */
    public String getRca() {
	return rca;
    }

    /**
     * @param rca the rca to set
     */
    public void setRca(String rca) {
	this.rca = rca;
    }

    /**
     * @return the age
     */
    public String getAge() {
	return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
	this.age = age;
    }

    /**
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * @return the custNotify
     */
    public String getCustNotify() {
        return custNotify;
    }

    /**
     * @param custNotify the custNotify to set
     */
    public void setCustNotify(String custNotify) {
        this.custNotify = custNotify;
    }

    /**
     * @return the proactiveTkt
     */
    public String getProactiveTkt() {
	return proactiveTkt;
    }

    /**
     * @param proactiveTkt the proactiveTkt to set
     */
    public void setProactiveTkt(String proactiveTkt) {
	this.proactiveTkt = proactiveTkt;
    }

    /**
     * @return the activeAlarmAgeing
     */
    public String getActiveAlarmAgeing() {
	return activeAlarmAgeing;
    }

    /**
     * @param activeAlarmAgeing the activeAlarmAgeing to set
     */
    public void setActiveAlarmAgeing(String activeAlarmAgeing) {
	this.activeAlarmAgeing = activeAlarmAgeing;
    }

}
