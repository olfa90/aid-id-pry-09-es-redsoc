package com.andago.restlayer.resources;

import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.andago.semanthic.annotation.person.ifc.IPersonAnnotable;
import com.sun.jersey.spi.inject.Inject;
import javax.ws.rs.FormParam;
import org.apache.commons.beanutils.DynaBean;
import net.sf.json.JSONObject;


@Path("/person")
@Component
public class PersonResource {
	
	private static Logger log = Logger.getLogger(PersonResource.class);
	
	@Inject("personsAnnotator")
    private IPersonAnnotable annotator;
	
	@PUT
	public void addPerson(@FormParam("email") String email,
			@FormParam("name") String name) {
		
	}
	
	
	@GET
	@Path("{person_email}")
	@Produces("application/json")
	public String getPerson(@PathParam("person_email")
			String personEmail) throws Exception {
		DynaBean person = this.annotator.getPerson(personEmail);
		JSONObject jsonObject = JSONObject.fromObject( person );
		log.debug("Email to find: " + personEmail);
		log.debug("++++Annotator: " + this.annotator);
		return jsonObject.toString();
	}
	
}