package com.andago.webtvprovider.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import com.andago.restlayer.config.ApplicationConfigData;

public class AppInitializer implements ServletContextListener {

	private static Logger log = Logger.getLogger(AppInitializer.class);

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
	}


}
