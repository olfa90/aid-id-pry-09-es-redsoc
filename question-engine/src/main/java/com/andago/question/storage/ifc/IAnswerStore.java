package com.andago.question.storage.ifc;

import java.io.IOException;
import com.andago.question.exception.storage.StorageAccessException;
import com.andago.question.exception.storage.StorageCreationException;

/**
 * Interface to provide methods to store the person's answers for 
 * proposed questions.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
public interface IAnswerStore {
	
	/**
	 * Method to store a person's answer for a given question.
	 * 
	 * @param personEmail the person's email.
	 * @param questionId identifier of the answered question.
	 * @param language language of the given question.
	 * @param answer code of the selected answer.
	 * @throws StorageCreationException error at the creation of the answer's 
	 * storage.
	 * @throws StorageAccessException error accessing at the answer's 
	 * storage.
	 * @throws IOException any IO error.
	 * @throws Exception any error.
	 */
	public void storeAnswer(String personEmail, 
			Integer questionId, String language, org.jdom.Document answer) 
			throws StorageCreationException,
			StorageAccessException, IOException, Exception;
	
}