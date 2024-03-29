package com.andago.restlayer.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import com.andago.question.core.QuestionManager;
import com.andago.question.exception.core.BadAnswerException;
import com.andago.question.exception.dao.PersonNotExistException;
import com.andago.question.exception.storage.StorageAccessException;
import com.andago.question.exception.storage.StorageCreationException;
import org.springframework.stereotype.Component;
import com.sun.jersey.spi.inject.Inject;
import com.andago.restlayer.resources.BaseResource;
import com.andago.semanthic.annotation.person.ifc.IPersonAnnotable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/question")
@Component
public class QuestionResource extends BaseResource {

	private static Logger log = Logger.getLogger(QuestionResource.class);
	
	@Inject("questionManager")
    private QuestionManager questionManager;
	
	@Inject("personsAnnotator")
    private IPersonAnnotable annotator;

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
			this.annotator.annotatePerson(personEmail, 
					"testing-name", "testing-familyName");
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
			this.doAnswerAction(questionId, personEmail, 
					language, parsedAnswers, comment);
			response = this.buildOkResponse("Question " + questionId + 
					" have been answered succesfully for person " +
					personEmail);
		} catch(Exception e) {
			log.error("Error answering: " + e.getMessage());
			response = this.buildErrorResponse(e.getMessage().toString());
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
	
	
	
	private void doAnswerAction(Integer questionId, 
			String personEmail, String language,
			List<String> answers, String comment) 
			throws StorageCreationException, 
				StorageAccessException, IOException, 
				JDOMException, PersonNotExistException, 
				BadAnswerException, Exception {
		/**TO-DO En este apartado se deberia implementar 
		 * transaccionalidad, de tal forma que si cualquier
		 * operacion fallara, se deberia hacer un rollback.*/
		this.questionManager.answerQuestion(personEmail, 
				questionId, language, answers, comment);
		/*Recuperar los topic resultantes de contestar la pregunta*/
		List<String> topics = this.questionManager.getQuestionTopics(questionId, 
				language, answers);
		/*Anadir los topics al registro FOAF de la persona.*/
		for(String topic : topics) {
			this.annotator.addInterestTopicToPerson(personEmail, 
					topic);
		}
	}
	
	private String constructConfigInfo() {
		String configHTML = "<html><head></head>";
		configHTML +="<body>";
		configHTML +="<table>";
		for(String field : this.questionManager.getConfig().keySet()) {
			configHTML += "<tr>";
			configHTML += "<td>" + field + "</td>";
			configHTML += "<td>" + 
				this.questionManager.getConfig().get(field) + 
				"</td>";
			configHTML += "</tr>";
		}
		configHTML += "</table>";
		configHTML += "</body>";
		configHTML += "</html>";
		return configHTML;
	}
	

}