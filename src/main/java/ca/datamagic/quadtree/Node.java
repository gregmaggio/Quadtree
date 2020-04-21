/**
 * 
 */
package ca.datamagic.quadtree;

import java.io.Serializable;

/**
 * @author Greg
 *
 */
public class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	private Point point = null;
	private Station station = null;
	
	public Node() {
		
	}
	
	public Node(Point point, Station station) {
		this.point = point;
		this.station = station;
	}
	
	public Point getPoint() {
		return this.point;
	}
	
	public Station getStation() {
		return this.station;
	}
}
