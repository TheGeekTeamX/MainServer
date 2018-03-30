package ViewModel;

public class ConnectionData {

	private String id;
	private String startTime;
	private String endTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public ConnectionData(String id, String startTime, String endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
}
