package com.andago.semanthic.annotation.person.ifc;

import java.util.List;
import org.apache.commons.beanutils.DynaBean;

public interface IPersonAnnotable {
	
	public void addPerson(String email, String name, 
			String surname, String... extraParams) throws Exception;
	
	
	public DynaBean getPerson(String email) throws Exception;
	
}