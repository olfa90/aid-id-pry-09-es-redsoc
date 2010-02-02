package com.andago.semanthic.dynamic;

import org.ontoware.rdfreactor.generator.CodeGenerator;


public class RDFReactorGenerator {
	
	public void generateClasses() throws Exception {
		String rdfsFile = "D:\\ontologies\\geonames_v2.0_Lite.rdf";
		String outDir = "./src/main/java/";
		String targetPackage = "com.andago.pizza.classes.gen";
		String semantics = CodeGenerator.SEMANTICS_RDFS;
		boolean skipBuiltins = true;
		boolean alwaysWriteToModel = true;
		String prefix = "";
		CodeGenerator.generate(rdfsFile, outDir, targetPackage, 
				semantics, skipBuiltins, alwaysWriteToModel, prefix);
	}
}