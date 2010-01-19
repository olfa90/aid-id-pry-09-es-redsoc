package com.andago.webtvprovider.web.service.rest;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Ignore;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.util.ApplicationDescriptor;


public class PersonResourceTest extends JerseyTest {
	
	/*The logger.*/
	private static Logger log = Logger.getLogger(AgentResourceTest.class);
	
	public PersonResourceTest() throws Exception {
		super();
		ApplicationDescriptor appDescriptor = new ApplicationDescriptor();
		appDescriptor.setContextPath("/userpreferences/services");
        appDescriptor.setRootResourcePackageName("com.andago.webtvprovider.web.service.rest");
        appDescriptor.setContextListenerClassName("com.andago.webtvprovider.web.listener.AppInitializer");
        appDescriptor.setContextListenerClassName("org.springframework.web.context.ContextLoaderListener");
        Map<String, String> contextParams = new HashMap<String, String>();
		contextParams.put("contextConfigLocation", "classpath:applicationContext.xml");
        appDescriptor.setContextParams(contextParams);
        appDescriptor.setServletClass(com.sun.jersey.spi.spring.container.servlet.SpringServlet.class);
        super.setupTestEnvironment(appDescriptor);
	}
	
	@Test
	public void testGetPerson() {
		String responseMsg = webResource.
			path("person/eduardo.perrino@gmail.com").get(String.class);
		log.debug("Response: " + responseMsg);
	}
}