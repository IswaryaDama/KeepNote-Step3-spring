package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
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
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	@Autowired
	private SessionFactory sessionFactory;

	public CategoryDAOImpl(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {
		if(sessionFactory.getCurrentSession().save(category)!=null) {
			return true;
		}else {
			return false;
		}
		
		//return status;
	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		Boolean status = false;
		Session session = sessionFactory.getCurrentSession();

		try {
			Category cat1 = getCategoryById(categoryId);
			session.delete(cat1);
			return true;
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}

	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {

		Session session = sessionFactory.getCurrentSession();
		session.update(category);
		return true;

	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {

		Session session = sessionFactory.getCurrentSession();

		Category category = session.get(Category.class, categoryId);
		if (category == null) {
			throw new CategoryNotFoundException("category not found");
		}
		return category;

	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(Category.class);
		List<Category> allCategories = cr.add(Restrictions.eq("categoryCreatedBy", userId)).list();
		/*String queryString = "from " + Category.class.getName() + " where categoryCreatedBy like :userId";
		Query query = session.createQuery(queryString);	
		query.setParameter("userId", userId );*/
		return allCategories;

	}

}
