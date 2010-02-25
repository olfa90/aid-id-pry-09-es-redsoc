package com.andago.semanthic.model.drivven.impl;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.shared.PrefixMapping;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;
import com.andago.semanthic.model.drivven.ifc.IDrivven;
import com.andago.semanthic.model.creator.ifc.IModelCreator;

public class DBDrivven implements IDrivven {
	
	private ModelMaker maker;
	private IModelCreator aModelCreator;
	private IModelCreator tModelCreator;
	private Map<String, String> nsPrefixes = new HashMap<String, String>();
	
	public DBDrivven(String dbURL, String dbUser, 
			String dbPw, String dbType, boolean cleanDB,
			IModelCreator aModelCreator, 
			IModelCreator tModelCreator) throws Exception {
		this.maker = this.getRDBMaker(dbURL, dbUser, dbPw,
				dbType, cleanDB);
		this.aModelCreator = aModelCreator;
		this.tModelCreator = tModelCreator;
	}
	
	public DBDrivven(String dbURL, String dbUser, 
			String dbPw, String dbType, boolean cleanDB, 
			Map<String, String> prefixes,
			IModelCreator aModelCreator, 
			IModelCreator tModelCreator) throws Exception {
		this.maker = this.getRDBMaker(dbURL, dbUser, dbPw, 
				dbType, cleanDB);
		this.nsPrefixes.putAll(prefixes);
		this.aModelCreator = aModelCreator;
		this.tModelCreator = tModelCreator;
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



	@Override
	public Model getAModel() {
		Model m = this.aModelCreator.create(maker);
		PrefixMapping prefixMapping = PrefixMapping.Factory.create(); 
		prefixMapping.setNsPrefixes(this.nsPrefixes);
		m.withDefaultMappings(prefixMapping);
		return m;
	}
	
	public OntModel getTModel() {
		OntModel model = (OntModel)this.tModelCreator.create(maker);
		return model;
	}
	 
}