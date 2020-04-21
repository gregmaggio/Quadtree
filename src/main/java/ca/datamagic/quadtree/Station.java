/**
 * 
 */
package ca.datamagic.quadtree;

import java.io.Serializable;

/**
 * @author Greg
 *
 */
public class Station implements Serializable {
	private static final long serialVersionUID = 1L;
	private String stationId = null;
	private String stationName = null;
	private String state = null;
	private String wfo = null;
	private String radar = null;
	private String timeZoneId = null;
	private Double latitude = null;
	private Double longitude = null;
	
	public Station() {
	}
	
	public String getStationId() {
		return this.stationId;
	}
	
	public String getStationName() {
		return this.stationName;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getWFO() {
		return this.wfo;
	}
	
	public String getRadar() {
		return this.radar;
	}
	
	public String getTimeZoneId() {
		return this.timeZoneId;
	}
	
	public Double getLatitude() {
		return this.latitude;
	}
	
	public Double getLongitude() {
		return this.longitude;
	}
	
	public void setStationId(String newVal) {
		this.stationId = newVal;
	}
	
	public void setStationName(String newVal) {
		this.stationName = newVal;
	}
	
	public void setState(String newVal) {
		this.state = newVal;
	}
	
	public void setWFO(String newVal) {
		this.wfo = newVal;
	}
	
	public void setRadar(String newVal) {
		this.radar = newVal;
	}
	
	public void setTimeZoneId(String newVal) {
		this.timeZoneId = newVal;
	}
	
	public void setLatitude(Double newVal) {
		this.latitude = newVal;
	}
	
	public void setLongitude(Double newVal) {
		this.longitude = newVal;
	}
}
