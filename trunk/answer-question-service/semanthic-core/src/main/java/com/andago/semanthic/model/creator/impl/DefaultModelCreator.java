package com.andago.semanthic.model.creator.impl;

import com.andago.semanthic.model.creator.ifc.IModelCreator;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelMaker;

public class DefaultModelCreator implements IModelCreator {
	
	@Override
	public Model create(ModelMaker maker) {
		return maker.createDefaultModel();
	}

}
