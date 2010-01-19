package com.andago.webtvprovider.web.service.rest;

import org.jdom.Content;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public abstract class BaseResource {
	
	private final static String okStatus = "ok";
	private final static String errorStatus = "error";
	private final static String descriptionAttrName = "description";  
	
	public String buildOkResponse(String description) {
		return this.buildDescResponse(okStatus, description);
	}
	
	public String buildOkResponse(org.jdom.Document content) {
		String response = "";
		org.jdom.Document responseDoc = this.buildEmptyResponse(okStatus);
		Element contentRoot = content.getRootElement();
		contentRoot.detach();
		responseDoc.getRootElement().addContent((Element)contentRoot.clone());
		XMLOutputter outputter = new XMLOutputter();
		response = outputter.outputString(responseDoc);
		return response;
	}
	
	public String buildErrorResponse(String description) {
		return this.buildDescResponse(errorStatus, description);
	}
	
	private String buildDescResponse(String statusCode, String description) {
		String response = "";
		org.jdom.Document responseDoc = this.buildEmptyResponse(statusCode);
		responseDoc.getRootElement().setAttribute(descriptionAttrName, description);
		XMLOutputter outputter = new XMLOutputter();
		response = outputter.outputString(responseDoc);
		return response;
	}
	
	private org.jdom.Document buildEmptyResponse(String statusCode) {
		org.jdom.Document response = new org.jdom.Document();
		Element rootElement = new Element("response");
		rootElement.setAttribute("status", statusCode);
		response.setRootElement(rootElement);
		return response;
	}
}