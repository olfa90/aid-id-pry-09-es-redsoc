package com.andago.semanthic.annotation.person.impl;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.andago.semanthic.annotation.person.ifc.IPersonAnnotable;
import com.andago.semanthic.dynamic.DynamicClassGenerator;
import org.apache.log4j.Logger;
import org.apache.commons.beanutils.DynaBean;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Statement;
import com.andago.semanthic.exception.PersonNotFoundException;
import com.andago.util.HashGenerator;


/**
 * The class is a FOAF annotator that uses a database to store
 * the annotations.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
public class FOAFAnnotator implements IPersonAnnotable {
	
	/*The logger.*/
	private static Logger log = Logger.getLogger(FOAFAnnotator.class);
	/*An instance of RDF model.*/
	private Model model;
	/*An instance of dynamic generator of beans.*/
	private DynamicClassGenerator dynamicGenerator;
	/*The namespace of FOAF ontology.*/
	public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	/*Default application namespace to create resorces URI.*/
	public static String APP_NS = "http://bydefault.org/";
	
	/**
	 * Constructor of the class.
	 * 
	 * @param dbURL complete database URL.
	 * @param dbUser database user.
	 * @param dbPw database user's password.
	 * @param dbType type of database. These type is the same
	 * of Jena DB support.
	 * @param cleanDB true of false, to delete data from
	 * database.
	 * @param namespace application namespace to generate
	 * resources URIs.
	 * @throws SQLException any database error.
	 */
	public FOAFAnnotator(String dbURL, String dbUser, 
			String dbPw, String dbType, boolean cleanDB,
			String namespace) 
		throws SQLException {
		this.createModel(dbURL, dbUser, dbPw, dbType, 
				cleanDB);
		this.dynamicGenerator = new DynamicClassGenerator();
		Map<String,Class> properties = new HashMap<String,Class>();
		properties.put("mbox", String.class);
		properties.put("mbox_sha1sum", String.class);
		properties.put("name", String.class);
		properties.put("id", String.class);
		this.dynamicGenerator.createDynamicClass(FOAF.Person.getClass().getName(), 
				properties);
		APP_NS = namespace;
	}
	
	
	/* (non-Javadoc)
	 * @see com.andago.semanthic.annotation.person.ifc.IPersonAnnotable#annotatePerson(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void annotatePerson(String email, String name, 
			String familyName) throws Exception {
		if(this.existsPerson(email)) {
			return;
		}
		this.model.begin();
		String shaSum = HashGenerator.getSuminHex(email, 
				"SHA");
		String resourceURI = APP_NS + "#" + shaSum;
		Resource me = 
			this.model.createResource(resourceURI);
		me.addProperty(RDF.type, FOAF.Person);
		me.addProperty(FOAF.name, name + " " + familyName);
		me.addProperty(FOAF.givenname, name);
		me.addProperty(FOAF.family_name, familyName);
		me.addProperty(FOAF.mbox_sha1sum, shaSum);
		Resource mail = this.model.createResource
			("mailto:" + email);
		me.addProperty(FOAF.mbox, mail);
		this.model.commit();
	}
	
	
	/* (non-Javadoc)
	 * @see com.andago.semanthic.annotation.person.ifc.IPersonAnnotable#getPerson(java.lang.String)
	 */
	@Override
	public DynaBean getPerson(String email) 
		throws PersonNotFoundException, Exception {
		Resource person = this.findPerson(email);
		if(person == null) {
			throw new PersonNotFoundException(
					"Person with email " + email + 
					" doesn`t exist.");
		}
		return this.toBean(person);
	}
	
	/* (non-Javadoc)
	 * @see com.andago.semanthic.annotation.person.ifc.IPersonAnnotable#addInterestTopicToPerson(java.lang.String, java.lang.String)
	 */
	@Override
	public void addInterestTopicToPerson(String email, 
			String topic) throws PersonNotFoundException, 
			NoSuchAlgorithmException {
		Resource person = this.findPerson(email);
		Resource r_topic = this.model.createResource(topic);
		this.model.begin();
		person.addProperty(FOAF.topic_interest, r_topic);
		this.model.commit();	
	}
	
	/**
	 * Method to print model throw a writer.
	 * 
	 * @param writer the output element for the method.
	 */
	public void showModel(PrintWriter writer) {
		this.model.write(writer);
	}
	
	
	/**
	 * Method to create the KB model into a database.
	 * 
	 * @param dbURL complete database URL.
	 * @param dbUser database user.
	 * @param dbPw database user's password.
	 * @param dbType type of database. These type is the same
	 * of Jena DB support.
	 * @param cleanDB true of false, to delete data from
	 * database.
	 * @throws SQLException if any database error happened.
	 */
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
	
	
	/**
	 * Method to create an internal RDBMaker to create the
	 * model into a database.
	 * 
	 * @param dbURL complete database URL.
	 * @param dbUser database user.
	 * @param dbPw database user's password.
	 * @param dbType type of database. These type is the same
	 * of Jena DB support.
	 * @return the created model maker.
	 * @throws SQLException if any database error happened.
	 */
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
	
	/**
	 * Method to check if exist a person with the given email.
	 * 
	 * @param email person's email. 
	 * @return true or false if the person exists or no.
	 * @throws NoSuchAlgorithmException if not exist the
	 * algoritm to calcule the hash of email. 
	 */
	private boolean existsPerson(String email) 
		throws NoSuchAlgorithmException {
        Resource person = this.findPerson(email);
		boolean exists = person==null?false:true;
        return exists;
	}
	
	/**
	 * Method to find a person with the given email.
	 * 
	 * @param email person's email.
	 * @return a resource if the person exist. Otherwise, 
	 * return null.
	 * @throws NoSuchAlgorithmException if not exist the
	 * algoritm to calcule the hash of email. 
	 */
	private Resource findPerson(String email) 
		throws NoSuchAlgorithmException {
        ResIterator i = this.model.
        	listSubjectsWithProperty(RDF.type, FOAF.Person);
        // for each person, show their foaf:name if known
        // Property mbox = this.model.getProperty( FOAF_NS + "mbox" );
        String shaSum = HashGenerator.getSuminHex(email, "SHA");
        while (i.hasNext()) {
            Resource person = i.nextResource();
            Statement nm = person.getProperty( FOAF.mbox_sha1sum );   
            if (nm != null) {
                log.debug("MBox-SHA1Sum: " + nm.getString());
                log.debug("SHA1Sum to find: " + nm.getString());
            	if(shaSum.equals(nm.getString())) {
                	return person;
                }
            }
        }
        return null;
	}
	
	/**
	 * Method to convert a resource that represents an instance of
	 * Perso class into a java Dynabean.
	 * 
	 * @param person the resource to will be converted.
	 * @return a java Dynabean that contains all properties of the
	 * resource.
	 * @throws Exception if any error happened.
	 */
	private DynaBean toBean(Resource person) 
		throws Exception {    
        Map<String, Object> values = new HashMap<String, Object>();
        Statement nm = person.getProperty( FOAF.mbox_sha1sum );
        values.put("id", person.getURI().toString());
        values.put("mbox_sha1sum", nm.getString());
        nm = person.getProperty(FOAF.mbox);
        log.debug("isAnon " + FOAF.mbox.isAnon());
        if(nm != null) {
        	values.put("mbox", nm.getResource().getURI());
        }
        nm = person.getProperty( FOAF.name );
        log.debug("isAnon " + FOAF.name.isAnon());
        if (nm != null) {
            values.put("name", nm.getString());
        }
        DynaBean personBean = this.dynamicGenerator.instanceBean(
        		FOAF.Person.getClass().getName(), values);
        return personBean;
	}

	
}