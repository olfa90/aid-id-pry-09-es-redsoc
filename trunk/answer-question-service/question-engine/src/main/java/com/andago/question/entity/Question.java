package com.andago.question.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that represents a question to be suggested to any person.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
@Entity
@NamedQuery(name="question.id", query="select q from Question q where q.id = ?")
public class Question implements Serializable {
	
	private static final long serialVersionUID = -7342754079669431139L;
	/*Unique identifier of the question*/
	private Integer id;
	/*Date when the question was inserted in application.*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date releaseDate;
	
	/**
	 * Getter method for id attribute.
	 * @return id of question.
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
	public Integer getId() {
	    return id;
	}
	
	/**
	 * Setter method for id attribute.
	 * @param id value to be set.
	 */
	public void setId(Integer id) {
	    this.id = id;
	}
	
	/**
	 * Getter method for releaseDate attribute.
	 * @return releaseDate.
	 */
	public Date getReleaseDate() {
		return this.releaseDate;
	}
	
	/**
	 * Setter method for releaseDate attribute.
	 * 
	 * @param rD value to be set.
	 */
	public void setReleaseDate(Date rD) {
		this.releaseDate = rD;
	}
}