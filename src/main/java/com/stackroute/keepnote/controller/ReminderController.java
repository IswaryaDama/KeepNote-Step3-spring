package com.stackroute.keepnote.controller;

import java.util.Date;
import java.util.List;

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

import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.ReminderService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class ReminderController {

	/*
	 * From the problem statement, we can understand that the application requires
	 * us to implement five functionalities regarding reminder. They are as
	 * following:
	 * 
	 * 1. Create a reminder 2. Delete a reminder 3. Update a reminder 2. Get all
	 * reminders by userId 3. Get a specific reminder by id.
	 * 
	 * we must also ensure that only a user who is logged in should be able to
	 * perform the functionalities mentioned above.
	 * 
	 */

	/*
	 * Autowiring should be implemented for the ReminderService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
   @Autowired
   private ReminderService reminderService;
	public ReminderController(ReminderService reminderService) {
     this.reminderService=reminderService;
	}

	@PostMapping("/reminder")
	public ResponseEntity<?> createReminder(@RequestBody Reminder reminder,HttpSession session){
		String createdByUser=(String) session.getAttribute("loggedInUserId");
		if(session!=null && session.getAttribute("loggedInUserId")!=null) {
			reminder.setReminderCreatedBy(createdByUser);
			reminder.setReminderCreationDate(new Date());
			if(reminderService.createReminder(reminder)) {
				return new ResponseEntity<String>("created",HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<String>("Duplicate Id",HttpStatus.CONFLICT);
			}
		}
		else {
			return new ResponseEntity<String>("Unauthorised",HttpStatus.UNAUTHORIZED);
		}
	}
	
	@DeleteMapping("/reminder/{id}")
	public ResponseEntity<?> deleteReminder(@PathVariable("id") int reminderId,HttpSession session ){
		
		try {
			if(session.getAttribute("loggedInUserId")!=null) {
				if(reminderService.deleteReminder(reminderId)) {
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
	
	@PutMapping("/reminder/{id}")
	public ResponseEntity<?> updateReminder(@RequestBody Reminder reminder,@PathVariable("id") int reminderId,HttpSession session){
		try {
			if(session.getAttribute("loggedInUserId")!=null) {
				if(reminderService.updateReminder(reminder, reminderId)!=null) {
					return new ResponseEntity<User>(HttpStatus.OK);
				}else {
					return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
				}
			}
			else {
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("Exception occured",HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/reminder")
	public ResponseEntity<?> getAllRemindersById(HttpSession session){
		
		String userId =	(String) session.getAttribute("loggedInUserId");
		List<Reminder> reminders = reminderService.getAllReminderByUserId(userId);
		if(session.getAttribute("loggedInUserId")==null ||reminders ==null) {
				
					return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
				
				
			}
			else {
				return new ResponseEntity<List<Reminder>>(reminders,HttpStatus.OK);
			}
		
	}
	
	@GetMapping("/reminder/{id}")
	public ResponseEntity<?>getReminderById(@PathVariable("id") int reminderId,HttpSession session){
		
		try {
			if(session.getAttribute("loggedInUserId")!=null) {
				if(reminderService.getReminderById(reminderId)!=null) {
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
	
	/*
	 * Define a handler method which will create a reminder by reading the
	 * Serialized reminder object from request body and save the reminder in
	 * reminder table in database. Please note that the reminderId has to be unique
	 * and the loggedIn userID should be taken as the reminderCreatedBy for the
	 * reminder. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED - In case of successful
	 * creation of the reminder 2. 409(CONFLICT) - In case of duplicate reminder ID
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/reminder" using HTTP POST
	 * method".
	 */

	/*
	 * Define a handler method which will delete a reminder from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the reminder deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the reminder with specified reminderId is
	 * not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/reminder/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid reminderId without {}
	 */

	/*
	 * Define a handler method which will update a specific reminder by reading the
	 * Serialized object from request body and save the updated reminder details in
	 * a reminder table in database handle ReminderNotFoundException as well. please
	 * note that the loggedIn userID should be taken as the reminderCreatedBy for
	 * the reminder. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the reminder updated
	 * successfully. 2. 404(NOT FOUND) - If the reminder with specified reminderId
	 * is not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/reminder/{id}" using HTTP PUT
	 * method.
	 */

	/*
	 * Define a handler method which will get us the reminders by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the reminder found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/reminder" using HTTP GET method
	 */

	/*
	 * Define a handler method which will show details of a specific reminder handle
	 * ReminderNotFoundException as well. This handler method should return any one
	 * of the status messages basis on different situations: 1. 200(OK) - If the
	 * reminder found successfully. 2. 401(UNAUTHORIZED) - If the user trying to
	 * perform the action has not logged in. 3. 404(NOT FOUND) - If the reminder
	 * with specified reminderId is not found. This handler method should map to the
	 * URL "/reminder/{id}" using HTTP GET method where "id" should be replaced by a
	 * valid reminderId without {}
	 */

}