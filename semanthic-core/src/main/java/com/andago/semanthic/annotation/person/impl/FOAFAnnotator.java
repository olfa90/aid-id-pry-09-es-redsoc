package com.andago.semanthic.annotation.person.impl;

import java.util.HashMap;
import java.util.Iterator;
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
import com.andago.semanthic.model.drivven.ifc.IDrivven;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Statement;
import com.andago.semanthic.exception.PersonNotFoundException;
import java.io.OutputStream;

public class FOAFAnnotator implements IPersonAnnotable {
	
	private static Logger log = Logger.getLogger(FOAFAnnotator.class);
	private Model aBox;
	private OntModel tBox;
	private IDrivven drivver;
	private DynamicClassGenerator dynamicGenerator;
	public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	
	
	public FOAFAnnotator(IDrivven drivv) {
		this.drivver = drivv;
		this.aBox = this.drivver.getAModel();
		this.tBox = this.drivver.getTModel();
		this.dynamicGenerator = new DynamicClassGenerator();
		Map<String,Class> properties = new HashMap<String,Class>();
		properties.put("mbox", String.class);
		properties.put("name", String.class);
		this.dynamicGenerator.createDynamicClass(FOAF.Person.getClass().getName(), 
				properties);
	}
	
	public void addPerson(String email, String name, 
			String surname, String... extraParams) throws Exception {
		String resourceURI = "http://" + name + "." + 
			surname + ".de/foaf.rdf#cygri";
		Resource me = 
			this.aBox.createResource(resourceURI);
		Map<String, String> props = this.extractClassProperties();
		for(String pred : props.keySet()) {
			log.debug("+++" + pred + "-" + props.get(pred));
		}
		me.addProperty(RDF.type, FOAF.Person);
		me.addProperty(FOAF.name, name + " " + surname);
		me.addProperty(FOAF.givenname, name);
		me.addProperty(FOAF.family_name, surname);
		me.addProperty(FOAF.mbox, email);	
	}
	
	public void addTopic(String topicId, String topicDescription) {
		String resourceURI = "http://test.com#" + topicId;
		Resource me = 
			this.aBox.createResource();
			//.createResource(resourceURI);
		me.addProperty(RDFS.label, topicDescription);
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
	
	public void showModel(OutputStream out) {
		this.aBox.write(out, "RDF/XML");
	}
	
	private Resource findPerson(String email) 
		throws PersonNotFoundException {
		Resource personClass = this.aBox.getResource( FOAF_NS + "Person" );
        ResIterator i = this.aBox.listSubjectsWithProperty( RDF.type, personClass );
        // for each person, show their foaf:name if known
        Property name = this.aBox.getProperty( FOAF_NS + "name" );
        Property mbox = this.aBox.getProperty( FOAF_NS + "mbox" );
        Map<String, Object> values = new HashMap<String, Object>();
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
		Property name = this.aBox.getProperty( FOAF_NS + "name" );
        Property mbox = this.aBox.getProperty( FOAF_NS + "mbox" );
        Map<String, Object> values = new HashMap<String, Object>();
        Statement nm = person.getProperty( mbox );
        values.put("mbox", nm.getString());
        nm = person.getProperty( name );
        if (nm != null) {
            values.put("name", nm.getString());
        }
        DynaBean personBean = this.dynamicGenerator.instanceBean(
        		FOAF.Person.getClass().getName(), values);
        return personBean;
	}
	
	private Map<String, String> extractClassProperties() {
		Map<String, String> props = new HashMap<String, String>();
		OntClass personClass = this.tBox.getOntClass(FOAF.Person.getURI());
		for (Iterator<OntProperty> i = personClass.listDeclaredProperties(); i.hasNext(); ) {
			OntProperty oProp = i.next();
			if(oProp.isDatatypeProperty()) {
				props.put(oProp.getLocalName(), 
						oProp.asDatatypeProperty().getLocalName());
			}
		}
		return props;
	}
	
}