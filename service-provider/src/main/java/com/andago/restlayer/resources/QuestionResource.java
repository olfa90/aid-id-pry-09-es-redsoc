package com.andago.restlayer.resources;

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

import java.util.ArrayList;
import java.util.List;

@Path("/question")
@Component
public class QuestionResource extends BaseResource {

	private static Logger log = Logger.getLogger(QuestionResource.class);
	
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
			@FormParam("answers") String answers, 
			@FormParam("comment") String comment) throws Exception {
		List<String> parsedAnswers = new ArrayList<String>();
		String[] splittedAnswers = answers.split("[,]");
		for(String answer : splittedAnswers) {
			log.debug("Received answer: " + answer);
			parsedAnswers.add(answer);
		}
		String response = "";
		try {
			this.questionManager.answerQuestion(personEmail, 
					questionId, language, parsedAnswers, comment);
			response = this.buildOkResponse("Question " + questionId + 
					" have been answered succesfully for person " +
					personEmail);
		} catch(Exception e) {
			log.error("Error answering: " + e.getMessage());
			response = this.buildErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@GET
	@Path("test")
	@Produces("text/html")
    public String resourceTest() {
        // Return some cliched textual content
		log.debug("+++++++++++++++++QuestionManager: " + 
				this.questionManager);
        return this.constructConfigInfo();
    }
	
	
	private String constructConfigInfo() {
		String configHTML = "<html><head></head>";
		configHTML +="<body>";
		configHTML +="<table>";
		for(String field : this.questionManager.getConfig().keySet()) {
			configHTML +="<tr>";
			configHTML +="<td>" + field + "</td>";
			configHTML +="<td>" + this.questionManager.getConfig().get(field) + "</td>";
			configHTML +="</tr>";
		}
		configHTML +="</table>";
		configHTML +="</body>";
		configHTML +="</html>";
		return configHTML;
	}
	

}