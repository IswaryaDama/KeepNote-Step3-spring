package com.stackroute.keepnote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the userDAO. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword.
	 */
     @Autowired
     private UserDAO userDao;
	/*
	 * This method should be used to save a new user.
	 */

	public boolean registerUser(User user) throws UserAlreadyExistException {
		
		if(!userDao.registerUser(user)) {
			throw new UserAlreadyExistException("User already exists");
		}
		else {
			return true;
		}
		
	}

	/*
	 * This method should be used to update a existing user.
	 */

	public User updateUser(User user, String userId) throws Exception {
		
		if(getUserById(userId)==null) {
			
			throw new Exception("exception occured");						
		}
		if(userDao.updateUser(user)) {
			return user;
		}else {
			return null;
		}
		

	}

	/*
	 * This method should be used to get a user by userId.
	 */

	public User getUserById(String UserId) throws UserNotFoundException {
	    User user1=userDao.getUserById(UserId);
		if(user1==null) {
			throw new UserNotFoundException("user not found");
		}
		else {
			return user1;
		}
		

	}

	/*
	 * This method should be used to validate a user using userId and password.
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		if(userDao.validateUser(userId, password)) {
			return true;
		}
		else {
			throw new UserNotFoundException("user not found") ;
		}

	}

	/* This method should be used to delete an existing user. */
	public boolean deleteUser(String UserId) {
		
			return	userDao.deleteUser(UserId);
		
	}

}
