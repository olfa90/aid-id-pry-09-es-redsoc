package com.andago.semanthic.model.creator.ifc;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelMaker;

public interface IModelCreator {
	
	public Model create(ModelMaker maker);
	
}