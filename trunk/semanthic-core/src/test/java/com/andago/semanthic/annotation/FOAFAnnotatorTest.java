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
import java.util.List;
import java.util.ArrayList;

@Ignore
public class FOAFAnnotatorTest {
	
	private static Logger log = Logger.getLogger(FOAFAnnotatorTest.class);
	private FOAFAnnotator foafAnnotator; 
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new 
	 		ClassPathXmlApplicationContext("/application_Context.xml");
		this.foafAnnotator = (FOAFAnnotator)context.getBean("personsAnnotator");
	}
	
	
	public void testAddPerson() throws Exception {
		String email = "eduardo.perrino@gmail.com";
		String name = "Eduardo";
		String surname = "Perrino";
		this.foafAnnotator.addPerson(email, name, surname);
		email = "alonso@hotmail.com";
		name = "Alberto";
		surname = "Perez";
		this.foafAnnotator.addPerson(email, name, surname);
		this.foafAnnotator.showModel(System.out);
	}
	
	
	public void testGetPerson() throws Exception {
		DynaBean bean = this.foafAnnotator.
			getPerson("eduardo.perrino@gmail.com");
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
		this.foafAnnotator.showModel(System.out);
	}
	
	@Test
	public void testAddTopic() {
		this.foafAnnotator.addTopic("london", "London");
		this.foafAnnotator.showModel(System.out);
	}
	
}