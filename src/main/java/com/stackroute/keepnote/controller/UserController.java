package com.stackroute.keepnote.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	
	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/user/register")
	public ResponseEntity<?> registerUser(@RequestBody User user){
		
		try {
			//SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
		user.setUserAddedDate(date);	
		Boolean status = userService.registerUser(user);
		if(status) {
			return new ResponseEntity<User>(HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
		} catch (UserAlreadyExistException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("UserAlreadyExistException",HttpStatus.CONFLICT);
		}
		
	}
	
	@PutMapping("/user/{id}")
	public ResponseEntity<?>updateUser(@RequestBody User user,@PathVariable("id") String userId,HttpSession session){
		
		try {
			if(session.getAttribute("loggedInUserId")!=null) {
				user.setUserAddedDate(new Date());
				if(userService.updateUser(user, userId)!=null) {
					return new ResponseEntity<User>(HttpStatus.OK);
				}else {
					return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
				}
			}
			else {
				return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("Exception occured",HttpStatus.CONFLICT);
		}
		
	}
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?>deleteUser(@PathVariable("id") String userId,HttpSession session ){
		
		try {
			if(session.getAttribute("loggedInUserId")!=null) {
				if(userService.deleteUser(userId)) {
					return new ResponseEntity<User>(HttpStatus.OK);
				}else {
					return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
				}
			}
			else {
				return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("Exception occured",HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?>getUserById(@PathVariable("id") String userId,HttpSession session){
		try {
			if(session.getAttribute("loggedInUserId")!=null) {
				if(userService.getUserById(userId)!=null) {
					return new ResponseEntity<User>(HttpStatus.OK);	
				}	else {
					return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
				}
			}else {
				return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
			}
			
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("Exception occured",HttpStatus.CONFLICT);
		}
		
	}
	

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in a User table
	 * in the database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 201(CREATED) - If the user created
	 * successfully. 2. 409(CONFLICT) - If the userId conflicts with any existing
	 * user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login.
	 * 
	 * This handler method should map to the URL "/user/register" using HTTP POST
	 * method
	 */

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * user table in database handle exception as well. This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 200(OK) - If the user updated successfully. 2. 404(NOT FOUND) - If the user
	 * with specified userId is not found. 3. 401(UNAUTHORIZED) - If the user trying
	 * to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP PUT method.
	 */

	/*
	 * Define a handler method which will delete a user from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */

	/*
	 * Define a handler method which will show details of a specific user handle
	 * UserNotFoundException as well. This handler method should return any one of
	 * the status messages basis on different situations: 1. 200(OK) - If the user
	 * found successfully. 2. 401(UNAUTHORIZED) - If the user trying to perform the
	 * action has not logged in. 3. 404(NOT FOUND) - If the user with specified
	 * userId is not found. This handler method should map to the URL "/user/{id}"
	 * using HTTP GET method where "id" should be replaced by a valid userId without
	 * {}
	 */

}