/**
 * 
 */
package com.turborep.turbotracker.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.domain.User;


/**
 * Handles CRUD services for users
 * 
 */
@Service("userService")
@Transactional
public class UserService implements IUserService   {
 	
	protected static Logger itsLogger = Logger.getLogger("service");
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	/**
	 * Retrieves all persons
	 * 
	 * @return a list of persons
	 */
	public List<User> getAll() {
		itsLogger.debug("Retrieving all");
		// Retrieve aSession from Hibernate
		Session aSession = itsSessionFactory.openSession();
		Query aQuery = null;
		try {
			// Create a Hibernate query (HQL)
			aQuery = aSession.createQuery("FROM  User");
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.close();
			aSession.flush();
		}
		// Retrieve all
		System.out.println("Connection: " + aSession.isOpen());
		return  aQuery.list();
	}
	
	/**
	 * Retrieves a single person
	 */
	public User get( String theId ) {
		// Retrieve aSession from Hibernate
		Session aSession = itsSessionFactory.getCurrentSession();
		User aPerson = null;
		try {
			// Retrieve existing person first
			aPerson = (User) aSession.get(User.class, theId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.close();
			aSession.flush();
		}
		System.out.println("Connection: " + aSession.isOpen());
		return aPerson;
	}
	/**
	 * Adds a new person
	 * @return 
	 */
	public Boolean add(User thePerson) {
		itsLogger.debug("Adding new person");
		// Retrieve aSession from Hibernate
		Session aSession = itsSessionFactory.getCurrentSession();
		try {
			// Save
			aSession.save(thePerson);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.close();
			aSession.flush();
		}
		System.out.println("Connection: " + aSession.isOpen());
		return true;
	}
	
	/**
	 * Deletes an existing person
	 * @param theId the id of the existing person
	 */
	public void delete(Integer theId) {
		itsLogger.debug("Deleting existing person");
		// Retrieve aSession from Hibernate
		Session aSession = itsSessionFactory.getCurrentSession();
		try {
			// Retrieve existing person first
			User aPerson = (User) aSession.get(User.class, theId);
			// Delete 
			aSession.delete(aPerson);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.close();
			aSession.flush();
		}
		System.out.println("Connection: " + aSession.isOpen());
	}
	
	public Boolean delete(User theUser) {
		Session aSession = itsSessionFactory.getCurrentSession();
		try {
			aSession.delete(theUser);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.close();
			aSession.flush();
		}
		System.out.println("Connection: " + aSession.isOpen());
		return true;
	}
	/**
	 * Edits an existing person
	 * @return 
	 */
	public Boolean edit(User theUerson) {
		itsLogger.debug("Editing existing person");
		// Retrieve aSession from Hibernate
		Session aSession = itsSessionFactory.getCurrentSession();
		try {
			// Retrieve existing person via id
			User aExistingPerson = (User) aSession.get(User.class, theUerson.getId());
			// Assign updated values to this person
			aExistingPerson.setFirstName(theUerson.getFirstName());
			aExistingPerson.setLastName(theUerson.getLastName());
			//existingPerson.setMoney(person.getMoney());
			// Save updates
			aSession.save(aExistingPerson);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.close();
			aSession.flush();
		}
		System.out.println("Connection: " + aSession.isOpen());
		return true;
	}
}
