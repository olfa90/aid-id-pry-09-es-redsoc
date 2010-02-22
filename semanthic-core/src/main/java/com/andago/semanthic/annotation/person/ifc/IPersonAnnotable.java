package com.andago.semanthic.annotation.person.ifc;

import org.apache.commons.beanutils.DynaBean;

public interface IPersonAnnotable {
	
	public void addPerson(String email, String name, 
			String surname) throws Exception;
	public DynaBean getPerson(String email) throws Exception;
	public void addTopic(String topickey, String topicDescription);
}