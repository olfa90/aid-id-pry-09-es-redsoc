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



public class FOAFAnnotatorTest {
	
	private static Logger log = Logger.getLogger(FOAFAnnotatorTest.class);
	private FOAFAnnotator foafAnnotator; 
	private final static String dbURL = 
		"jdbc:derby:D:\\app_data\\userpreferences\\semanthicdb;create=true";
	private final static String dbUser = "root";
	private final static String dbPw = "demonio";
	private final static String dbType = "Derby";
	private final static boolean dbClean = false;
	
	@Before
	public void setUp() throws Exception {
		this.foafAnnotator = new FOAFAnnotator(dbURL, 
				dbUser, dbPw, dbType, dbClean,
				"http://copernico.com");
	}
	
	
	@Test
	public void testAddPerson() throws Exception {
		String email = "jartyuhuu.perez@gmail.com";
		String name = "Yuhuuxxx";
		String surname = "";
		this.foafAnnotator.annotatePerson(email, name, surname);
		
	}
	
	
	@Test
	public void testGetPerson() throws Exception {
		DynaBean bean = this.foafAnnotator.
			getPerson("jartyuhuu.perez@gmail.com");
		if(bean==null) {
			return;
		}
		for(DynaProperty property : bean.getDynaClass().getDynaProperties()) {
			System.out.println(property.getName() + ": " + 
					bean.get(property.getName()));
		}
	}
	
	
	
	public void testAddTopicToPerson() throws Exception {
		String topic = "http://copernico.topics.com#london";
		this.foafAnnotator.addInterestTopicToPerson("jartyuhuu.perez@gmail.com", 
				topic);
	}
	
	
	
	public void testShowModel() {
		PrintWriter writer = new PrintWriter(System.out);
		this.foafAnnotator.showModel(writer);
	}
	
	
}