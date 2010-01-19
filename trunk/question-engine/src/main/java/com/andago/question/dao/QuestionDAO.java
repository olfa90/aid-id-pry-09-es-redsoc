package com.andago.question.dao;

import java.util.Collection;
import com.andago.question.entity.Person;
import com.andago.question.entity.Proposal;
import com.andago.question.entity.Question;
import com.andago.question.exception.dao.PersonNotExistException;
import com.andago.question.exception.dao.QuestionNotExistException;

/**
 * Interface that contains all methods to access to the application
 * entities. Any DAO must implement the methods of these interface to 
 * be used in the application.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
public interface QuestionDAO {
	
	/**
	 * Method to get all persons entities that exists into the
	 * data model.
	 * 
	 * @return a collection with all persons registered into the
	 * application.
	 */
	public Collection<Person> getPersons();
	/**
	 * Method to get a person by a given id.
	 * 
	 * @param id person's identifier.
	 * @return a Person with the given identifier.
	 * @throws PersonNotExistException if a person doesn't 
	 * exist in the system with the given identifier.
	 */
	public Person getPersonById(Integer id) throws PersonNotExistException;
	/**
	 * Method to get a person by a given email.
	 * 
	 * @param email person's email.
	 * @return a Person with the given email.
	 * @throws PersonNotExistException if a person doesn't 
	 * exist in the system with the given email.
	 */
	public Person getPersonByEmail(String email) throws PersonNotExistException;
	/**
	 * Method to insert or update a person.
	 * 
	 * @param person the person object to save.
	 * @return the person object saved.
	 */
	public Person savePerson(Person person);
	/**
	 * Method to get all questions of the system.
	 * 
	 * @return a collection with the questions. 
	 */
	public Collection<Question> getQuestions();
	/**
	 * Method to check if a question exist into the system.
	 * 
	 * @param id the identifier of the question to check.
	 * @return true of false if the question exists or no.
	 */
	public boolean existsQuestionById(Integer id);
	/**
	 * Method to get all the proposals entities.
	 * 
	 * @return Collection with all the proposal entities.
	 */
	public Collection<Proposal> getProposals();
	/**
	 * Method to insert or update a proposal entity.
	 * @param p the proposal to save.
	 * @return the proposal object saved.
	 */
	public Proposal saveProposal(Proposal p);
	/**
	 *  Method to insert or update a question entity.
	 * @param q the question to save.
	 * @return the question object saved.
	 */
	public Question saveQuestion(Question q);
	/**
	 * Method to get a question by its identifier.
	 * @param id the question identifier to find.
	 * @return the question object.
	 * @throws QuestionNotExistException if doesn't exist a question
	 * with the given identifier.
	 */
	public Question getQuestionById(Integer id) throws QuestionNotExistException;
	/**
	 * Method to delete a proposal entity.
	 * @param p the proposal object to delete.
	 */
	public void deleteProposal(Proposal p);
	
}