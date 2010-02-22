package com.andago.semanthic.dynamic;

import org.ontoware.rdfreactor.generator.CodeGenerator;


public class RDFReactorGenerator {
	
	public void generateClasses(String ontoFilePath, String outputDir, 
			String targetPackage) throws Exception {
		String semantics = CodeGenerator.SEMANTICS_RDFS;
		boolean skipBuiltins = true;
		boolean alwaysWriteToModel = true;
		String prefix = "";
		CodeGenerator.generate(ontoFilePath, outputDir, targetPackage, 
				semantics, skipBuiltins, alwaysWriteToModel, prefix);
	}
	
}