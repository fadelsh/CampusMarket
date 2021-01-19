	package org.campusmarket.app.models;
	
	/**
	 * A model class for login request to help with session 
	 * @author nheisler 
	 *
	 */
	public class LoginRequest
	{
	    private String username;
	    private String password;
	
	    
	    /**
	     * default constructor 
	     */
	    public LoginRequest() { }
	    
	    /**
	     * Create a new login request with a username and a password
	     * @param username
	     * @param password
	     */
	    public LoginRequest(String username, String password)
	    {
	        this.username = username;
	        this.password = password;
	    }
	    
	    /**
	     * Setter for password
	     * @param password the password to set
	     */
	    public void setPassword(String password)
	    {
	        this.password = password;
	    }
	    /**
	     * Setter for username
	     * @param username the username to set
	     */
	    public void setUsername(String username)
	    {
	        this.username = username;
	    }
	    /**
	     * Getter for password
	     * @return the password
	     */
	    public String getPassword()
	    {
	        return password;
	    }
	    /**
	     * Getter for username
	     * @return the username
	     */
	    public String getUsername()
	    {
	        return username;
	    }
	}