/**
 * 
 */
package ca.datamagic.quadtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Greg
 *
 */
public class Quad implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final double distanceFromCenter = 50000;
	private static final double radiusOfEarthMeters = 6371e3;
	private Point topLeft = null; 
	private Point botRight = null; 
	private LinkedList<Node> nodes = new LinkedList<Node>(); 
	private Quad topLeftTree = null; 
	private Quad topRightTree = null; 
	private Quad botLeftTree = null; 
	private Quad botRightTree = null;
	private double midLatitude = Double.NaN;
	private double midLongitude = Double.NaN;
    
	public Quad() {
		
	}
	
	public Quad(Point topLeft, Point botRight) {
		this.topLeft = topLeft;
		this.botRight = botRight;
		this.midLatitude = (topLeft.getLatitude() + botRight.getLatitude()) / 2;
		this.midLongitude = (topLeft.getLongitude() + botRight.getLongitude()) / 2;
	}
	
	public List<Station> list() {
		List<Station> list = new ArrayList<Station>();
		this.list(list);
		return list;
	}
	
	private void list(List<Station> list) {
		if (this.nodes != null) {
			for (int ii = 0; ii < this.nodes.size(); ii++) {
				Node node = this.nodes.get(ii);
				Station station = node.getStation();
				if (station != null) {
					list.add(station);
				}
			}
		}
		if (this.topLeftTree != null) {
			this.topLeftTree.list(list);
		}
		if (this.topRightTree != null) {
			this.topRightTree.list(list);
		}
		if (this.botLeftTree != null) {
			this.botLeftTree.list(list);
		}
		if (this.botRightTree != null) {
			this.botRightTree.list(list);
		}
	}
	
	public Station readNearest(double latitude, double longitude, double distance, String units) {
		double distanceInMeters = distanceToMeters(distance, units);
		LinkedList<SearchResult> results = new LinkedList<Quad.SearchResult>();
		this.readNearest(latitude, longitude, distanceInMeters, results);
		Node nearestNode = null;
		double nearestDistance = Double.NaN;
		for (int ii = 0; ii < results.size(); ii++) {
			SearchResult searchResult = results.get(ii);
			if (nearestNode == null) {
				nearestNode = searchResult.getNode();
				nearestDistance = searchResult.getDistance();
			} else if (searchResult.getDistance() < nearestDistance) {
				nearestNode = searchResult.getNode();
				nearestDistance = searchResult.getDistance();
			}
		}
		if (nearestNode != null) {
			return nearestNode.getStation();
		}
		return null;
	}
	
	private void readNearest(double latitude, double longitude, double distanceInMeters, LinkedList<SearchResult> results) {
		// Current quad cannot contain it 
	    if (!this.inBoundary(latitude, longitude)) {
	        return; 
	    }
	    
	    double distance = computeDistance(this.midLatitude, this.midLongitude, latitude, longitude);
	    if (!Double.isNaN(distance)) {
		    if (distance <= distanceFromCenter) {
		    	for (int ii = 0; ii < this.nodes.size(); ii++) {
		    		Node node = this.nodes.get(ii);
		    		Point point = node.getPoint();
		    		double distanceToNode = computeDistance(latitude, longitude, point.getLatitude(), point.getLongitude());
		    		if (distanceToNode <= distanceInMeters) {
		    			results.add(new SearchResult(node, distanceToNode));
		    		}
		    	}
		    	return;
		    }
	    }
	    
	    if (this.midLongitude > longitude) {
	    	if (this.midLatitude < latitude) {
	    		// Indicates topLeftTree
	    		this.topLeftTree.readNearest(latitude, longitude, distanceInMeters, results);
	    	} else {
	    		// Indicates botLeftTree
	    		this.botLeftTree.readNearest(latitude, longitude, distanceInMeters, results);
	    	}
	    } else {
	    	if (this.midLatitude < latitude) {
	    		// Indicates topRightTree
	    		this.topRightTree.readNearest(latitude, longitude, distanceInMeters, results);
	    	} else {
	    		// Indicates botRightTree
	    		this.botRightTree.readNearest(latitude, longitude, distanceInMeters, results);
	    	}
	    }
	}
	
	public void insert(Station station, double latitude, double longitude) {
		// Current quad cannot contain it 
	    if (!inBoundary(latitude, longitude)) {
	        return; 
	    }
	    
	    double distance = computeDistance(this.midLatitude, this.midLongitude, latitude, longitude);
	    if (!Double.isNaN(distance)) {
		    if (distance <= distanceFromCenter) {
		    	this.nodes.add(new Node(new Point(latitude, longitude), station));
		    	return;
		    }
	    }
	    
	    // We are at a quad of unit area 
	    // We cannot subdivide this quad further 
	    if ((Math.abs(topLeft.getLongitude() - botRight.getLongitude()) <= 1) && 
	    	(Math.abs(topLeft.getLatitude() - botRight.getLatitude()) <= 1)) {
	    	return;
	    }
	    
	    if (this.midLongitude > longitude) {
	    	if (this.midLatitude < latitude) {
	    		// Indicates topLeftTree
	    		if (this.topLeftTree == null) {
	    			this.topLeftTree = new Quad(
	    					new Point(this.topLeft.getLatitude(), this.topLeft.getLongitude()),
	    					new Point(this.midLatitude, this.midLongitude));
	    		}
	    		this.topLeftTree.insert(station, latitude, longitude);
	    	} else {
	    		// Indicates botLeftTree
	    		if (this.botLeftTree == null) {
	    			this.botLeftTree = new Quad(
	    					new Point(this.midLatitude, this.topLeft.getLongitude()),
	    					new Point(this.botRight.getLatitude(), this.midLongitude));
	    		}
	    		this.botLeftTree.insert(station, latitude, longitude);
	    	}
	    } else {
	    	if (this.midLatitude < latitude) {
	    		// Indicates topRightTree
	    		if (this.topRightTree == null) {
	    			this.topRightTree = new Quad(
	    					new Point(this.topLeft.getLatitude(), this.midLongitude),
	    					new Point(this.midLatitude, this.botRight.getLongitude()));
	    		}
	    		this.topRightTree.insert(station, latitude, longitude);
	    	} else {
	    		// Indicates botRightTree
	    		if (this.botRightTree == null) {
	    			this.botRightTree = new Quad(
	    					new Point(this.midLatitude, this.midLongitude),
	    					new Point(this.botRight.getLatitude(), this.botRight.getLongitude()));
	    		}
	    		this.botRightTree.insert(station, latitude, longitude);
	    	}
	    }
	}
	
	private static double distanceToMeters(double distance, String units) {
		if (units.compareToIgnoreCase("statute miles") == 0) {
			return distance * 1609.34;
		}
		return Double.NaN;
	}
	
	private static double computeDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
		latitude1 = Math.toRadians(latitude1);
		latitude2 = Math.toRadians(latitude2);
		double deltaLatitude = Math.toRadians(Math.abs(latitude1 - latitude2));
		double deltaLongitude = Math.toRadians(Math.abs(longitude1 - longitude2));
		double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
				Math.cos(latitude1) * Math.cos(latitude2) *
				Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = radiusOfEarthMeters * c;
		return distance;		
	}
	
	private boolean inBoundary(double latitude, double longitude) 
	{ 
	    return ((longitude >= topLeft.getLongitude()) &&
	    		(longitude <= botRight.getLongitude()) &&
	    		(latitude <= topLeft.getLatitude()) &&
	    		(latitude >= botRight.getLatitude()));
	}
	
	private class SearchResult {
		private Node node = null;
		private double distance = Double.NaN;
		
		public SearchResult(Node node, double distance) {
			this.node = node;
			this.distance = distance;
		}
		
		public Node getNode() {
			return this.node;
		}
		
		public double getDistance() {
			return this.distance;
		}
	}
}
