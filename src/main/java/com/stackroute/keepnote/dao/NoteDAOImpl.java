package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

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
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
     @Autowired
     private SessionFactory sessionFactory;
     
	public NoteDAOImpl(SessionFactory sessionFactory) {
 
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new note
	 */
	
	public boolean createNote(Note note) {
		Session session = sessionFactory.getCurrentSession();
		
		
				//if(session.save(note)!=null || session.save(note)!=" ") {
				 try {
					session.save(note);
					 return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return false;
				}

	}

	/*
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Note note1 = getNoteById(noteId);
			if(note1!=null) {
				session.delete(note1);
				return true;
			}
		} catch (NoteNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	
	public List<Note> getAllNotesByUserId(String userId) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria cr = session.createCriteria(Note.class);
		List<Note> notes=cr.add(Restrictions.eq("createdBy", userId)).list();
		return notes;

	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		
		Session session = sessionFactory.getCurrentSession();
		
		
		Note note = session.get(Note.class, noteId);
			if(note==null) {
				throw new NoteNotFoundException("note not found");
			}
				return note;

	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {
		Session session = sessionFactory.getCurrentSession();
		session.update(note);
		return true;
		
	}

}
