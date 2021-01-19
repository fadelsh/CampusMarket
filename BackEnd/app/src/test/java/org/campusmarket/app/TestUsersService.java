package org.campusmarket.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.campusmarket.app.models.Item;
import org.campusmarket.app.models.User;
import org.campusmarket.app.models.services.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * A  class to perform mockito tests for users
 * @author nheisler, fadelsh
 *
 */



public class TestUsersService extends TestServices {
	
    @InjectMocks
    UserService userService;


    @Test // test 1 NH
    public void getUserByEmailTest()
    {
        when(super.UsersRepo.findByEmail("nheisler@iastate.edu")).thenReturn(new User("nheisler",
                                                                           "testpw",
                                                                           "Nick",
                                                                           "Heisler",
                                                                           "nheisler@iastate.edu",
                                                                           "Iowa State University",
                                                                           true));

        User test = userService.findByEmail("nheisler@iastate.edu");
        
        assertEquals("nheisler", test.getUsername());
        assertEquals("testpw", test.getPassword());
        assertEquals("Nick", test.getFirstname());
        assertEquals("Heisler", test.getLastname());
        assertEquals("nheisler@iastate.edu", test.getEmail());
        assertEquals("Iowa State University", test.getUniversity());
        assertTrue(test.getAdmin());
    }

    @Test // test 2 NH
    public void GetAllUserTest()
    {
        List<User> list = new ArrayList<User>();

        User first = new User("nheisler","testpw","Nick","Heisler","nheisler@iastate.edu","Iowa State University",true);
        User second = new User("nheisler1","testpw","Nick","Heisler","nheisler1@iastate.edu","Iowa State University",false);
        User third = new User("nheisler2","testpw","Nick","Heisler","nheisler2@iastate.edu","Iowa State University",true);

        list.add(first);
        list.add(second);
        list.add(third);

        when(super.UsersRepo.findAll()).thenReturn(list);

        List<User> testList = userService.findAll();

        assertEquals(3, testList.size());

        assertFalse(testList.get(1).getAdmin());
    }

    @Test // test 3 NH
    public void GetUserByIdTest()
    {
        when(super.UsersRepo.findById(1)).thenReturn(new User("nheisler",
                                                   "testpw",
                                                   "Nick",
                                                   "Heisler",
                                                   "nheisler@iastate.edu",
                                                   "Iowa State University",
                                                   true));

        User test = userService.findById(1);

        assertEquals("nheisler", test.getUsername());
        assertEquals("testpw", test.getPassword());
        assertEquals("Nick", test.getFirstname());
        assertEquals("Heisler", test.getLastname());
        assertEquals("nheisler@iastate.edu", test.getEmail());
        assertEquals("Iowa State University", test.getUniversity());
        assertTrue(test.getAdmin());
    }
    
    
    
    @Test //test 1 FA- Demo4
	public void existsByUserName() {
    	User u= new User ("fadelsh","password","Fadel","Alshammasi","fadelsh@iastate.edu","ISU",false);
    	when(super.UsersRepo.existsByUserName("fadelsh")).thenReturn (1);
    	
    	int test=userService.existsByUserName(u.getUsername());
    	int test2=userService.existsByUserName("smitra");

        	
    	 assertEquals(1, test);
    	 assertEquals(0, test2);
    	 verify(super.UsersRepo, times(1)).existsByUserName("fadelsh");
    	
    }
    

    
    @Test // test 1 NH - Demo5
    public void testAddToCart()
    {
        User u = new User("nheisler","testpw","Nick","Heisler","nheisler@iastate.edu","Iowa State University",true);
        u.setId(1);
        Item itemOne=new Item (1,"Cat",999.99,"Pet", "in good shape", u);

        u.addItem(itemOne);

        List<Integer> lst = new ArrayList<Integer>();
        lst.add(1);

        when (super.UsersRepo.getShoppingCartItems(u.getId())).thenReturn(lst);

        List<Integer> ret = userService.getShoppingCartItems(u.getId());

        assertEquals(itemOne.getRefnum(), ret.get(0).intValue());
    }

    @Test // test 1 NH - Demo5
    public void testDropFromCart()
    {
        User u = new User("nheisler","testpw","Nick","Heisler","nheisler@iastate.edu","Iowa State University",true);
        u.setId(1);
        Item itemOne=new Item (1,"Cat",999.99,"Pet", "in good shape", u);

        u.addItem(itemOne);

        List<Integer> lst = new ArrayList<Integer>();
        lst.add(1);

        when (super.UsersRepo.getShoppingCartItems(u.getId())).thenReturn(lst);

        List<Integer> ret = userService.getShoppingCartItems(u.getId());

        assertEquals(itemOne.getRefnum(), ret.get(0).intValue());

        u.dropItem(itemOne);
        lst.clear();

        ret = userService.getShoppingCartItems(u.getId());

        assertEquals(0, ret.size());
    }
}
