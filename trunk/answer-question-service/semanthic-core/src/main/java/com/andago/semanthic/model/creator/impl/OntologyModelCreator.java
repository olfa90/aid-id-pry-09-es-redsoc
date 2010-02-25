package com.andago.semanthic.model.creator.impl;

import java.util.Iterator;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import org.apache.log4j.Logger;
import com.andago.semanthic.model.creator.ifc.IModelCreator;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.ontology.Individual;

public class OntologyModelCreator implements IModelCreator {
	
	public static final String ONT1 = "http://xmlns.com/foaf/0.1/";
	private static Logger log = Logger.getLogger(OntologyModelCreator.class);
	private String ontologyURL = "";
	public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	
	public OntologyModelCreator(String ontoURL) {
		this.ontologyURL = ontoURL;
	}
	
	@Override
	public Model create(ModelMaker maker) {
		String s_source = getDefaultSource();
		Model base = maker.createModel(s_source);
		OntModel m = ModelFactory.createOntologyModel(
				this.getModelSpec(maker), base );
		m.read(ONT1);
		
		/*Resource personClass = m.getResource( FOAF_NS + "Person" );
		for (Iterator<Individual> i = m.listIndividuals(personClass); i.hasNext(); ) {
			Individual individual = i.next();
			log.debug("***Individual: " + individual.getURI());
		}
		for (Iterator<OntClass> i = m.listClasses(); i.hasNext(); ) {
            OntClass c = i.next();
            log.debug("++++Class " + c.getURI() );
            StmtIterator it = c.listProperties();
            while(it.hasNext()) {
            	Statement smtp = (Statement)it.next();
            	log.debug("------------------------------------------");
            	log.debug("Subject: " + smtp.getSubject() +
            			" Predicado: " + smtp.getPredicate() + 
            			" Object: " + smtp.getObject());
            	log.debug("------------------------------------------");
            }
        }*/
		return m;
	}
	
	private OntModelSpec getModelSpec(ModelMaker maker) {
        // create a spec for the new ont model that will use no inference over models
        // made by the given maker (which is where we get the persistent models from)
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
        spec.setImportModelMaker(maker);
        return spec;
    }
	
	/**
	 * Answer the default source document, and set up the document manager so
	 * that we can find it on the file system
	 *
	 * @return The URI of the default source document
	 */
	private  String getDefaultSource() {
		// use the ont doc mgr to map from a generic URN to a local source file
		/*OntDocumentManager.getInstance().addAltEntry(ONT1,
				"file:d:/ontologias/geonames_v2.0_Lite.rdf");*/
		OntDocumentManager.getInstance().addAltEntry(ONT1, 
				this.ontologyURL);
		return ONT1;
	}
}
