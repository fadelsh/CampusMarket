	package org.campusmarket.app.models;
	
	import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	import com.fasterxml.jackson.annotation.JsonIgnore;
	import java.time.LocalDate;
	import java.time.format.DateTimeFormatter;
	
	import javax.persistence.*;
	import javax.persistence.CascadeType;
	import javax.persistence.Table;
	import javax.persistence.Entity;
	
	import org.hibernate.annotations.*;
	import org.hibernate.annotations.OnDeleteAction;
	import org.springframework.core.style.ToStringCreator;
	
	
	/**
	 * A class to represent the model for an Item
	 * @author fadelalshammasi
	 *
	 */
	@Entity
	@Table(name = "items")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	
	
	public class Item {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "refnum")
	    private int refnum;
		
	    @Column(name = "name")
	    private String  name;
	    
	    @Column(name = "price")
	    private double price;
	    
	    @Column (name="category")
	    private String category;
	    
	    @Column (name="cond")
	    private String cond;
	    
	    @CreationTimestamp
	    @Column (name= "postdate")
	    private LocalDate postdate;
	    
	    @Column (name="filename")
	    private String filename;
	    
	    @Column (name="filetype")
        private String filetype;
        
        @Column (name = "checkout")
        private int checkout;
	    
	    
	    
	    @Lob @Basic(fetch = FetchType.LAZY)
	    @Column (name= "img",columnDefinition="BLOP")
	    private byte[] img;
	    
	
	
	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "seller", referencedColumnName = "username", nullable = false)
	    @OnDelete(action = OnDeleteAction.CASCADE)
	    private User user;
	
	  
	
	  
	    
	    /**
	     * Default constructor 
	     */
	   public Item() {
		   
	   }
	   
	   /**
	    * A constructor to create an item with all the required parameters of the items table 
	    * @param refnum
	    * @param name
	    * @param price
	    * @param category
	    * @param condition
	    * @param user
	    */
	   public Item(int refnum, String name, double price, String category,String condition, User user, byte []img) {
		   this.refnum=refnum;
		   this.name=name;
		   this.price=price;
		   this.category=category;
		   this.cond=condition;
		   this.user=user;
		   this.postdate=LocalDate.now();
		   this.img=img;
	   }
	   
	   public Item (int refnum,String name, Double price, String category, String condition, User user){
	  	 this.refnum=refnum;
		   this.name=name;
		   this.price=price;
		   this.category=category;
		   this.cond=condition;
		   this.user=user;
	   
	   
	   }
	   
	   
	   public Item(String name, Double price, String category, String condition, String filename, String filetype, byte[] img ) {
	       this.name=name;
	       this.price=price;
	       this.category=category;
	       this.cond=condition;
	       this.filename=filename;
	       this.filetype=filetype;
	       this.img=img;
	       
	   
	   }
	   
	   /**
	    * A getter method to get the refnum of the item 
	    * @return refnum 
	    */
	   public int getRefnum() {
		   return this.refnum;
	   }
	    
	   /**
	    * A getter method to get the name of the item 
	    * @return name 
	    */
	   public String getName() {
		   return this.name;
	   }
	   /**
	    * A getter method to get the price of the item 
	    * @return price 
	    */
	   public double getPrice() {
		   return this.price;
	   }
	   /**
	    * A getter method to get the category of the item 
	    * @return category
	    */
	   public String getCategory() {
		   return this.category;
	   }
	   /**
	    * A getter method to get the condition of the item 
	    * @return condition 
	    */
	   public String getCondition() {
		   return this.cond;
	   }
	   /**
	    * A getter method to get the user who posted the item
	    * @return seller
	    */
	   public User getUser() {
		   return this.user;
	   }
	   
	   /**
	    * A getter method to get the BLOB of the file (image)
	    * @return fdata
	    */
	    
	   public byte[] getImg() {
		   return this.img;
	   }
	   
	   /**
	    * A getter method to get the name of the  file(image) 
	    * @return fname
	    */
	   public String getFilename() {
		   return this.filename;
	   }
	   
	   /**
	    * A getter to get the type of the file (pdf,png,...etc)
	    * @return
	    */
	   public String getFiletype() {
		   return this.filetype;
       }
       
       /**
        * @return the checkout
        */
       public int getCheckout() {
           return checkout;
       }
	   
	   
	   /**
	    * A getter method to get the date that the item was posted at the cmarket
	    * @return postdate
	    */
	   public LocalDate getPostedDate() {
		   return this.postdate;
	   }
	   
	   /**
	    * A setter method to change the refnum of an item 
	    * @param refnum
	    */
	   public void setRefnum(int refnum) {
		   this.refnum=refnum;
	   }
	   
	   /**
	    * A setter method to change the name of an item 
	    * @param name
	    */
	   public void setName(String name) {
		   this.name=name;
	   }
	   /**
	    * A setter method to change the price of an item 
	    * @param price
	    */
	   public void setPrice(double price) {
		   this.price=price;
	   }
	   /**
	    * A setter method to change the category of an item 
	    * @param category
	    */
	   public void setCategory(String category) {
		   this.category=category;
	   }
	   /**
	    * A setter method to change the condition of an item 
	    * @param condition
	    */
	   public void setCondition(String condition) {
		   this.cond=condition;
	   }
	   /**
	    * A setter method to change the seller/user of the person who posted the item
	    * @param user
	    */
	   public void setUser(User user) {
		   this.user=user;
	   }
	   
	   /**
	    * A setter method to change the image 
	    * @param fdata
	    */
	   public void setImg(byte[]img) {
		   this.img=img;
	   }
	   
	   
	   /**
	    * A setter method to change the type of the image 
	    * @param ftype
	    */
	   public void setFiletype(String filetype) {
		   this.filetype=filetype;
	   }
	   
	   
	   /**
	    * A setter method to change the name of the image 
	    * @param fname
	    */
	   public void setFilename(String filename) {
		   this.filename=filename;
	   }
	   /**
	    * A setter method to change the date an item was posted (in case of an error or if there's an update to the item's information)
	    * @param date
	    */
	   public void setDatePosted(LocalDate date) {
		   this.postdate=date;
       }
       
       /**
        * @param checkout the checkout to set
        */
       public void setCheckout(int checkout) {
           this.checkout = checkout;
       }
	   
	   
	   /**
	    * A method to get the string representation of an item 
	    * 
	    */
	   @Override
	   public String toString()
	   {
		   return new ToStringCreator(this)
				   .append("Refnum",this.getRefnum())
				   .append("Name",this.getName())
				   .append("Price",this.getPrice())
				   .append("Date", this.getPostedDate())
				   .append("Category",this.getCategory())
				   .append("Condition",this.getCondition()).append(System.lineSeparator())
				   .append ("Seller",this.getUser().getUsername()).toString(); 
	   }
	}
