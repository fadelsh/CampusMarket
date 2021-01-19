	package org.campusmarket.app.controllers;
	
	import java.util.ArrayList;
	import java.util.List;
	
	import org.apache.commons.logging.Log;
	import org.apache.commons.logging.LogFactory;
	import org.campusmarket.app.models.Item;
	import org.campusmarket.app.models.Session;
	import org.campusmarket.app.models.User;
	import org.campusmarket.db.repositories.ItemsRepository;
	import org.campusmarket.db.repositories.SessionsRepository;
	import org.campusmarket.db.repositories.UsersRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.server.ResponseStatusException;
	
	enum Checkout
    {
        FORSALE,           // 0
        CHECKOUT,          // 1
        AWAITPAYMENT,	   // 2
        AWAITRECIEVED;     // 3
    }

	/**
	 * a class to represent the controller for users
	 * @author fadelalshammasi
	 * @author nheisler 
	 */
	@RequestMapping("/carts")
	@RestController
	public class CartController
	{
	    @Autowired
	    private UsersRepository users;
	
	    @Autowired
	    private SessionsRepository sessions;
	
	    @Autowired
	    private ItemsRepository items;
	
	
	    Log log = LogFactory.getLog(UserController.class);
	    
	    /**
	     * A method to add an item to a users shopping cart
	     * @param refnum the item number
	     * @param sessid the session id of the user
	     * @return true if add was successful
	     */
	    @RequestMapping("/add/{refnum}")
	    private boolean addToMyCart(@PathVariable("refnum") int refnum,
	                               @RequestParam(name = "sessid", required = true) String sessid)
	    {
	        if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
	        User loggedIn = users.findById(sessions.findUserBySession(sessid));
	
	        Item addItem = items.findByRefnum(refnum);
	
	        if (addItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an item with refnum: " + refnum);
	
	        if (loggedIn != null)
	        {
                addItem.setCheckout(Checkout.CHECKOUT.ordinal());
                items.save(addItem);
	            loggedIn.addItem(addItem);
	            log.info("User with ID: " + loggedIn.getId() + " added item with refnum: " + refnum + " to shopping cart.");
	            users.save(loggedIn);
	            return true;
	        }
	        else return false;
        }

	    /**
	     * A method to drop an item from a users shopping cart
	     * @param refnum the item number
	     * @param sessid the session id of the user
	     * @return true if drop was successful
	     */
	    @RequestMapping("/drop/{refnum}")
	    private boolean dropFromMyCart(@PathVariable("refnum") int refnum,
	                                  @RequestParam(name = "sessid", required = true) String sessid)
	    {
	        if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
	        User loggedIn = users.findById(sessions.findUserBySession(sessid));
	
	        Item dropItem = items.findByRefnum(refnum);
	
	        if (dropItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an item with refnum: " + refnum);
	
	        if (users.existsInUserCart(loggedIn.getId(), refnum) == 1)
	        {
                dropItem.setCheckout(Checkout.FORSALE.ordinal());
                items.save(dropItem);
	            users.removeItemFromCart(loggedIn.getId(), refnum);
	            log.info("User with ID: " + loggedIn.getId() + " dropped item with refnum: " + refnum + " from shopping cart.");
	            users.save(loggedIn);
	            return true;
	        }
	        else return false;
	    }
	
	    /**
	     * A method to clear the contents of a users shopping cart
	     * @param sessid the session id of the user
	     * @return nothing (void)
	     */
	    @RequestMapping("/clear")
	    private void clearCartItems(@RequestParam(name = "sessid", required = true) String sessid)
	    {
	        if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
	        User loggedIn = users.findById(sessions.findUserBySession(sessid));
	
	        try
	        {
				for (Item item : loggedIn.getCart())
				{
					item.setCheckout(Checkout.FORSALE.ordinal());	
				}
	            users.removeEverythingFromCart(loggedIn.getId());
	            log.info("User with ID: " + loggedIn.getId() + " cleared shopping cart.");
	            users.save(loggedIn);
	        }
	        catch(Exception e)
	        {
	            log.error(e);
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops! Something went wrong...");
	        }
	    }
	 
	    @RequestMapping(value = "/count", method = RequestMethod.GET)
	    private int getCartCount(@RequestParam(name = "sessid", required = true) String sessid)
	    {
	        if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
	        User loggedIn = users.findById(sessions.findUserBySession(sessid));
	
	        try
	        {
	            return loggedIn.getCart().size();
	        }
	        catch (Exception e)
	        {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops! Something went wrong...");
	        }
	    }
	    @RequestMapping("/get")
	    private List<Item> getMyCartItems(@RequestParam(name = "sessid", required = true) String sessid)
	    {
	        if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
	        }
	
	        Session active = sessions.findBySessId(sessid);
	        
	        if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
	        User loggedIn = users.findById(sessions.findUserBySession(sessid));
	
	        try
	        {
	            List<Integer> refnums = users.getShoppingCartItems(loggedIn.getId());
	            List<Item> cart = new ArrayList<Item>();
	            for (Integer integer : refnums)
	            {
	                cart.add(items.findByRefnum(integer));
	            }
	            return cart;
	        }
	        catch (Exception e)
	        {
	            log.error(e);
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops! Something went wrong...");
	        }
	    }
		
		@GetMapping("/checkout/{refnum}")
        public boolean checkoutItem(@PathVariable("refnum") int refnum,
                                    @RequestParam(name = "sessid", required = true) String sessid)
        {
            if (sessid.isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
            }

            Session active = sessions.findBySessId(sessid);
            
            if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);

            User loggedIn = users.findById(sessions.findUserBySession(sessid));

            Item addItem = items.findByRefnum(refnum);

            if (addItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an item with refnum: " + refnum);

            if (loggedIn != null)
            {
                addItem.setCheckout(Checkout.AWAITPAYMENT.ordinal());
                items.save(addItem);
                return true;
            }
            return false;    
		}
		
		@GetMapping("/received/{refnum}")
        public boolean SetItemRecieved(@PathVariable("refnum") int refnum,
                                    @RequestParam(name = "sessid", required = true) String sessid)
        {
            if (sessid.isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
            }

            Session active = sessions.findBySessId(sessid);
            
            if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);

            User loggedIn = users.findById(sessions.findUserBySession(sessid));

            Item dropItem = items.findByRefnum(refnum);

            if (dropItem == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an item with refnum: " + refnum);

            if (loggedIn != null)
            {
				loggedIn.dropItem(dropItem);;
                items.delete(dropItem);
                return true;
            }
            return false;
        }
	}