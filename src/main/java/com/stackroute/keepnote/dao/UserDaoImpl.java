package com.stackroute.keepnote.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */

@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	public UserDaoImpl(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new user
	 */

	public boolean registerUser(User user) {
		Session session= sessionFactory.getCurrentSession();
		Boolean status = false;
		User user1 = getUserById(user.getUserId());
		if(user1==null) {
			if(session.save(user)!=null || session.save(user)!=" ") {
				status = true;
			}
		}
		return status;
	}

	/*
	 * Update an existing user
	 */

	public boolean updateUser(User user) {
		Session session= sessionFactory.getCurrentSession();
		
		try {
			session.update(user);
		} catch (Exception e) {
			
			return false;
		}
		return true;

	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) {
		Session session= sessionFactory.getCurrentSession();
	    User user =	session.get(User.class, UserId);
		return user;
	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		User user = getUserById(userId);
		if(user==null || !(user.getUserPassword().equalsIgnoreCase(password))) {
			throw new UserNotFoundException("user not found");
		}
		else {
			return true;
		}				

	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {
		Session session= sessionFactory.getCurrentSession();
		try {
			session.delete(getUserById(userId));
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}

	}

}
