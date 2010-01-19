package com.andago.question.dao;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.andago.question.entity.Person;
import com.andago.question.entity.Question;
import com.andago.question.entity.Proposal;
import com.andago.question.exception.dao.PersonNotExistException;
import com.andago.question.exception.dao.QuestionNotExistException;
import java.util.Collection;

/**
 * These class implements the QuestionDAO interface using hibernate
 * persistence framework.
 * 
 * @author eduardo.perrino@andago.com
 *
 */
public class QuestionDAOHibernateImpl extends HibernateDaoSupport 
	implements QuestionDAO {
	
	/*The logger.*/
	private static Logger log = Logger.getLogger(QuestionDAOHibernateImpl.class);
	
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#getPersons()
	 */
	public Collection<Person> getPersons() {
		Collection<Person> persons = getHibernateTemplate().loadAll(Person.class);
		log.debug("Persons size: " + persons.size());
		return persons;
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#getPersonById(java.lang.Integer)
	 */
	public Person getPersonById(Integer id) 
		throws PersonNotExistException {
		Person person = (Person)this.getHibernateTemplate().
			load(Person.class, id);
		if(person == null) {
			throw new PersonNotExistException("Person with id " +
	    			id + " doesn't exist.");
		}
		return person;
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#getPersonByEmail(java.lang.String)
	 */
	public Person getPersonByEmail(String email) 
		throws PersonNotExistException {
	    Collection<Person> persons = this.getHibernateTemplate().
	    	findByNamedQuery("person.emailLike", "%"+email+"%");
	    log.debug("Persons size: " + persons.size());
	    if(persons.size()==0) {
	    	throw new PersonNotExistException("Person with email " +
	    			email + " doesn't exist.");
	    }
		return persons.iterator().next();
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#getQuestions()
	 */
	public Collection<Question> getQuestions() {
		return this.getHibernateTemplate().loadAll(Question.class);
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#existsQuestionById(java.lang.Integer)
	 */
	public boolean existsQuestionById(Integer id) {
		Collection<Question> questions = this.getHibernateTemplate().
    		findByNamedQuery("question.id", id);
		if(questions.size()==0) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#getQuestionById(java.lang.Integer)
	 */
	public Question getQuestionById(Integer id) 
		throws QuestionNotExistException {
		Collection<Question> questions = this.getHibernateTemplate().
			findByNamedQuery("question.id", id);
		if(questions.size()==0) {
			throw new QuestionNotExistException("Question with id "
					 + id + " doesn't exist.");
		}
		return questions.iterator().next();
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#getProposals()
	 */
	public Collection<Proposal> getProposals() {
		return this.getHibernateTemplate().loadAll(Proposal.class);
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#deleteProposal(com.andago.question.entity.Proposal)
	 */
	public void deleteProposal(Proposal p) {
		this.getHibernateTemplate().delete(p);
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#saveProposal(com.andago.question.entity.Proposal)
	 */
	public Proposal saveProposal(Proposal p) {
		this.getHibernateTemplate().saveOrUpdate(p);
		return p;
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#saveQuestion(com.andago.question.entity.Question)
	 */
	public Question saveQuestion(Question q) {
		this.getHibernateTemplate().saveOrUpdate(q);
		return q;
	}
	
	/* (non-Javadoc)
	 * @see com.andago.question.dao.QuestionDAO#savePerson(com.andago.question.entity.Person)
	 */
	public Person savePerson(Person person) {
		this.getHibernateTemplate().saveOrUpdate(person);
	    return person;
	}
	
}