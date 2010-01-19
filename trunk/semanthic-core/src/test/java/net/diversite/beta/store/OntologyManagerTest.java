package net.diversite.beta.store;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

@Ignore
public class OntologyManagerTest {
	
	private OntologyManager ontology_manager  = null;
	private static Logger log = Logger.getLogger(OntologyManagerTest.class);
	
	
	public OntologyManagerTest() {
		
	}
	
	@Before
	public void setUp() {
		this.ontology_manager = new OntologyManager();
	}
	
	@Test
	public void test1() {
		log.debug("testing 1......................");
	}
	
}