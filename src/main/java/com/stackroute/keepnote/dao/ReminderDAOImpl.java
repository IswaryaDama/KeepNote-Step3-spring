package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Reminder;

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
public class ReminderDAOImpl implements ReminderDAO {
	
	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
    private SessionFactory sessionFactory;

	public ReminderDAOImpl(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		
		if(sessionFactory.getCurrentSession().save(reminder)!=null) {
			return true;
		}
		return false;

	}
	
	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		
		Session session = sessionFactory.getCurrentSession();
		session.update(reminder);
		return true;

	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) {
		
		Boolean status = false;
		Session session = sessionFactory.getCurrentSession();

		try {
			Reminder re = getReminderById(reminderId);
			session.delete(re);
			return true;
		} catch (ReminderNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}

	}

	/*
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		
		Reminder reminder = sessionFactory.getCurrentSession().get(Reminder.class, reminderId);
		
		if(reminder==null) {
			throw new ReminderNotFoundException("reminder not found");
		}
		return reminder;

	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(Reminder.class);
		List<Reminder> reminders = cr.add(Restrictions.eq("reminderCreatedBy", userId)).list();
		return reminders;

	}

}
