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

import java.net.URI;
import java.util.List;
import java.util.ArrayList;

@Ignore
public class QuestionManagerTest {
	
	private static Logger log = Logger.getLogger(QuestionManagerTest.class);
	private QuestionManager questionManager = null;
	
	@Before
	public void setUp() {
		log.debug("Configuring Question-Dispatcher..");
		System.setProperty("application.workfolder", 
				"D:\\app_data\\userpreferences");
		ApplicationContext context = new 
	 	ClassPathXmlApplicationContext("/applicationContext.xml");
		questionManager = (QuestionManager)context.getBean("questionManager");
	}
	

	
	public void getQuestionTest() 
		throws Exception { 
		String question_content = 
			this.questionManager.getQuestion("eduardo.perrino@gmail.com", "eu"); 
		log.debug("Question-Content: " + question_content);
	}
	
	
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
	
	@Test
	public void testExtractTopicsFromQuestion() 
		throws Exception {
		Integer questionId = 2;
		String language = "es";
		List<String> answers = new ArrayList<String>();
		answers.add("1");
		List<String> topics = this.questionManager.getQuestionTopics(questionId, 
				language, answers);
		log.debug("Extracted topics...........");
		for(String topic : topics) {
			log.debug("Topic: " + topic.toString());
		}
	}
}