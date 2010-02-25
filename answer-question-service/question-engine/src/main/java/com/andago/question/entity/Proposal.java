package com.andago.question.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * Class to represent the proposal entity. A proposal is a suggested
 * question for a person. A proposal have an answerDate if the person
 * have been answered the suggested question. 
 * 
 * @author eduardo.perrino@andago.com
 *
 */
/**
 * @author eduardo.perrino
 *
 */
@Entity
public class Proposal {
	
	private static final long serialVersionUID = -7342754079669431137L;
	/*Unique identifier for the proposal.*/
	private Integer id;
	/*The person who has received the question.*/
	private Person person;
	/*The question suggested.*/
	private Question question;
	/*Date when the question was suggested.*/
    @Temporal(TemporalType.TIMESTAMP)
	private Date broadcastDate;
    /*Date when the question was answered.*/
    @Temporal(TemporalType.TIMESTAMP)
    private Date answerDate;
	/*True of false value if the question was anwered or not.*/
    private boolean answered;
    /*The language of suggested question.*/
    private String language;
	
    /**
     * Getter method for id attribute.
     * @return id attribute.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
	public Integer getId() {
	    return id;
	}
	
    /**
     * Setter method for id attribute.
     * @param id value to be set into id attribute.
     */
	protected void setId(Integer id) {
	    this.id = id;
	}
	
	/**
	 * Getter method for person attribute.
	 * @return the person who has received the question suggested.
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	public Person getPerson() {
		return this.person;
	}
	
	
	/**
	 * Setter method for person attribute.
	 * 
	 * @param p person to be set.
	 */
	public void setPerson(Person p) {
		this.person = p;
	}
	
	/**
	 * Getter method for question attribute.
	 * 
	 * @return the question suggested in the proposal.
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	public Question getQuestion() {
		return this.question;
	}
	
	/**
	 * Setter method for question attribute.
	 * @param the question to be set.
	 */
	public void setQuestion(Question q) {
		this.question = q;
	}
	
	/**
	 * Getter method for the broadcastDate attribute.
	 * 
	 * @return broadcastDate.
	 */
	public Date getBroadcastDate() {
		return this.broadcastDate;
	}
	
	/**
	 * Setter method for broadcastDate attribute.
	 * 
	 * @param emision date to be set into broadcastDate
	 * attribute.
	 */
	public void setBroadcastDate(Date emision) {
		this.broadcastDate = emision;
	}
	
	/**
	 * Getter method for answerDate attribute.
	 * @return answerDate.
	 */
	public Date getAnswerDate() {
		return this.answerDate;
	}
	
	/**
	 * Setter method for answerDate attribute.
	 * @param emision date to be set into answerDate attribute.
	 */
	public void setAnswerDate(Date emision) {
		this.answerDate = emision;
	}
	
	/**
	 * Getter method for answered attribute.
	 * @return true or false if the suggested question has been
	 * answered or not.
	 */
	public boolean isAnswered() {
		return this.answered;
	}
	
	/**
	 * Setter method for answered attribute.
	 * @param a value to be set into answered attribute.
	 */
	public void setAnswered(boolean a) {
		this.answered = a;
	}
	
	/**
	 * Getter method for language attribute.
	 * @return suggested question language.
	 */
	public String getLanguage() {
		return this.language;
	}
	
	/**
	 * Setter method for language attribute.
	 * @param l language to be set.
	 */
	public void setLanguage(String l) {
		this.language = l;
	}
}