package com.andago.question.core;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import com.andago.question.dao.QuestionDAO;
import com.andago.question.entity.Person;
import com.andago.question.entity.Question;
import com.andago.question.entity.Proposal;
import com.andago.question.exception.dao.PersonNotExistException;
import com.andago.question.exception.dao.QuestionNotExistException;
import com.andago.question.exception.storage.StorageAccessException;
import com.andago.question.exception.storage.StorageCreationException;
import com.andago.question.storage.ifc.IAnswerStore;
import com.andago.question.exception.core.QuestionNotAvailableException;
import java.util.List;
import java.util.ArrayList;
import com.andago.question.exception.core.BadAnswerException;
/**
 * 
 * This class give the  methods to manage person's preferences questions 
 * and its answers.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
public class QuestionManager {
	
	/*The bean work folder.*/
	private String workFolder = "";
	/*Folder name to store questions.*/
	private final static String question_folder_name = "questions";
	/*Folder to store questions.*/
	private File question_storage;
	/*The logger.*/
	private static Logger log = Logger.getLogger(QuestionManager.class);
	/* Map to store questions of the system, keys
	 * are the identifiers of the questions and values are
	 * the modified data of question.*/
	private Map<Integer, Date> questions = new HashMap<Integer, Date>();
	/*DAO object.*/
	private QuestionDAO questionDAO;
	/*Answer store object.*/
	private IAnswerStore answerStore;
	
	/**
	 * Constructor of the class.
	 * 
	 * @param workFolder folder to store questions, answers and application data. 
	 * @param questionDAO an object that implements the interface QuestionDAO, 
	 * uses to store persons, questions, etc.
	 * @param answerStore  an object that implements the interface IAnswerStore,
	 * uses to store the person's answers.
	 */
	public QuestionManager(String workFolder, 
			QuestionDAO questionDAO, IAnswerStore answerStore) {
		this.workFolder = workFolder;
		this.questionDAO = questionDAO;
		this.answerStore = answerStore;
		this.question_storage = new File(this.workFolder + 
				File.separator + this.question_folder_name);
		if(this.question_storage.exists()) {
			this.loadQuestions();
		}
	}
	
	/**
	 * Method to load the folder that contains the questions to
	 * launch to the persons. The questions are loaded into a HashMap,
	 * these map is used to dispatch questions to the persons.
	 */
	private void loadQuestions() {
		Integer questionId;
		for(File file: this.question_storage.listFiles()) {
			if(!file.isDirectory()) {
				continue;
			}
			try {
				questionId = Integer.parseInt(file.getName());
			} catch(Exception e) {
				log.info("See these folder: " + 
						file.getAbsolutePath());
				continue;
			}
			this.questions.put(questionId, 
					new Date(file.lastModified()));
		}
	}
	
	/**
	 * Method to give a question to somebody.
	 * 
	 * @param personEmail the person's email who will be received 
	 * the question.
	 * @param language the user's language.
	 * @return a string in UTF-8 encoding, that contains the question in 
	 * xml format.
	 * @throws QuestionNotAvailableException  If doesn't exist any question available.
	 * @throws Exception any problem.
	 */
	public String getQuestion(String personEmail, 
			String language) throws 
			QuestionNotAvailableException, Exception {
		Document question = this.loadQuestionXML(personEmail, language);
	    XMLOutputter outputter = new XMLOutputter();
		String question_content = outputter.outputString(question);
	    return question_content;
	}
	
	/**
	 * Method to give a question to somebody.
	 * 
	 * @param personEmail the person's email who will be received 
	 * the question.
	 * @param language the user's language.
	 * @return a org.jdom.Document in UTF-8 encoding, that contains the 
	 * question in xml format. If doesn't exist any question null is the 
	 * returned value.
	 * @throws QuestionNotAvailableException  If doesn't exist any question available.
	 * @throws Exception any problem.
	 */
	public org.jdom.Document getQuestionXML(String personEmail, 
			String language) throws 
			QuestionNotAvailableException, Exception {
	    return this.loadQuestionXML(personEmail, language);
	}
	
	/**
	 * Method to store the answer to a question from any person.
	 * 
	 * @param personEmail the person's email.
	 * @param questionId the identifier of the answered question.
	 * @param language the user's language.
	 * @param answerCode the code of the selected answer.
	 * @param comment any comment for the answer that is done for 
	 * the user.
	 * @throws StorageCreationException error at the person's answer 
	 * store creation. 
	 * @throws StorageAccessException error accessing to the person's 
	 * answer store.
	 * @throws IOException error while processing question file.
	 * @throws JDOMException xml manage error.
	 * @throws PersonNotExistException when the person's email not exist 
	 * into the system.
	 * @throws Exception any error.
	 */
	public void answerQuestion(String personEmail, 
			Integer questionId, String language, 
			List<String> answers, String comment) throws 
			StorageCreationException, StorageAccessException, 
			IOException, JDOMException, PersonNotExistException,
			BadAnswerException, Exception {
		String questionFileName = questionId 
			+ "_" + language + ".xml";
		String questionPath =  this.question_storage.getAbsolutePath() + 
			File.separator + questionId + File.separator + questionFileName;
		Document answeredQuestion = null, originalQuestion = null;
		SAXBuilder sb = new SAXBuilder();
		originalQuestion = sb.build(new File(questionPath));
		this.checkAnswerValidity(originalQuestion, answers);
		answeredQuestion = this.buildAnsweredQuestion(originalQuestion, 
				answers, comment);
		this.answerStore.storeAnswer(personEmail, questionId, 
				language, answeredQuestion);
		Person person = this.questionDAO.getPersonByEmail(personEmail);
		for(Proposal proposal : person.getProposals()) {
			if(proposal.getQuestion().getId().equals(questionId)) {
				proposal.setAnswerDate(Calendar.getInstance().getTime());
				proposal.setAnswered(true);
				this.questionDAO.saveProposal(proposal);
				break;
			}
		}
	}
	
	private void checkAnswerValidity(org.jdom.Document question, 
			List<String> answers) throws BadAnswerException, Exception {
		Map<String, String> posibleAnswers = this.loadQuestionAnswers(question);
		if(!posibleAnswers.keySet().containsAll(answers)) {
			String ansStr = "";
			for(String answer : answers) {
				ansStr += "/" + answer;
			}
			throw new BadAnswerException("Answer is not correct (" + 
					ansStr + "). - (" + posibleAnswers.keySet() + ")");
		}
	}
	
	private Map<String, String> loadQuestionAnswers(org.jdom.Document question) {
		Map<String, String> answers = new HashMap<String, String>();
		Element proposal = question.getRootElement().getChild("proposal");
		List<Element> answerEls = proposal.getChildren("answer");
		log.debug("+++Proposal: " + proposal);
		String code="", text=""; 
		for(Element answerEl : answerEls) {
			code = answerEl.getChildText("code");
			text = answerEl.getChildText("text");
			log.debug("--Code: " + code);
			log.debug("--Text: " + text);
			answers.put(code, text);
		}
		return answers;
	}
	
	private org.jdom.Document loadQuestionXML(String personEmail, 
			String language) throws QuestionNotAvailableException, 
			Exception {
		Document question = null;
		Integer questionId = this.discoverQuestion(personEmail, language);
		if(questionId == null) {
			throw new QuestionNotAvailableException("Doesn't exist " +
					" any question available for user " + personEmail);
		}
		String questionPath =  this.question_storage.getAbsolutePath() + 
			File.separator + questionId + File.separator + questionId 
			+ "_" + language + ".xml";
	    log.debug("----QuestionPath: " + questionPath);
		SAXBuilder sb = new SAXBuilder();
	    question = sb.build(new File(questionPath));
	    this.addProposal(personEmail, language, questionId);
	    return question;
	}
	
	/**
	 * Method to build the xml-file to store the person's answer.
	 * @param originalQuestion the unique identifier of the answered question.
	 * @param answerCode the code of the selected answer.
	 * @param comment comment any comment for the answer that is done for 
	 * the user.
	 * @return the answered question in a jdom object.
	 */
	private org.jdom.Document buildAnsweredQuestion(org.jdom.Document 
			originalQuestion, List<String> answers, String comment) {
		Element selectedElement = new Element("selected");
		for(String answer : answers) {
			Element answerElement = new Element("answer");
			selectedElement.addContent(answerElement);
			Element codeElement = new Element("code");
			answerElement.addContent(codeElement);
			Element textElement = new Element("text");
			answerElement.addContent(textElement);
			codeElement.setText(answer);
			textElement.setText("Texto de prueba");	
		}
		Element commentElement = new Element("comment");
		selectedElement.addContent(commentElement);
		commentElement.setText(comment);
		originalQuestion.getRootElement().addContent(selectedElement);
		return originalQuestion;
	}
	
	/**
	 * Method to add a proposed question for a person.
	 * 
	 * @param personEmail personEmail the person's email.
	 * @param language the user's language.
	 * @param questionId the unique identifier of the answered question.
	 * @throws PersonNotExistException when the person's email not exist 
	 * into the system.
	 * @throws QuestionNotExistException when the proposed question doesn't
	 * exist into the system.
	 * @throws Exception if any error appears.
	 */
	private void addProposal(String personEmail, 
			String language, Integer questionId) 
			throws PersonNotExistException, QuestionNotExistException,
			Exception {
		Person person = this.questionDAO.getPersonByEmail(personEmail);
		Question question = this.storeQuestion(questionId);
		Proposal proposal = new Proposal();
		proposal.setAnswered(false);
		proposal.setBroadcastDate(Calendar.getInstance().getTime());
		proposal.setQuestion(question);
		proposal.setPerson(person);
		proposal.setLanguage(language);
		person.getProposals().add(proposal);
		this.questionDAO.savePerson(person);
	}
	
	/**
	 * Method to store a question into the data model. These method doesn't
	 * manage the xml documents of the questions.
	 * 
	 * @param questionId the unique identifier of the question.
	 * @return a Question object.
	 * @throws PersonNotExistException if the person's email not exist 
	 * into the system.
	 * @throws Exception if any error appears.
	 */
	private Question storeQuestion(Integer questionId) 
		throws PersonNotExistException, Exception {
		Question question = null;
		if(!this.questionDAO.existsQuestionById(questionId)) {
			question = new Question();
			question.setReleaseDate(Calendar.getInstance().getTime());
			question = this.questionDAO.saveQuestion(question);
		} else {
			question = this.questionDAO.getQuestionById(questionId);
		}
		return question;
	}
	
	/**
	 * Method that discover a valid question for a given person.
	 * 
	 * @param personEmail the person's email.
	 * @param language the person's language.
	 * @return the unique identifier of discovered question.
	 * @throws PersonNotExistException if the person doesn't exist.
	 */
	private Integer discoverQuestion(String personEmail, 
			String language) throws PersonNotExistException {
		Integer discoveredQuestionId = null;
		for(Integer questionId : this.questions.keySet()) {
			if(!this.isProposedQuestion(personEmail, 
					language, questionId)) {
				discoveredQuestionId = questionId;
				break;
			}
		}
		return discoveredQuestionId;
	}
	
	/**
	 * These method check if a question have been proposed for a 
	 * given person yet.
	 * 
	 * @param personEmail personEmail the person's email.
	 * @param language the user's language.
	 * @param questionId the unique identifier of the question.
	 * @return
	 * @throws PersonNotExistException
	 */
	private boolean isProposedQuestion(String personEmail, 
			String language, Integer questionId) 
			throws PersonNotExistException {
		Person person = null;
		try {
			person = this.questionDAO.getPersonByEmail(personEmail);
		} catch(PersonNotExistException pEX) {
			person = new Person();
			person.setEmail(personEmail);
			this.questionDAO.savePerson(person);
		}
		person = this.questionDAO.getPersonByEmail(personEmail);
		for(Proposal proposal : person.getProposals()) {
			if(proposal.getQuestion().getId().intValue() == 
				questionId.intValue()) {
				return true;
			}
		}
		return false;
	}

}