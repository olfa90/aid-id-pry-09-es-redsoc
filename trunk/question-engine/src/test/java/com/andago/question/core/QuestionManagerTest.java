package com.andago.question.core;

import org.junit.Before;
import org.junit.Test;
import org.apache.log4j.Logger;
import org.jdom.output.XMLOutputter;
import org.junit.Ignore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.andago.question.dao.QuestionDAO;
import com.andago.question.storage.impl.FileSystemAnswerStore;
import java.util.List;
import java.util.ArrayList;

@Ignore
public class QuestionManagerTest {
	
	private static Logger log = Logger.getLogger(QuestionManagerTest.class);
	private QuestionManager questionManager = null;
	
	@Before
	public void setUp() {
		log.debug("Configuring Question-Dispatcher..");
		ApplicationContext context = new 
	 	ClassPathXmlApplicationContext("/application_Context.xml");
		questionManager = (QuestionManager)context.getBean("questionManager");
	}
	

	
	public void getQuestionTest() 
		throws Exception { 
		String question_content = 
			this.questionManager.getQuestion("eduardo.perrino@gmail.com", "eu"); 
		log.debug("Question-Content: " + question_content);
	}
	
	@Test
	public void answerQuestion() throws Exception {
		String personEmail = "eduardo.perrino@gmail.com";
		Integer questionId = 1;
		String language = "es";
		String comment = "Pero solo en verano";
		List<String> answers = new ArrayList<String>();
		answers.add("1");
		answers.add("4");
		this.questionManager.answerQuestion(personEmail, questionId, 
				language, answers, comment);
	}
}