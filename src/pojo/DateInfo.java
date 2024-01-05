package pojo;

public class DateInfo {
    private String startDate;
    private String endDate;
    
    public DateInfo() {
	super();
    }
    public DateInfo(String startDate, String endDate) {
	super();
	this.startDate = startDate;
	this.endDate = endDate;
    }
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
