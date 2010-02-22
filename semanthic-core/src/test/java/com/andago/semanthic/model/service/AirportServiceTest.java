package com.andago.semanthic.model.service;

import java.io.IOException;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;


public class AirportServiceTest {
	
	private AirportService instance;
	
	@Before
	public void setUp() throws ModelRuntimeException, IOException {
		this.instance = new AirportService("D:\\ontologies\\qallme.ttl");
	}
	
	@Test
	public void testAddAirport() throws IOException {
		this.instance.addAirport();
		this.instance.showAirportsInfo();
		this.instance.getAirportsURI();
	}
	
	
}