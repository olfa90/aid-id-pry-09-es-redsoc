package com.andago.restlayer.resources;


import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Ignore;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;

@Ignore
public class PersonResourceTest extends BaseResourceTest {
	
	/*The logger.*/
	private static Logger log = Logger.getLogger(PersonResourceTest.class);
	
	public PersonResourceTest() throws Exception {
	}
	
	
	
	@Test
	public void testAnnotatePerson() {
		Form formData = new Form();
    	formData.add("email", "julandres@gmail.com");
    	formData.add("name", "jepos");
    	formData.add("familyname", "tostoxxxx");
    	ClientResponse response = webResource.path("person").
			type("application/x-www-form-urlencoded").put(ClientResponse.class, 
			formData);
	}
	
	@Test
	public void testGetPerson() {
		String responseMsg = webResource.
			path("person/julandres@gmail.com").get(String.class);
		log.debug("Response: " + responseMsg);
	}
}