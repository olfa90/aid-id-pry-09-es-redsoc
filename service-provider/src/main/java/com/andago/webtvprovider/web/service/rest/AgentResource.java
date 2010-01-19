package com.andago.webtvprovider.web.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import org.apache.log4j.Logger;
import com.andago.question.core.QuestionManager;
import org.springframework.stereotype.Component;
import com.sun.jersey.spi.inject.Inject;
import java.util.List;

@Path("/question")
@Component
public class AgentResource extends BaseResource {

	private static Logger log = Logger.getLogger(AgentResource.class);
	
	@Inject("questionManager")
    private QuestionManager questionManager;


	@GET
    @Path("discover/{person_email}/{language}")
    @Produces({"application/xml"})
	public String getQuestion(@PathParam("person_email") 
			String personEmail, @PathParam("language") 
			String language) {
		String response = "";
		
		try {
			org.jdom.Document questionDoc = this.questionManager.
				getQuestionXML(personEmail, language);
			response = this.buildOkResponse(questionDoc);
		} catch(Exception e) {
			e.printStackTrace();
			response = this.buildErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@POST
    @Path("answer")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({"application/xml"})
	public String answerQuestion(@FormParam("question_id") Integer questionId, 
			@FormParam("person_email") String personEmail, 
			@FormParam("language") String language, 
			@FormParam("answers") List<String> answers, 
			@FormParam("comment") String comment) throws Exception {
		String response = "";
		try {
			this.questionManager.answerQuestion(personEmail, 
					questionId, language, answers, comment);
			response = this.buildOkResponse("Question " + questionId + 
					" have been answered succesfully for person " +
					personEmail);
		} catch(Exception e) {
			e.printStackTrace();
			response = this.buildErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@GET
	@Path("test")
	@Produces("text/plain")
    public String resourceTest() {
        // Return some cliched textual content
		log.debug("+++++++++++++++++QuestionManager: " + 
				this.questionManager);
        return "User preferences services is UP :-).";
    }
	

}