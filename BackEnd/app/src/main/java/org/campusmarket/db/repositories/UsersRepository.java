	package org.campusmarket.db.repositories;
	
	import org.campusmarket.app.models.User;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	
	import java.math.BigInteger;
	import java.util.*;
	import org.springframework.data.repository.query.Param;
	import org.springframework.stereotype.Component;
	import org.springframework.stereotype.Repository;
	import org.springframework.transaction.annotation.Transactional;
	
	/**
	 * An interface for User to represent the UsersRepository. It that has the declarations of the methods that we use in the controller with some customized queries 
	 * @author fadelalshammasi
	 * @author nheisler 
	 *
	 */
	@Repository
	@Component
	public interface UsersRepository extends JpaRepository<User, Integer>
	{
	
		/**
	     * A method to get all users in the users table 
	     * @return a list of all users in the database
	     */
	    List<User> findAll();
	    
	    /**
	     * find a user by their ID
	     * @param id
	     * @return the user with id
	     */
	    User findById(@Param("id") int id);
	    
	    /**
	     * find a user by their email
	     * @param email
	     * @return the user with email
	     */
	    User findByEmail(@Param("email") String email);
	    
	    
	    /**
	     * find a user by their username
	     * @param username
	     * @return the user with username
	     */
	    User findByUsername(@Param("username") String username);
	    
	    /**
	     * A customized query to help deleting a user from the users table given their id
	     * @param id
	     */
	    @Query(nativeQuery = true, value="DELETE FROM users WHERE id=:id")
	    @Modifying
	    void deleteById (@Param("id") int id);
	
	    
	    /**
	     * A method to check if a user exists in the users table or not given their username 
	     * @param username
	     * @return 1 if it does, otherwise 0 
	     */
	    @Query(nativeQuery = true, value="SELECT EXISTS (SELECT * from users where username=:username)")
	    @Transactional(readOnly = true)
	    int existsByUserName(@Param("username") String username);
	    
	    /**
	     * A query method to get all the shopping cart items of a user
	     * @param user_id
	     * @return List of integers representing item refnums
	     */
	    @Query(nativeQuery = true, value = "SELECT item_id FROM shopping_carts WHERE user_id=:user_id")
	    @Transactional(readOnly = true)
	    List<Integer> getShoppingCartItems(@Param("user_id") int user_id);
	
	    @Query(nativeQuery = true, value = "DELETE FROM shopping_carts WHERE user_id=:user_id AND item_id=:item_id")
	    @Modifying
	    @Transactional(readOnly = false)
	    void removeItemFromCart(@Param("user_id") int user_id, @Param("item_id") int item_id);
	
	    @Query(nativeQuery = true, value = "DELETE FROM shopping_carts WHERE user_id=:user_id")
	    @Modifying
	    @Transactional(readOnly = false)
	    void removeEverythingFromCart(@Param("user_id") int user_id);
	    
	    @Query(nativeQuery = true, value = "SELECT user_id FROM shopping_carts WHERE item_id=:item_id")
	    @Transactional(readOnly = true)
	    int getBuyerId (@Param("item_id") int item_id);
	
	    /**
	     * A query method to check if an item exists in a users cart
	     * @param user_id the user id to check
	     * @param item_id the item id to check
	     * @return true if the item exists, false otherwise
	     */
	    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT * FROM shopping_carts WHERE user_id=:user_id AND item_id=:item_id)")
	    @Transactional(readOnly = true)
	    int existsInUserCart(@Param("user_id") int user_id, @Param("item_id") int item_id);
	    
	    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT * FROM shopping_carts WHERE item_id=:item_id)")
	    @Transactional(readOnly = true)
	    int existsInUserCartOnlyRefnum(@Param("item_id") int item_id);
	    
	}
	
