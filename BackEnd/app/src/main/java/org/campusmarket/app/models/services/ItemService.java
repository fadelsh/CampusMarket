	package org.campusmarket.app.models.services;
	
	import java.util.ArrayList;
	import java.util.List;
	
	import org.campusmarket.app.models.Item;
	import org.campusmarket.app.exception.MyFileNotFoundException;
	import org.campusmarket.db.repositories.ItemsRepository;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.repository.query.Param;
	import org.springframework.stereotype.Service;
	
	
	
	/**
	 * A service class to help testing the mockito for items 
	 * @author fadelalshammasi
	 *
	 */
	@Service
	public class ItemService {
	
		@Autowired
		 private ItemsRepository itemsRepo;
		
	
		/**
		 * A method to get all items 
		 * @return the list of all items added 
		 */
		public List<Item> getAllItemList(){
							
		    return itemsRepo.findAll();
		
		}
		
		/**
		 * A method to find an item given their refnum 
		 * @param refnum
		 * @return the item that has that unique refnum 
		 */
		public Item findByRefnum(int refnum) {
			return itemsRepo.findByRefnum(refnum);
			
		}
		
		/**
		 * A method to get all items sold by a specfic seller 
		 * @param seller
		 * @return an arraylist of items what were sold by seller 
		 */
		public ArrayList<Item>findBySeller( @Param("seller") String seller){
			 return itemsRepo.findBySeller(seller);
		}
		
		
	
		    /**
		     * A method to search for a file in the db
		     * @param id
		     * @return the file found otherwise throwing an exception
		     */
		    public Item getFile(int refnum) {
		    	try {
		        return itemsRepo.findByRefnum(refnum);
		    	}
		    	
		    	catch (MyFileNotFoundException e) {
		    		throw new MyFileNotFoundException("Couldn't find file with id " + refnum);
		    	}
		    }
		    public ArrayList<Item>findByCategory( @Param("category") String category){
				 return itemsRepo.findByCategory(category);
			}
			  
		    public ArrayList<Item>findByCondAndName( @Param("name") String name, @Param("cond") String cond){
				 return itemsRepo.findByCondAndName(name, cond);
			}
		    
		    public ArrayList<Item>findByCondAndCategoryAndPrice(@Param("cond") String cond, @Param("category")String category, @Param("price") double price){
		    	return itemsRepo.findByCondAndCategoryAndPrice(cond, category, price);
		    }

		   public ArrayList<Item>findByCond(@Param("cond") String cond){
			   return itemsRepo.findByCond(cond);
		   }
		   
		   
		  public ArrayList<Item>findByName(@Param("name") String name){
			  return itemsRepo.findByName(name);
			  
		  }
		    
		}
	
		
	
