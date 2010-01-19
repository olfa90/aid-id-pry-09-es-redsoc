package com.andago.question.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;

/**
 * Class that represents the person entity.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
/**
 * @author eduardo.perrino
 *
 */
@Entity
@Table(name = "person")
@NamedQuery(name="person.emailLike", query="select p from Person p where p.email like ?")
public class Person implements Serializable {
	
	private static final long serialVersionUID = -7342754079669431138L;
	private Integer id;
	private String email;
	private List<Proposal> proposals;
	
	
    /**
     * Getter method for the id attribute.
     * @return the person id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
	public Integer getId() {
	    return id;
	}
	
    
	/**
	 * Setter method for the id attribute.
	 * @param id identifier to set.
	 */
	protected void setId(Integer id) {
	    this.id = id;
	}
	
	
	/**
	 * Getter method for email attributte.
	 * @return the person email.
	 */
	public String getEmail() {
		return this.email;
	}
	
	
	/**
	 * Setter method to email attribute.
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	/**
	 * Getter method to access to the proposals related
	 * with the Person.
	 * 
	 * @return a list with the questions proposed to the person.
	 */
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public List<Proposal> getProposals() {
		return this.proposals;
	}
	
	/**
	 * Setter method for proposals list.
	 * @param ps proposals list to be set.
	 */
	public void setProposals(List<Proposal> ps) {
		this.proposals = ps;
	}
}