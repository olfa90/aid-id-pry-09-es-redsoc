package com.andago.semanthic.reasoner.impl;

import java.io.PrintWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

@Ignore
public class JenaReasonerTest {
	
	private JenaReasoner reasoner;
	
	@Before
	public void setUp() {
		this.reasoner = new JenaReasoner("D:\\ontologies\\questions_users.owl", "RDF/XML", "rdfs");
	}
	
	@Test
	public void validate() {
		this.reasoner.validate();
	}
	
	@Test
	public void testPrintIndividuals() {
		PrintWriter writer = new PrintWriter(System.out);
		this.reasoner.printIndividuals(writer);
	}
}
