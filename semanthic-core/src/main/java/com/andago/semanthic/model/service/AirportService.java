package com.andago.semanthic.model.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
//import org.ontoware.rdfreactor.runtime.ReactorResult;
import com.andago.qallme.classes.gen.Airport;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;

public class AirportService {
	
	private Model model;
	private static Logger log = Logger.getLogger(AirportService.class);
	private String outFileName = "";
	
	public AirportService(String outFile) throws 
		ModelRuntimeException, IOException {
		this.outFileName = outFile;
		this.model = RDF2Go.getModelFactory().createModel();
		this.model.open();
		File rdfStoreFile = new File(this.outFileName);
		if (rdfStoreFile.exists()) {
			FileReader reader = new FileReader(rdfStoreFile);
			this.model.readFrom(reader, Syntax.Turtle);
		}
	}
	
	public void addAirport() throws IOException {
		this.model.open();
		Airport airport1 = new Airport(this.model, true);
		airport1.setAward("testing award2");
		FileWriter writer = new FileWriter(this.outFileName);
	    this.model.writeTo(writer, Syntax.Turtle);
	    this.model.commit();
		this.model.close();
	}
	
	
	
	public void showAirportsInfo() {
		this.model.open();
		Airport[] airports =  Airport.getAllInstances_as(this.model).asArray();
		for(Airport airport : airports) {
			log.debug("-- " + airport.getResource().asURI());
		}
	}
	
	public void getAirportsURI() {
		URI[] uris = Airport.MANAGED_URIS;
		for(URI uri : uris) {
			log.debug("Uri: " + uri.asJavaURI().toString());
		}
	}
	
}
