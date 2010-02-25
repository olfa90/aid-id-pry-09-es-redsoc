package com.andago.semanthic.model.creator.impl;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.query.ResultSet;

public class FileModelCreator {
	
	private static Logger log = Logger.getLogger(FileModelCreator.class);
	private OntModel ontModel;
	private OntModel tBox;
	private Model aBox;
	
	public FileModelCreator() throws Exception {		
		this.create();
		//log.debug("Valid model: " + this.ontModel.validate().isValid());
	}
	
	private void create() {
		Model tBox = this.loadTBox();
		Model aBox = this.loadABox();
		Reasoner reasoner = PelletReasonerFactory.theInstance().create();
		reasoner.bindSchema(tBox);
		//Reasoner reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(tBox);
		OntModelSpec spec = new OntModelSpec( PelletReasonerFactory.THE_SPEC );
		spec.setReasoner( reasoner );
		this.ontModel = ModelFactory.createOntologyModel( spec, aBox );
		
		log.debug("Validity: " + this.ontModel.validate());
	}
	
	
	
	private Model loadTBox() {
		Model tBox =  ModelFactory.createDefaultModel();
		tBox.read( "file:d:/ontologies/qallme-tourism4.0.owl" );
		return tBox;
	}
	
	private Model loadABox() {
		Model aBox = ModelFactory.createDefaultModel();
		aBox.read( "file:d:/ontologies/qallme.rdf" );
		return aBox;
	}
	
	
	private void launchQuery() {
		String queryString =
			"PREFIX  rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"+
			"PREFIX  qallme: <http://qallme.itc.it/ontology/qallme-tourism.owl#> \n"+
			"SELECT ?c \n"+
			"WHERE { ?c qallme:award 'testing award' } \n";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, this.ontModel);
		
		try{
			ResultSet results = qexec.execSelect();
			ResultSetFormatter.out(System.out, results);
		} finally {qexec.close();}
	}
	
}
