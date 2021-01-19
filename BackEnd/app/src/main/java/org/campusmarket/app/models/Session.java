	package org.campusmarket.app.models;
	
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.Id;
	import javax.persistence.Table;
	
	/**
	 * A class to represent the model for a session
	 * @author nehisler 
	 *
	 */
	@Entity()
	@Table(name = "session")
	public class Session
	{
	    @Id
	    @Column(name = "sess_id")
	    private String sessId;
	
	    @Column(name = "admin")
	    private boolean admin;
	
	
	    /*---Constructors---*/
	    
	    /**
	     * default constructor 
	     */
	    public Session() { }
	    
	    /**
	     * Constructs a new session with an id and whether you are an admin or not 
	     * @param Id
	     * @param admin
	     */
	    public Session(String Id, boolean admin)
	    {
	        this.sessId = Id;
	        this.admin = admin;
	    }
	
	
	    /*---Getters and Setters--*/
	    
	    /**
	     * A getter for session id
	     * @return the session id
	     */
	    public String getId()
	    {
	        return sessId;
	    }
	    /**
	     * A setter method for id
	     * @param id the id to set
	     */
	    public void setId(String id)
	    {
	        sessId = id;
	    }
	    
	    /**
	     * A getter method to determine if is an admin or not
	     * @return true if admin, otherwise false 
	     */
	    public boolean getAdmin()
	    {
	        return this.admin;
	    }
	    
	    /**
	     * A setter for admin to change to admin status 
	     * @param admin
	     */
	    public void setAdmin(boolean admin)
	    {
	        this.admin = admin;
	    }
	}