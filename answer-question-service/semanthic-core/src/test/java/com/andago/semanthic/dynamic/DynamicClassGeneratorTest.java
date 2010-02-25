package com.andago.semanthic.dynamic;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;

@Ignore
public class DynamicClassGeneratorTest {
	
	private static Logger log = Logger.getLogger(DynamicClassGeneratorTest.class);
	private DynamicClassGenerator classGenerator;
	
	@Before
	public void setUp() {
		this.classGenerator = new DynamicClassGenerator();
	}
	
	@Test
	public void testCreateDynamicClass() throws Exception {
		Map<String,Class> properties = new HashMap<String,Class>();
		properties.put("email", String.class);
		properties.put("name", String.class);
		properties.put("surname", String.class);
		BasicDynaClass clase = 
			this.classGenerator.createDynamicClass("Person", properties);
		log.debug("Class name:" + clase.getName());
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("email", "pepe@hotmail.com");
		values.put("name", "Pepe");
		values.put("surname", "Gotera");
		DynaBean bean = this.classGenerator.instanceBean("Person", values);
		log.debug("Email: " + bean.get("email"));
	}
}