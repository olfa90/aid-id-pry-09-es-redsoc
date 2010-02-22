package com.andago.semanthic.dynamic;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RDFReactorGeneratorTest {
	
	private RDFReactorGenerator instance;
	
	@Before
	public void setUp() {
		this.instance = new RDFReactorGenerator();
	}
	
	@Test
	public void testGenerateClasses() throws Exception {
		String rdfsFile = "D:\\ontologies\\qallme-tourism4.0.owl";
		String outDir = "./src/main/java/";
		String targetPackage = "com.andago.qallme.classes.gen";
		this.instance.generateClasses(rdfsFile, outDir, targetPackage);
	}
}