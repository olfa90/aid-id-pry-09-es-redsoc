package com.andago.restlayer.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import com.andago.restlayer.config.ApplicationConfigData;
import com.andago.question.core.QuestionManager;
import org.apache.commons.io.FileUtils;


public class AppInitializer implements ServletContextListener {

	private static Logger log = Logger.getLogger(AppInitializer.class);
	private final static String DEFAULT_QUESTIONS_FOLDER_NAME = "default_questions";
	
	public void contextDestroyed(ServletContextEvent servletContextEvt) {
		// TODO Auto-generated method stub
	}

	public void contextInitialized(ServletContextEvent event) {
		ApplicationConfigData.APP_BASE = 
			event.getServletContext().getRealPath(File.separator);
		ApplicationConfigData.CONFIG_FOLDER_PATH = 
			ApplicationConfigData.APP_BASE + 
			ApplicationConfigData.CONFIG_FOLDER_NAME;
		PropertyConfigurator.configure(ApplicationConfigData.CONFIG_FOLDER_PATH + 
				File.separator + "log4j.properties");
		ApplicationConfigData.DATA_FOLDER_PATH = ApplicationConfigData.APP_BASE + 
			ApplicationConfigData.DATA_FOLDER_NAME;
		log.debug("----Application Configuration Parameters");
		log.debug("APP_BASE: " + ApplicationConfigData.APP_BASE);
		log.debug("CONFIG_FOLDER: " + ApplicationConfigData.CONFIG_FOLDER_PATH);
		log.debug("DATA_FOLDER: " + ApplicationConfigData.DATA_FOLDER_PATH);
		log.debug("---------------------------------------------");
		String application_workfolder = 
			event.getServletContext().getInitParameter("application.workfolder");
		log.debug("--APPLICATION WORKFOLDER: " + 
				application_workfolder + " these value is defined in web.xml.");
		System.setProperty("application.workfolder", 
				application_workfolder);
		try {
			this.copyDefaultQuestions(application_workfolder);
		} catch(IOException ioEx) {
			log.error("Error copying default questions: " + 
					ioEx.getMessage());
		}
		
	}
	
	private void copyDefaultQuestions(String applicationWorkfolder) 
		throws IOException {
		
		String dest_defaultQuestionsFolderPath = applicationWorkfolder + 
			File.separator + QuestionManager.QUESTIONS_FOLDER_NAME;
		/**
		 * Code to create default questions folder.
		 */
		File dest_defaultQuestionsFolder = new File(dest_defaultQuestionsFolderPath);
		
		/**
		 * Code to copy default defined questions.
		 */
		String src_defaultQuestionsFolderPath = ApplicationConfigData.DATA_FOLDER_PATH + 
			File.separator + ApplicationConfigData.DEFAULT_QUESTIONS_FOLDER_NAME;
		
		File src_questions_folder = new File(src_defaultQuestionsFolderPath);
		
		FileUtils.copyDirectory(src_questions_folder, 
				dest_defaultQuestionsFolder);
	}

}