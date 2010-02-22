package com.andago.semanthic.reasoner.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.andago.semanthic.reasoner.ifc.IReasoner;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class JenaReasoner implements IReasoner {

	private OntModel model = null;

	public JenaReasoner(String inputFilename, String inputFileFormat,
			String reasoningLevel) {
		this.createModel(inputFilename, inputFileFormat, reasoningLevel);
	}
	
	public void finalize() {
		this.model.close();
	}

	private void createModel(String inputFileName, String inputFileFormat,
			String reasoningLevel) {

		// create an input stream for the input file
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(inputFileName);
		} catch (FileNotFoundException e) {
			System.err.println("'" + inputFileName
					+ "' is an invalid input file.");
			return;
		}
		// create the appropriate jena model
		if ("none".equals(reasoningLevel.toLowerCase())) {
			/*
			 * "none" is jena model with OWL_DL ontologies loaded and no
			 * inference enabled
			 */
			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		} else if ("rdfs".equals(reasoningLevel.toLowerCase())) {
			/*
			 * "rdfs" is jena model with OWL_DL ontologies loaded and RDFS
			 * inference enabled
			 */
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		} else if ("owl".equals(reasoningLevel.toLowerCase())) {
			/*
			 * "owl" is jena model with OWL_DL ontologies wrapped around a
			 * pellet-based inference model
			 */
			model = ModelFactory
					.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		}
		
		//OntDocumentManager dm = model.getDocumentManager();
		//dm.addAltEntry( "http://qallme.itc.it/ontology/qallme-tourism.owl",
		//                  "file:d:/ontologies/qallme-tourism4.0.owl"    );
		//model.read( "http://qallme.itc.it/ontology/qallme-tourism.owl" );
		//model.read(inputStream, "RDF/XML");
		//model.read("file:d:/ontologies/qallme-tourism4.0.owl");
		 //load the facts into the model
	    model.read(inputStream, null, inputFileFormat);
	}
	
	public OntModel getModel() {
		return this.model;
	}
	
	public void validate() {
		// validate the file
		ValidityReport validityReport = model.validate();
		if (validityReport != null && !validityReport.isValid()) {
			Iterator i = validityReport.getReports();
			while (i.hasNext()) {
				System.err.println(((ValidityReport.Report) i.next())
						.getDescription());
			}
			return;
		} else {
			System.out.println("The model is valid");
		}
	}
	
	
	public void printIndividuals(PrintWriter writer) {
		// Iterate over the individuals, print statements about them
		ExtendedIterator iIndividuals = model.listIndividuals();
		while (iIndividuals.hasNext()) {
			Individual i = (Individual) iIndividuals.next();
			printIndividual(i, writer);
		}
		iIndividuals.close();

		writer.close();
	}

	/**
	 * Print information about the individual
	 * 
	 * @param i
	 *            The individual to output
	 * @param writer
	 *            The writer to which to output
	 */
	private void printIndividual(Individual i, PrintWriter writer) {
		// print the local name of the individual (to keep it terse)
		writer.println("Individual: " + i.getLocalName());

		// print the statements about this individual
		StmtIterator iProperties = i.listProperties();
		while (iProperties.hasNext()) {
			Statement s = (Statement) iProperties.next();
			writer.println("  " + s.getPredicate().getLocalName() + " : "
					+ s.getObject().toString());
		}
		iProperties.close();
		writer.println();
	}

}