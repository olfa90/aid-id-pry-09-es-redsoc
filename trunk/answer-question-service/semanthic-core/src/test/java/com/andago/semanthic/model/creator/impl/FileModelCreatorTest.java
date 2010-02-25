package com.andago.semanthic.model.creator.impl;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class FileModelCreatorTest {
	
	private FileModelCreator instance;
	private static Logger log = Logger.getLogger(FileModelCreatorTest.class);
	
	@Before
	public void setUp() throws Exception {
		String dbURL = "jdbc:derby:D:\\workspace\\semanthic-app\\data_store\\semanthicdb;create=true";
		String dbUser = "root";
		String dbPw = "demonio";
		String dbType = "Derby";
		boolean cleanDB = true;
		/*this.instance = new OntologyModelCreator(dbURL, dbUser, 
				dbPw, dbType,cleanDB,"file:d:/ontologies/qallme-tourism4.0.owl");*/
		this.instance =  new FileModelCreator();
	}
	
	
	@Test
	public void testCreate() {
		log.debug("Instance: " + this.instance);
	}
	
	
	
}
