package net.diversite.beta.store;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DC_11;
import com.hp.hpl.jena.util.FileManager;

public class OntologyManager {
	
	public OntologyManager() {
		
		OntModel model = null;	   
		// crear un modelo utilizando como razonador OWL_MEM_RULE_INF
		model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF );
		// abrir el archivo con la ontolia
		java.io.InputStream in = FileManager.get().open( "d:\\ontologias\\geonames_v2.0_Lite.rdf" );
		if (in == null) {
		    throw new IllegalArgumentException("Archivo no encontrado");
		}
	}
}