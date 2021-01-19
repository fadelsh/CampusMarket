	package org.campusmarket.app.controllers;
	
	import java.util.ArrayList;
	import java.util.Collection;
	import java.util.List;
	
	import org.apache.commons.logging.Log;
	import org.apache.commons.logging.LogFactory;
	import org.campusmarket.app.exception.FileStorageException;
	import org.campusmarket.app.models.Item;
	import org.campusmarket.app.models.services.ItemService;
	import org.campusmarket.app.models.Session;
	import org.campusmarket.app.models.User;
	import org.campusmarket.db.repositories.ItemsRepository;
	import org.campusmarket.db.repositories.SessionsRepository;
	import org.campusmarket.db.repositories.UsersRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.core.io.ByteArrayResource;
	import org.springframework.core.io.Resource;
	import org.springframework.http.HttpHeaders;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.MediaType;
	import org.springframework.http.ResponseEntity;
	import org.springframework.util.StringUtils;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.PutMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.multipart.MultipartFile;
	import org.springframework.web.server.ResponseStatusException;
	import org.json.*;
	
	
	/**
	 * a class to represent the controller for items
	 * @author fadelsh
	 * @author nheisler
	 *
	 */
	@RestController
	@RequestMapping("/items")
	public class ItemController 
	{
		@Autowired
	    private ItemsRepository items;
		
		@Autowired
		private UsersRepository users;
		
		@Autowired
		private SessionsRepository sessions;
		
		@Autowired
		private ItemService files;
	
		
		Log log = LogFactory.getLog(ItemController.class);
		
		//1. POST requests
		
		 /**
		  * A method to post a new item 
		  * @param the body of the item model class
		  * @param sessid of the user posting the item
		  */
		@PostMapping("/new/file")
		private Item newItem(@RequestParam(name = "sessid", required = true) String sessid,
				@RequestParam("file") MultipartFile file, @RequestParam ("json") String str )
		{
			if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
			JSONObject json=new JSONObject(str);
			String name=json.getString("name");
			Double price=  Double.parseDouble(json.getString("price")); 
			String category=json.getString("category");
			String cond=json.getString("condition");
			
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	
			if(fileName.contains("..")) {
	            throw new FileStorageException("Filename contains invalid path characters " + fileName);
	        }
			
			
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
			try
			{	
				User u=users.findById(sessions.findUserBySession(sessid));
				
				Item item=new Item (name,price,category,cond, fileName, file.getContentType(), file.getBytes());
				item.setCheckout(0);
		
				item.setUser(u);
				items.save(item);
				
	        log.info(" success: a new item was created with a reference number(keep for your record): " + item.getRefnum());
	        return item;
	
			}
			catch (Exception e)
			{
		        log.error(e.getMessage());
		        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no such user with this username so sorry we won't be able to add your item :(",e);
		    }
		}
		
		
		
		@PostMapping("/new")
		private Item newItem2(@RequestBody Item item ,@RequestParam(name = "sessid", required = true) String sessid){
			if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
		
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
			try
			{	
				User u=users.findById(sessions.findUserBySession(sessid));
				item.setCheckout(0);
		
				item.setUser(u);
				items.save(item);
				
	        log.info(" success: a new item was created with a reference number(keep for your record): " + item.getRefnum());
	        return item;
	
			}
			catch (Exception e)
			{
		        log.error(e.getMessage());
		        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no such user with this username so sorry we won't be able to add your item :(",e);
		    }
		}
	
	
		//2. PUT requests
		
		/**
		 * A method to update an item 
		 * @param item
		 * @param refnum
		 * @param sessid
		 */
	    @PutMapping("/update/{refnum}")
		private void updateItem(@RequestBody Item item, 
								@PathVariable (value = "refnum") int refnum,
								@RequestParam(name = "sessid", required = true) String sessid) 
		{
	    	
			if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
			User user = users.findById(sessions.findUserBySession(sessid));
	
			
			if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user.");
			if(users.existsByUserName(user.getUsername())==0) {
				log.error("There's no such user with this username in our system so sorry we can't update this item");
				return;
			}
	
			Item oldItem = items.findByRefnum(refnum);
	
			if (oldItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find item with refnum: " + refnum);
	
			if (active.getAdmin() || oldItem.getUser().getUsername().equals(user.getUsername()))
			{
				try 
				{
					
					updateItemsFields(item,oldItem);
	
					items.save(oldItem);
						
					log.info(" success: the item with a reference number of " + refnum +" was updated");
				} 
				catch (Exception e)
				{
					log.error(e.getMessage());
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Failure: Could not update the item with refnum: " + refnum);
				}
			}
			else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
	
	    }
	    
	    private void updateItemsFields(@RequestBody Item item, Item oldItem) {
	    	        oldItem.setName(item.getName());
			oldItem.setPrice(item.getPrice());
			oldItem.setCategory(item.getCategory());
			oldItem.setCondition(item.getCondition());
			oldItem.setImg(item.getImg());
	    		
	    }
	    
	    //3. DELETE requests
	    
	    /**
	     * A method to delete an item
	     * @param refnum
	     * @param sessid
	     */
	    @RequestMapping(value = "/delete/{refnum}", method = RequestMethod.DELETE)
		private void deleteItem( @PathVariable(value = "refnum") int refnum,
								@RequestParam(name = "sessid", required = true) String sessid)
	    {
	    	if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
			User user = users.findById(sessions.findUserBySession(sessid));
	
			if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user.");
	
			Item oldItem = items.findByRefnum(refnum);
			
			if(users.existsByUserName(user.getUsername())==0) {
				log.error("There's no such user with this username in our system so sorry we can't update this item");
				return;
			}
	
			
			if (oldItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find item with refnum: " + refnum);
			
		
				
			if (active.getAdmin() || oldItem.getUser().getUsername().compareTo(user.getUsername()) == 0)
			{
				try 
				{
					
					if(users.existsInUserCartOnlyRefnum(refnum)==0) {
						items.deleteById(refnum);
					}
					else {
						int buyerId=users.getBuyerId(refnum);
						users.removeItemFromCart(buyerId, refnum);
						items.deleteById(refnum);
					}
					
					
					log.info(" success: the item with a reference number of " + refnum +" was deleted");
					
				}
				catch (Exception e)
				{
					log.error("item with refnum"+ refnum + " not found" );
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
				}
			}
			else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
	    }
	    
	    /**
	     * A method to delete all items in the campusmarket 
	     * @param sessid
	     */
	    @RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
	    private void deleteAll(@RequestParam(name = "sessid", required = true) String sessid)
	    {
			if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
			if (active.getAdmin())
			{
				try
				{
					items.deleteAll(); 
					log.info("User Table Cleared: all users removed.");
				} 
				catch (Exception e) 
				{
					log.error("No items in database to remove.");
					throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No items in database to remove.");
				}
			}
			else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
	   }
	    
	    //4. GET requests
	    
	    /**
		 * A method to get all the items
		 * @return a list of all items 
		 */
		@RequestMapping("/all/admin")
		private List<Item> getAll()
		{
			try 
			{
		    	return items.findAll();
			}
			catch (Exception e)
			{
	            log.error(e.getMessage());
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No items were found.");
	        }
	    }
	
		@RequestMapping("/all")
		private List<Item> getItemsCheckedOut()
		{
			try 
			{
		    	return items. findItemdCheckout();
			}
			catch (Exception e)
			{
	            log.error(e.getMessage());
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No items were found.");
	        }
	    }
	
		
	    
	    /**
	     * A method to get all the items for a specific seller
	     * @param seller
	     * @return an arrylist of the items for that seller
	     */
	    @GetMapping("/seller/{username}")
		private ArrayList<Item> findItemBySeller(@PathVariable("username") String seller)
		{	
			try 
			{
	    		return items.findBySeller(seller);
			}
			catch(Exception e)
			{
	    		log.error(e.getMessage());
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user has no items for sale.");
	    	}
	    }
	     
	      /**
	       * A method to search item by their name (or part of the name)
	       * @param name
	       * @return a collection of the items that have that name(or part of it) sorted by price
	       */
	    @GetMapping("/name/{name}")
	    private Collection<Item> findItemByName(@PathVariable("name") String name) {
	    	
	    	try {
	    	return items.findByName(name);
	    	
	    	} catch(Exception e) {
	    		log.error(e.getMessage());
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No item with this name was found in the cymarket");
	
	    	}
	    }
	     
	    /**
	     * A method to search for items by their category
	     * @param category
	     * @return  a collection of the items that have that category sorted by price 
	     */
	    @GetMapping("/category/{category}")
	    private ArrayList<Item> findItemByCategory(@PathVariable("category") String category) {
	    	try {
	    	return items.findByCategory(category);
	    	
	    	 } catch(Exception e) {
	    		log.error(e.getMessage());
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No item with this category was found in the cymarket");
	
	    	}
	    }
	    
	    
	    /**
	     * A method to search for items by their condition
	     * @param cond
	     * @return a collection of the items that have that condition sorted by price
	     */
	    @GetMapping("/cond/{cond}")
	    private Collection<Item> findItemByCondition(@PathVariable("cond") String cond) {
	    	try {
	    	return items.findByCond(cond);
	    	
		 } catch(Exception e) {
	 		log.error(e.getMessage());
	         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No item with this condition was found in the cymarket");
	
	 	}
	 }
	 
	     @GetMapping("/refnum/{refnum}")
	     private Item findItemByRefnum(@PathVariable("refnum") int refnum)
	   	{	
	   		try 
	   		{
	       		return items.findByRefnum(refnum);
	   		}
	   		catch(Exception e)
	   		{
	       		log.error(e.getMessage());
	               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there's no such item.");
	       	}
	       }
	    
	    
	     /**
	      * a method to search for items using their names AND conditions 
	      * @param name
	      * @param cond
	      * @return List of items with the name and condition provided (or part of them)
	      */
	    @GetMapping("/name/{name}/cond/{cond}")
	    private ArrayList<Item>findByCondAndName(@PathVariable("name") String name ,@PathVariable("cond") String cond){
	    	try {
			return items.findByCondAndName(name, cond);
			
	    	}catch(Exception e) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No item with this name & condition was found in the cymarket");
	
	    	}
	    }
	    
	    /**
	     * a method to search for items using their category AND condition AND the maximum price desired to narrow the search even more
	     * @param name
	     * @param cond
	     * @return List of items with the category & condition & price provided sorted by price
	     */
	    @GetMapping("/category/{category}/cond/{cond}/price/{price}")
	    private Collection<Item>findByCondPriceCategory(@PathVariable("cond") String cond ,@PathVariable("category") String category, @PathVariable("price") double price){
	    	try {
			return items.findByCondAndCategoryAndPrice(cond, category, price);
	    	} catch (Exception e) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No item with this name & category & condition & price limit was found in the cymarket");
	
	    	}
	    }
	    
	  
		 
		 /**
		  * A method to display(download if using the browser) the content of the file
		  * @param id
		  * @return
		  */
		   @GetMapping("/download/{refnum}")
		   private ResponseEntity<Resource> downloadFile(@PathVariable int refnum) {
		       
		        Item f = files.getFile(refnum);
	
		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(f.getFiletype()))
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.getFilename() + "\"")
		                .body(new ByteArrayResource(f.getImg()));
		    }
	
	}
	
