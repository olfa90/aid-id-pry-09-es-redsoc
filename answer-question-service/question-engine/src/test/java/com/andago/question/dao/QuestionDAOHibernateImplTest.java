package com.andago.question.dao;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.runner.RunWith;
import java.util.Calendar;
import java.util.Collection;
import com.andago.question.dao.QuestionDAO;
import com.andago.question.entity.Person;
import com.andago.question.entity.Question;
import com.andago.question.entity.Proposal;
import com.andago.question.exception.dao.QuestionNotExistException;

@Ignore
public class QuestionDAOHibernateImplTest {
	
	private static Logger log = Logger.getLogger(QuestionDAOHibernateImplTest.class);
	private QuestionDAO questionDAO;
	
	@Before
	public void setUp() {
		 ApplicationContext context = new 
		 	ClassPathXmlApplicationContext("/spring-hibernate-jpa-config.xml");
		 questionDAO = (QuestionDAO)context.getBean("questionDAO");
	}
	
	
	public void savePersonTest() throws Exception {
		Person person = null;	
		try {
			person = questionDAO.getPersonByEmail("eduardo.perrino@gmail.com");
		} catch(Exception e) {
			person = new Person();
			person.setEmail("eduardo.perrino@gmail.com");
			questionDAO.savePerson(person);
		}
		person = questionDAO.getPersonByEmail("eduardo.perrino@gmail.com");
		log.debug("----" + person.getEmail());
	}
	
	
	public void getPersonById() throws Exception {
		Person person = questionDAO.getPersonByEmail("eduardo.perrino@gmail.com");
		log.debug("--Person:ID " + person.getId());
		log.debug("Person:Email: " + person.getEmail());
	}
	
	
	@Test
	public void getAllPersonsTest() {
		Collection<Person> persons = questionDAO.getPersons();
		log.debug("Number of persons is " + persons.size());
		for(Person person : persons) {
			log.debug("Person:ID " + person.getId());
			log.debug("Person:Email " + person.getEmail());
			log.debug("Person:Proposals: " + person.getProposals());
		}
	}
	
	
	public void getAllQuestionsTest() {
		Collection<Question> questions = questionDAO.getQuestions();
		for(Question question : questions) {
			log.debug("Question:ID " + question.getId());
			log.debug("Question:ReleaseDate " + question.getReleaseDate());
		}
	}
	
	
	public void exitsQuestionByIdTest() 
		throws QuestionNotExistException {
		Integer questionId = 10;		
		log.debug("Question:ID " + questionId + 
				" exists: "  + questionDAO.existsQuestionById(questionId));
	}
	
	
	
	public void getQuestionByIdTest() 
		throws QuestionNotExistException {
		Integer questionId = 2;
		Question question = questionDAO.getQuestionById(questionId);
		log.debug("Question:ID " + question.getId());
	}
	
	
	public void saveQuestionTest() {
		Question question = new Question();
		question.setReleaseDate(Calendar.getInstance().getTime());
		questionDAO.saveQuestion(question);
	}
	
	
	public void getProposalsTest() {
		Collection<Proposal> proposals = this.questionDAO.getProposals();
		for(Proposal proposal : proposals) {
			log.debug("Proposal:ID " + proposal.getId());
			log.debug("Proposal:Language " + proposal.getLanguage());
			log.debug("Receiver " + proposal.getPerson().getEmail());
			log.debug("Question:ID " + proposal.getQuestion().getId());
		}
	}
	
}