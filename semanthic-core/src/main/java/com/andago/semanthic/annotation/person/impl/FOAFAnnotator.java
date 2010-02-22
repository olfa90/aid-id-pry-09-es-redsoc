package com.andago.semanthic.annotation.person.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.andago.semanthic.annotation.person.ifc.IPersonAnnotable;
import com.andago.semanthic.dynamic.DynamicClassGenerator;
import org.apache.log4j.Logger;
import org.apache.commons.beanutils.DynaBean;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Statement;
import com.andago.semanthic.exception.PersonNotFoundException;
import java.io.File;
import java.io.OutputStream;


public class FOAFAnnotator implements IPersonAnnotable {
	
	private static Logger log = Logger.getLogger(FOAFAnnotator.class);
	private Model model;
	private DynamicClassGenerator dynamicGenerator;
	public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	public static String APP_NS = "http://bydefault.org/";
	
	
	public FOAFAnnotator(String dbURL, String dbUser, 
			String dbPw, String dbType, boolean cleanDB,
			String namespace) 
		throws SQLException {
		this.createModel(dbURL, dbUser, dbPw, dbType, 
				cleanDB);
		this.dynamicGenerator = new DynamicClassGenerator();
		Map<String,Class> properties = new HashMap<String,Class>();
		properties.put("mbox", String.class);
		properties.put("name", String.class);
		properties.put("id", String.class);
		this.dynamicGenerator.createDynamicClass(FOAF.Person.getClass().getName(), 
				properties);
		APP_NS = namespace;
	}
	
	
	private void createModel(String dbURL, String dbUser, 
			String dbPw, String dbType, boolean cleanDB) 
		throws SQLException {
		ModelMaker maker = this.getRDBMaker(dbURL, dbUser, 
				dbPw, dbType, cleanDB);	
		this.model = maker.createModel("foaf");
		this.model.begin();
		this.model.read(FOAF_NS, "RDF/XML");
		this.model.commit();
	}
	
	
	private ModelMaker getRDBMaker( String dbURL, String dbUser, 
			 String dbPw, String dbType, boolean cleanDB) 
	 			throws SQLException {
	       // Create database connection
	       IDBConnection conn  = new DBConnection( dbURL, dbUser, 
	    		   dbPw, dbType );
	       // do we need to clean the database?
	       if (cleanDB) {
	    	   conn.cleanDB();
	       }
	       // Create a model maker object
	       ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
	       return maker;
	}
	
	@Override
	public void addPerson(String email, String name, 
			String surname) throws Exception {
		if(this.existsPerson(email)) {
			return;
		}
		this.model.begin();
		String resourceURI = APP_NS + "#" + name + surname;
		Resource me = 
			this.model.createResource(resourceURI);
		me.addProperty(RDF.type, FOAF.Person);
		me.addProperty(FOAF.name, name + " " + surname);
		me.addProperty(FOAF.givenname, name);
		me.addProperty(FOAF.family_name, surname);
		me.addProperty(FOAF.mbox, email);
		this.model.commit();
	}
	
	
	@Override
	public void addTopic(String topickey, String topicDescription) {
		this.model.begin();
		String resourceURI = APP_NS + "#" + topickey;
		Resource me = 
			this.model.createResource(resourceURI);
		me.addProperty(RDFS.label, topicDescription);
		this.model.commit();
	}
	
	public void addTopicsToPerson(String email, List<String> topics) 
		throws PersonNotFoundException {
		Resource person = this.findPerson(email);
		for(String topic : topics) {
			person.addProperty(FOAF.topic_interest, topic);
		}
	}
	
	public DynaBean getPerson(String email) 
		throws PersonNotFoundException, Exception {
		Resource person = this.findPerson(email);
		return this.toBean(person);
	}
	
	private boolean existsPerson(String email) {
		ResIterator i = this.model.listSubjectsWithProperty( RDF.type, FOAF.Person );
        // for each person, show their foaf:name if known
        Property mbox = this.model.getProperty( FOAF_NS + "mbox" );
        while (i.hasNext()) {
            Resource person = i.nextResource();
            Statement nm = person.getProperty( mbox );   
            if (nm != null) {
                log.debug("MBox: " + nm.getString());
            	if(email.equals(nm.getString())) {
                	return true;
                }
            }
        }
        return false;
	}
	
	private Resource findPerson(String email) 
		throws PersonNotFoundException {
        ResIterator i = this.model.listSubjectsWithProperty( RDF.type, FOAF.Person );
        // for each person, show their foaf:name if known
        Property mbox = this.model.getProperty( FOAF_NS + "mbox" );
        while (i.hasNext()) {
            Resource person = i.nextResource();
            Statement nm = person.getProperty( mbox );   
            if (nm != null) {
                log.debug("MBox: " + nm.getString());
            	if(email.equals(nm.getString())) {
                	return person;
                }
            }
        }
        throw new PersonNotFoundException("The person " + email + 
        		" doesn't exist.");
	}
	
	
	private DynaBean toBean(Resource person) 
		throws Exception {
		Property name = this.model.getProperty( FOAF_NS + "name" );
        Property mbox = this.model.getProperty( FOAF_NS + "mbox" );
        Map<String, Object> values = new HashMap<String, Object>();
        Statement nm = person.getProperty( mbox );
        values.put("id", person.getURI().toString());
        values.put("mbox", nm.getString());
        nm = person.getProperty( name );
        if (nm != null) {
            values.put("name", nm.getString());
        }
        DynaBean personBean = this.dynamicGenerator.instanceBean(
        		FOAF.Person.getClass().getName(), values);
        return personBean;
	}

	
}