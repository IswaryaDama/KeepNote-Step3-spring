package com.stackroute.keepnote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Category;
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
public class CategoryServiceImpl implements CategoryService {
	/*
	 * Autowiring should be implemented for the CategoryDAO. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword.
	 */

	/*
	 * This method should be used to save a new category.
	 */
	
	@Autowired
	private CategoryDAO categoryDao;
	
	
	
	public CategoryServiceImpl(CategoryDAO categoryDao) {
		super();
		this.categoryDao = categoryDao;
	}

	public boolean createCategory(Category category) {
		Boolean status = false;
		try {
			if(categoryDao.createCategory(category)){
				status= true;
			}
			else {
				
				//return false;
				throw new CategoryNotFoundException("category not found");
				
			}
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
	}

	/* This method should be used to delete an existing category. */
	public boolean deleteCategory(int categoryId) {
		if(categoryDao.deleteCategory(categoryId)) {
			return true;
		}
		else {
		return false;
		}
	}

	/*
	 * This method should be used to update a existing category.
	 */

	public Category updateCategory(Category category, int id) throws CategoryNotFoundException {
		
      if(getCategoryById(id)!=null) {
			
    	  if(categoryDao.updateCategory(category)) {
  			return category;
  		}
    	  else {
    		  return null;
    	  }
    	  }
		else {
			return null;
		}	

	}

	/*
	 * This method should be used to get a category by categoryId.
	 */
	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		 Category cate1=categoryDao.getCategoryById(categoryId);
			if(cate1==null) {
				throw new CategoryNotFoundException("category not found");
			}
				return cate1;			
	}

	/*
	 * This method should be used to get a category by userId.
	 */

	public List<Category> getAllCategoryByUserId(String userId) {
	
		return categoryDao.getAllCategoryByUserId(userId);
		

	}

}
