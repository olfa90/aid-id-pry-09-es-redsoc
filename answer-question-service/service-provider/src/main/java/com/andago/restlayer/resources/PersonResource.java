package com.andago.restlayer.resources;

import javax.ws.rs.Consumes;
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
import com.andago.restlayer.resources.BaseResource;

@Path("/person")
@Component
public class PersonResource extends BaseResource {
	
	private static Logger log = Logger.getLogger(PersonResource.class);
	
	@Inject("personsAnnotator")
    private IPersonAnnotable annotator;
	
	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces({"application/xml"})
	public String annotatePerson(@FormParam("email") String email,
			@FormParam("name") String name, 
			@FormParam("familyname") String familyName) {
		String response = "";
		try {
			this.annotator.annotatePerson(email, name, familyName);
			response = this.buildOkResponse("Person " + email + 
					" have been annotated succesfully.");
		} catch(Exception e) {
			log.error("Error in annotation process: " + 
					e.getMessage());
			response = this.buildErrorResponse(e.getMessage().toString());
		}
		return response;
	}
	
	
	@GET
	@Path("{email}")
	@Produces("application/json")
	public String getPerson(@PathParam("email")
			String personEmail) {
		try {
			DynaBean person = this.annotator.getPerson(personEmail);
			JSONObject jsonObject = JSONObject.fromObject( person );
			log.debug("Email to find: " + personEmail);
			log.debug("++++Annotator: " + this.annotator);
			return jsonObject.toString();
		} catch(Exception e) {
			log.error("Error getting person FOAF info: " + 
					e.getMessage());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", e.getLocalizedMessage());
			return jsonObject.toString();
		}
	}
	
	
}