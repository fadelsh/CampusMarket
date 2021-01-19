	package org.campusmarket.app.models.services;
	
	import java.util.List;

import org.campusmarket.app.models.Item;
import org.campusmarket.app.models.User;
	import org.campusmarket.db.repositories.ItemsRepository;
	import org.campusmarket.db.repositories.UsersRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	
	/**
	 * A service class to help testing the mockito for users
	 * @author nehisler , @fadelsh
	 *
	 */
	@Service
	public class UserService
	{
	    @Autowired
	    private UsersRepository repo;
	    
	
	    /**
	     * A method to get all users
	     * @return a list of all users created 
	     */
	    public List<User> findAll()
	    {					
		    return repo.findAll();
	    }
	
	    /**
	     * A method to find an item given their email
	     * @param email
	     * @return the user that has that unique email
	     */
	    public User findByEmail(String email)
	    {
	        return repo.findByEmail(email);
	    }
	
	    /**
	     * A method to find an item given their id
	     * @param id
	     * @return the user that has that unique id
	     */
	    public User findById(int id)
	    {
	        return repo.findById(id);
	    }
	    
	    public int existsByUserName(String username) {
	    	return repo.existsByUserName(username);
	    }
	    
	
	    public void removeItemFromCart(int user_id, int item_id) {
	    	repo.removeItemFromCart(user_id, item_id);
	    }
	    
	    public void clearCart(int user_id) {
	    	repo.removeEverythingFromCart(user_id);
		}
		
		public List<Integer> getShoppingCartItems(int user_id)
		{
			return repo.getShoppingCartItems(user_id);
		}
	    
	}