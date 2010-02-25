package com.andago.restlayer.resources;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.util.ApplicationDescriptor;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.header.MediaTypes;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;


@Ignore
public class QuestionResourceTest extends BaseResourceTest {
	
	/*The logger.*/
	private static Logger log = Logger.getLogger(QuestionResourceTest.class);
	
	public QuestionResourceTest() throws Exception {
		super();
	}
	
	/**
     * Test that the expected response is sent back.
     * @throws java.lang.Exception
     */
	
    public void testAnswerQuestion() throws Exception {
    	Object gt;
    	Form formData = new Form();
    	formData.add("question_id", "1");
    	formData.add("person_email", "eduardo.perrino@gmail.com");
    	formData.add("language", "eu");
    	String answers = "1,4";
    	formData.add("answers", answers);
    	formData.add("comment", "Comentario de prueba");
    	ClientResponse response = webResource.path("question/answer").
    		type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
    	log.debug(response.getEntity(String.class));
    }
    
    @Test
	public void testGetQuestion() throws Exception {
		String responseMsg = webResource.path("question/discover/leox@gmail.com/eu").get(String.class);
		log.debug(responseMsg);
	}
    
	
    public void testApplicationWadl() throws MalformedURLException {
		String serviceWadl = webResource.path("application.wadl").
                accept(MediaTypes.WADL).get(String.class);
        System.out.println("Wadl: " + serviceWadl);
        //Assert.assertTrue(serviceWadl.length() > 0);
    }    

}
