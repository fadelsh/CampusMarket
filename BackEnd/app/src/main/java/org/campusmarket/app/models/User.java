	package org.campusmarket.app.models;
	
	import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
	
	import javax.persistence.CascadeType;
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.JoinTable;
	import javax.persistence.OneToMany;
	import javax.persistence.Table;
	import javax.validation.constraints.NotNull;
	
	import com.fasterxml.jackson.annotation.JsonIgnore;
	import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	
	
	/**
	 * A class to represent the model for a user
	 * @author fadelalshammasi
	 * @author nheisler
	 *
	 */
	@Entity
	@Table(name = "users")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public class User implements Serializable
	{
	    private static final long serialVersionUID = 1L;
	
	    /*--- Class Variables ---*/
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private int     Id;
	    
	    @NotNull
	    @Column(name = "username", unique = true)
	    private String  username;
	    
	    @NotNull
	    @Column(name = "password")
	    private String password;
	    
	    @Column(name = "firstname")
	    private String  firstname;
	    
	    @Column(name = "lastname")
	    private String  lastname;
	    
	    @Column(name = "email", unique = true)
	    private String  email;
	    
	    @Column(name = "university")
	    private String  university;
	    
	    @Column(name = "admin")
	    private boolean admin;
	    
	
	    /*--- Links to Other Repositories ---*/
	    @OneToMany(cascade = CascadeType.ALL,
	               orphanRemoval = true)
	    @JoinTable(name = "user_sessions", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "sess_id"))
	    @JsonIgnore()
	    private Set<Session> sessions = new HashSet<>();
	
	    @OneToMany()
	    @JoinTable(name = "shopping_carts", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
	    @JsonIgnore()
	    private Set<Item> cart = new HashSet<>();
	
	    
	    /*--- Constructors ---*/
	    /**
	     * default constructor 
	     */
	    public User() { }
	
	    /**
	     * A constructor to create a user with all the required parameters of the users table 
	     * @param username
	     * @param password
	     * @param firstname
	     * @param lastname
	     * @param email
	     * @param university
	     * @param admin
	     */
	    public User(String username, 
	                String password,
	                String firstname,
	                String lastname,
	                String email,
	                String university,
	                boolean admin)
	    {
	        this.username = username;
	        this.password = password;
	        this.firstname = firstname;
	        this.lastname = lastname;
	        this.email = email;
	        this.university = university;
	        this.admin = admin;
	    }
	    
	    
	    public User(int id,String username, 
	            String password,
	            String firstname,
	            String lastname,
	            String email,
	            String university,
	            boolean admin)
	{
	    	this.Id=id;
	    this.username = username;
	    this.password = password;
	    this.firstname = firstname;
	    this.lastname = lastname;
	    this.email = email;
	    this.university = university;
	    this.admin = admin;
	}
	
	
	    /*--- Getter Methods ---*/
	    
	    /**
	     * A getter method for username of the user
	     * @return the username 
	     */
	    public String getUsername()
	    {
	        return this.username;
	    }
	    
	    /**
	     * A getter method for password of the user
	     * @return the password 
	     */
	    public String getPassword()
	    {
	        return this.password;
	    }
	    
	    /**
	     * A getter method for the firstname of the user
	     * @return the firstname
	     */
	    public String getFirstname()
	    {
	        return this.firstname;
	    }
	    
	    /**
	     * A getter method for the lastname of the user
	     * @return the lastname
	     */
	    public String getLastname()
	    {
	        return this.lastname;
	    }
	    
	    /**
	     * A getter method for the id of the user
	     * @return the id 
	     */
	    public int getId()
	    {
	        return this.Id;
	    }
	    
	    /**
	     * A getter method for university of the user
	     * @return the university 
	     */
	    public String getUniversity()
	    {
	        return this.university;
	    }
	    
	    /**
	     * A getter method for email of the user
	     * @return the email
	     */
	    public String getEmail()
	    {
	        return this.email;
	    }
	    
	    /**
	     * A getter method for admin of the user
	     * @return true if admin, otherwise false
	     */
	    public boolean getAdmin()
	    {
	        return this.admin;
	    }
	    
	    /**
	     * A getter method for the set of sessions of the user
	     * @return A set of sessions 
	     */
	    public Set<Session> getSessions()
	    {
	        return sessions;
	    }
	
	    public Set<Item> getCart()
	    {
	        return cart;
	    }
	
	    /*--- Setter Methods ---*/
	    
	    /**
	     * A setter method to change the username of the user
	     * @param userName
	     */
	    public void setUsername(String userName)
	    {
	        this.username = userName;
	    }
	    
	    /**
	     * A setter method to change the password of the user
	     * @param password
	     */
	    public void setPassword(String password)
	    {
	        this.password = password;
	    }
	    
	    /**
	     * A setter method to change the first name of the user
	     * @param firstName
	     */
	    public void setFirstname(String firstName)
	    {
	        this.firstname = firstName;
	    }
	    
	    /**
	     * A setter method to change the lastname of the user
	     * @param lastName
	     */
	    public void setLastname(String lastName)
	    {
	        this.lastname = lastName;
	    }
	    
	    /**
	     * A setter method to change the id of the user
	     * @param Id
	     */
	    public void setId(int Id)
	    {
	        this.Id = Id;
	    }
	    
	    /**
	     * A setter method to change the university of the user
	     * @param university
	     */
	    public void setUniversity(String university) 
	    {
	        this.university = university;
	    }
	    
	    /**
	     * A setter method to change the email of the user
	     * @param email
	     */
	    public void setEmail(String email) 
	    {
	        this.email = email;
	    }
	    /**
	     * A setter method to change the sessions set of the user
	     * @param sessions
	     */
	    public void setSessions(Set<Session> sessions)
	    {
	        this.sessions = sessions;
	    }
	
	
	    /*--- Class Methods ---*/
	    
	    /**
	     *  A method to get the string representation of an item 
	     */
	    @Override
	    public String toString()
	    {
	        String ret = new String();
	        // Convert the class info into a string format
	        ret = String.format("{Username:%1$s}\n{Id:%2$d}\n{Firstname:%3$s}\n{Lastname:%4$s}\n{Email:%5$s}\n{University:%6$s}\n{Admin:%7$b}",
	                            this.username,
	                            this.Id,
	                            this.firstname,
	                            this.lastname,
	                            this.email,
	                            this.university,
	                            this.admin
	                            );
	        return ret;
	    }
	
	    /**
	     * A method to compare whether two users are the same
	     * @param u  -- User to compare to
	     * @return True if they are the same, false otherwise
	     */
	    public boolean compareTo(User u)
	    {
	        if (this.username.equals (u.username)
	        &&  this.password.equals (u.password)
	        &&  this.Id       == u.Id
	        &&  this.email.equals (u.email)) return true;
	        else return false;
	    }
	
	    public void dropAllSessions()
	    {
	        this.sessions.clear();
	    }
	
	    /**
	     * A method to add a seesion to the sessions set
	     * @param s
	     */
	    public void addSession(Session s)
	    {
	        this.sessions.add(s);
	    }
	
	    /**
	     * A method to remove/drop a session from the session set
	     * @param s
	     */
	    public void dropSession(Session s)
	    {
	        this.sessions.remove(s);
	    }
	
	    /**
	     * A method to add an item to the users shopping cart
	     * @param i
	     */
	    public void addItem(Item i)
	    {
	        this.cart.add(i);
		}
		
		/**
	     * A method to drop an item to the users shopping cart
	     * @param i
	     */
	    public void dropItem(Item i)
	    {
	        this.cart.remove(i);
	    }
	}
