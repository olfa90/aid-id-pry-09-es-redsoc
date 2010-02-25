package com.andago.question.exception.dao;

public class QuestionNotExistException extends Exception {

	private static final long serialVersionUID = 1091L;
	
	public QuestionNotExistException(String string) {
		super(string);
	}
	
}