/**
 * 
 */
package ca.datamagic.quadtree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Greg
 *
 */
public class QuadTester {
	private static final double distance = 25;
	private static final String units = "statute miles";
	private Quad tree = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure("src/test/resources/log4j.cfg.xml");
	}
	
	@Before
	public void setUp() throws Exception {
		this.tree = new Quad(new Point(74.72, -177.36667), new Point(-14.331, 174.11667));
		Station station1 = new Station();
		station1.setStationId("KALX");
		station1.setStationName("Alexander City, Thomas C Russell Field Airport");
		station1.setState("AL");
		station1.setLatitude(32.91472);
		station1.setLongitude(-85.96278);
		this.tree.insert(station1, station1.getLatitude(), station1.getLongitude());
		Station station2 = new Station();
		station2.setStationId("KSEM");
		station2.setStationName("Craig Field / Selma");
		station2.setState("AL");
		station2.setLatitude(32.35);
		station2.setLongitude(-86.98333);
		this.tree.insert(station2, station2.getLatitude(), station2.getLongitude());
		Station station3 = new Station();
		station3.setStationId("K1M4");
		station3.setStationName("Haleyville, Posey Field Airport");
		station3.setState("AL");
		station3.setLatitude(34.28028);
		station3.setLongitude(-87.60028);
		this.tree.insert(station3, station3.getLatitude(), station3.getLongitude());
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("src/test/resources/tree.ser"));
		outputStream.writeObject(this.tree);
		outputStream.close();
	}
	
	@After
	public void tearDown() throws Exception {
		this.tree = null;
	}
	
	@Test
	public void testReadNearest() throws Exception {
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("src/test/resources/tree.ser"));
		Quad tree = (Quad)inputStream.readObject();
		inputStream.close();
		Station station1 = tree.readNearest(32.91472, -85.96278, distance, units);
		Assert.assertNotNull(station1);
		Assert.assertTrue(station1.getStationId().compareToIgnoreCase("KALX") == 0);
		Station station2 = tree.readNearest(32.35, -86.98333, distance, units);
		Assert.assertNotNull(station2);
		Assert.assertTrue(station2.getStationId().compareToIgnoreCase("KSEM") == 0);
		Station station3 = tree.readNearest(34.28028, -87.60028, distance, units);
		Assert.assertNotNull(station3);
		Assert.assertTrue(station3.getStationId().compareToIgnoreCase("K1M4") == 0);
	}
}
