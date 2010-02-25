package com.andago.restlayer.resources;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.util.ApplicationDescriptor;

@Ignore
public class BaseResourceTest extends JerseyTest {

	public BaseResourceTest() throws Exception {
		ApplicationDescriptor appDescriptor = new ApplicationDescriptor();
		appDescriptor.setContextPath("/userpreferences/services");
		Map<String, String> contextParams = new HashMap<String, String>();
		contextParams.put("contextConfigLocation", "classpath:applicationContext.xml");
		System.setProperty("application.workfolder", "D:\\app_data\\userpreferences");
        appDescriptor.setContextParams(contextParams);
        appDescriptor.setRootResourcePackageName("com.andago.restlayer.resources");
        appDescriptor.setContextListenerClassName("org.springframework.web.context.ContextLoaderListener");
        appDescriptor.setServletClass(com.sun.jersey.spi.spring.container.servlet.SpringServlet.class);
        super.setupTestEnvironment(appDescriptor);
	}

}
