package com.andago.question.storage.impl;

import java.io.File;
import java.io.FileOutputStream;
import org.jdom.output.XMLOutputter;
import java.io.IOException;
import java.util.Calendar;
import com.andago.question.storage.ifc.IAnswerStore;
import com.andago.question.exception.storage.StorageCreationException;
import com.andago.question.exception.storage.StorageAccessException;

/**
 * @author eduardo.perrino
 *
 */
public class FileSystemAnswerStore implements IAnswerStore {
	
	private final static String storageName = "answers";
	private String storagePath = "";
	
	
	/**
	 * @param stPath
	 */
	public FileSystemAnswerStore(String stPath) {
		this.storagePath = stPath + File.separator + storageName;
	}	


	/* (non-Javadoc)
	 * @see com.andago.question.storage.ifc.IAnswerStore#storeAnswer(java.lang.String, java.lang.Integer, java.lang.String, org.jdom.Document)
	 */
	public void storeAnswer(String personEmail, 
			Integer questionId, String language, org.jdom.Document answer) 
			throws StorageCreationException,
			StorageAccessException, IOException, Exception {
		String destinationName = questionId 
			+ "_" + language + ".xml";
		String personStoragePath = this.storagePath + 
			File.separator + personEmail;
		File personStore = new File(personStoragePath);
		if(!personStore.exists()) {
			this.createPersonStore(personStore);
		}
		this.checkStoreAccess(personStore);
		File destinationFile = new File(personStore.getAbsolutePath() + 
				File.separator + destinationName);
		if(destinationFile.exists()) {
			if(!this.saveOldAnswer(destinationFile)) {
				throw new Exception("Not posible delete old answer (" +
						destinationFile.getAbsolutePath()+ ")");
			}
		}
		if(!destinationFile.createNewFile()) {
			throw new StorageAccessException("Not posible " +
					" create destination file (" +
					destinationFile.getAbsolutePath() + ").");
		}
		FileOutputStream fos = new FileOutputStream(destinationFile);
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(answer, fos);
		fos.flush();
		fos.close();
	}
	
	/**
	 * These method store an old answer to a given question. Invoked when
	 * a question is answer again. It's importat register all answers for
	 * a given question.
	 * @param oldAnswer the file that contains the old answer.
	 * @return true or false if the old answer is saved or not.
	 */
	private boolean saveOldAnswer(File oldAnswer) {
		long now = Calendar.getInstance().getTimeInMillis();
		String newName = oldAnswer.getName().split("[.xml]")[0] + 
			"_" + now;
		String newLocation = oldAnswer.getParentFile().
			getAbsoluteFile() + File.separator + newName + ".xml";
		return oldAnswer.renameTo(new File(newLocation));
	}
	
	/**
	 * Method to check the permissions of a person's storage system.
	 * 
	 * @param personStore folder that represents the person's storage
	 * system.
	 * @throws StorageAccessException if the person hasn't permissions
	 * to access to his answer storage.
	 */
	private void checkStoreAccess(File personStore) 
		throws StorageAccessException {
		if(!personStore.canRead()) {
			throw new StorageAccessException("Not posible " +
					" read operation in storage location (" +
					personStore.getAbsolutePath() + ").");
		}
		if(!personStore.canWrite()) {
			throw new StorageAccessException("Not posible " +
					" write operation in storage location (" +
					personStore.getAbsolutePath() + ")");
		}
	}
	
	/**
	 * Method to create an answer store for a person.
	 * 
	 * @param store the folder that will be contained the
	 * person's answer for given questions.
	 * @throws StorageCreationException if occurred any problem in
	 * the creation process.
	 */
	private void createPersonStore(File store) 
		throws StorageCreationException {
		if(!store.mkdirs()) {
			throw new StorageCreationException("Not posible " +
					"create store in " + store.getAbsolutePath());
		}
	}
	
}