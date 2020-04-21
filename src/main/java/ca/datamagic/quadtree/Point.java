/**
 * 
 */
package ca.datamagic.quadtree;

import java.io.Serializable;

/**
 * @author Greg
 *
 */
public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	private double latitude = 0;
	private double longitude = 0;	
	
	public Point() {
		
	}
	
	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
}
