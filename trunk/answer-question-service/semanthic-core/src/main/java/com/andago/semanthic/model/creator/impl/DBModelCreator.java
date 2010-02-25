package com.andago.semanthic.model.creator.impl;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

public class DBModelCreator {
	
	private static Logger log = Logger.getLogger(DBModelCreator.class);
	private OntModel ontModel;
	
	
	public DBModelCreator(String dbURL, String dbUser, 
			String dbPw, String dbType, boolean cleanDB,
				String ontoURL) throws Exception {
		this.ontModel = this.create(this.getRDBMaker(dbURL, dbUser, 
				dbPw, dbType, cleanDB), ontoURL);
	}
	
	private ModelMaker getRDBMaker( String dbURL, String dbUser, 
			 String dbPw, String dbType, boolean cleanDB ) 
	 			throws SQLException {
	       // Create database connection
	       IDBConnection conn  = new DBConnection( dbURL, dbUser, 
	    		   dbPw, dbType );
	       // do we need to clean the database?
	       if (cleanDB) {
	    	   conn.cleanDB();
	       }
	       // Create a model maker object
	       ModelMaker maker = ModelFactory.createModelRDBMaker( conn );
	       return ModelFactory.createModelRDBMaker( conn );
	}
	
	private OntModel create(ModelMaker maker, String ontoName) {
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
        spec.setImportModelMaker(maker);
		OntModel m = ModelFactory.createOntologyModel(spec);
		OntDocumentManager dm = m.getDocumentManager();
		dm.addAltEntry( "http://qallme.itc.it/ontology/qallme-tourism.owl",
		                  "file:d:/ontologies/qallme-tourism4.0.owl"    );
		m.read( "http://qallme.itc.it/ontology/qallme-tourism.owl" );
		return m;
	}
	
	
}
