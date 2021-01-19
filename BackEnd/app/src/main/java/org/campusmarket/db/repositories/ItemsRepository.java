package org.campusmarket.db.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.campusmarket.app.models.Item;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.query.Param;
	import org.springframework.transaction.annotation.Transactional;
	
	
	/**
	 * An interface for Item to represent the ItemsRepository. It that has the declarations of the methods that we use in the controller with some customized queries 
	 * @author fadelalshammasi
	 * @author nheisler 
	 *
	 */
	public interface ItemsRepository extends JpaRepository<Item, Integer>
	{
		
		/**
		 * Find an item by their refnum in the database
		 * @param refnum
		 * @return an item 
		 */
		Item findByRefnum(@Param("refnum") int refnum);
         
     
        List<Item> findAll();
        
        
       
        @Query(nativeQuery = true, value = "SELECT * FROM items WHERE checkout=0")
        List<Item> findItemdCheckout();

		/**
	     * A method to search for items by their name (or part of the name)
	     * @param name
	     * @return a collection of the items that have that name(or part of it) sorted by price
	     */
		@Query(nativeQuery = true, value="SELECT * FROM items WHERE name LIKE %:name% ORDER BY price")
	    @Transactional(readOnly = true)
		ArrayList<Item>findByName(@Param("name") String name);
	    
		/**
	     * A method to search for items by their name (or part of the name)
	     * @param name
	     * @return a collection of the items that have that name(or part of it) sorted by price 
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE category=:category ORDER BY price")
	    @Transactional(readOnly = true)
	    ArrayList<Item> findByCategory(@Param("category") String category);
	    
	    /**
	     * A method to search for items by their condition sorted by price
	     * @param cond
	     * @return  a collection of the items that have that condition 
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE cond=:cond ORDER BY price")
	    @Transactional(readOnly = true)
	    ArrayList<Item>findByCond(@Param("cond") String cond);
	
	    /**
	     * a method to search for items using their names AND conditions 
	     * @param name
	     * @param cond
	     * @return List of items with the name and condition provided (or part of them)
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE name LIKE %:name% AND cond=:cond")
	    @Transactional(readOnly = true)
	    ArrayList<Item>findByCondAndName(@Param("name") String name,@Param("cond") String cond);
	    
	    
	    /**
	     * a method to search for items using their category AND condition AND the maximum price desired to narrow the search even more
	     * @param name
	     * @param cond
	     * @return List of items with the category & condition & price provided sorted by price
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE cond=:cond AND category=:category AND price<=:price ORDER BY price")
	    @Transactional(readOnly = true)
	   ArrayList<Item>findByCondAndCategoryAndPrice(@Param("cond") String cond, @Param("category")String category, @Param("price") double price);
	    
	    
	    /**
	     * A method to get all the items for a specific seller
	     * @param seller
	     * @return an arrylist of the items for that seller
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE seller=:seller")
	    @Transactional(readOnly = true)
	    ArrayList<Item>findBySeller(@Param("seller") String seller);
	
	    /**
	     *  A method to search for items given their all fields: name,cond,category and price
	     * @param name
	     * @param cond
	     * @param category
	     * @param price
	     * @return a collection of all items with the name,cond,category and price
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE name LIKE %:name% AND cond=:cond AND category=:category AND price<=:price")
	    @Transactional(readOnly = true)
	    Collection<Item>sortQuery(@Param("name") String name, @Param("cond") String cond, @Param("category")String category, @Param("price") double price);
	
	   
	    @Query(nativeQuery = true, value="SELECT * FROM items WHERE name LIKE %:name% AND cond=:cond AND category=:category")
	    @Transactional(readOnly = true)
	    Collection<Item>sortQuery(@Param("name") String name, @Param("cond") String cond, @Param("category")String category);
	    
	
		
	}
