package com.andago.semanthic.annotation;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import com.andago.semanthic.annotation.person.impl.FOAFAnnotator;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;


@Ignore
public class FOAFAnnotatorTest {
	
	private static Logger log = Logger.getLogger(FOAFAnnotatorTest.class);
	private FOAFAnnotator foafAnnotator; 
	private final static String dbURL = 
		"jdbc:derby:D:\\workspace\\semanthic-app\\data_store\\semanthicdb;create=true";
	private final static String dbUser = "root";
	private final static String dbPw = "demonio";
	private final static String dbType = "Derby";
	private final static boolean dbClean = false;
	
	@Before
	public void setUp() throws Exception {
		//ApplicationContext context = new 
	 	//	ClassPathXmlApplicationContext("/application_Context.xml");
		//this.foafAnnotator = (FOAFAnnotator)context.getBean("personsAnnotator");
		//this.foafAnnotator = new FOAFAnnotator("D:\\ontologies\\questions_users.owl");
		String inputFilename = "D:\\ontologies\\questions_users.owl";
		this.foafAnnotator = new FOAFAnnotator(dbURL, dbUser, dbPw, dbType, 
				dbClean,inputFilename);
	}
	
	
	
	public void testAddPerson() throws Exception {
		String email = "alfonso.perez@gmail.com";
		String name = "Alfonso";
		String surname = "Perrino";
		this.foafAnnotator.addPerson(email, name, surname);
		email = "alonso@hotmail.com";
		name = "Alberto";
		surname = "Perez";
		this.foafAnnotator.addPerson(email, name, surname);
		FileOutputStream fos = new FileOutputStream("D:\\ontologies\\questions_users.owl");
	}
	
	
	@Test
	public void testGetPerson() throws Exception {
		DynaBean bean = this.foafAnnotator.
			getPerson("alonso@hotmail.com");
		if(bean==null) {
			return;
		}
		for(DynaProperty property : bean.getDynaClass().getDynaProperties()) {
			System.out.println(property.getName() + ": " + 
					bean.get(property.getName()));
		}
	}
	
	
	public void testAddTopicsToPerson() throws Exception {
		List<String> topics = new ArrayList<String>();
		topics.add("London");
		topics.add("Miami");
		this.foafAnnotator.addTopicsToPerson("eduardo.perrino@gmail.com", topics);
	}
	
	
	public void testAddTopic() {
		this.foafAnnotator.addTopic("london", "London");
	}
	
}