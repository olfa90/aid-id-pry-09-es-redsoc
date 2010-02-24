package com.andago.semanthic.annotation.person.ifc;

import java.security.NoSuchAlgorithmException;
import org.apache.commons.beanutils.DynaBean;
import com.andago.semanthic.exception.PersonNotFoundException;

public interface IPersonAnnotable {
	
	/**
	 * Method to annotate a person into KB. If the mail 
	 * exists into de KB, the system will make a modication.
	 * 
	 * @param email the person's email. These mail is the unique
	 * identifier for all persons. 
	 * @param name the person's name.
	 * @param familyName the person's name.
	 * @throws Exception if any error happened.
	 */
	public void annotatePerson(String email, String name, 
			String familyName) throws Exception;
	
	/**
	 * Method to get an annotated person.
	 * @param email the person's email.These mail is the unique
	 * identifier for all persons.
	 * @return a dynabean that contains all annotated properties.
	 * @throws PersonNotFoundException if the person doesn't exist
	 * into the KB.
	 * @throws Exception if any error happened.
	 */
	public DynaBean getPerson(String email) throws PersonNotFoundException, Exception;
	
	/**
	 * Method to add an interest topic for a person.
	 * 
	 * @param email the person's email .
	 * @param topic the URI topic to add.
	 * @throws PersonNotFoundException if the person doesn't exist
	 * into the KB.
	 * @throws NoSuchAlgorithmException Hashing method error.
	 */
	public void addInterestTopicToPerson(String email, 
			String topic) throws PersonNotFoundException, 
			NoSuchAlgorithmException;
	
}