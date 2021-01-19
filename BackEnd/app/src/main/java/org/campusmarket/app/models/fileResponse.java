	package org.campusmarket.app.models;
	
	/**
	 * A class to help generate a meaningful response (that has the filename,the download/view link, filetype, and the size) after uploading a file (image,doc,..etc)
	 * @author fadelalshammasi
	 *
	 */
	public class fileResponse {
	
		    private String filename;
		    private String downloadUrl;
		    private String fileType;
		    private long size;
	
		    /**
		     *  A constructor to create a file response with all the required parameters 
		     * @param filename
		     * @param downloadUrl
		     * @param fileType
		     * @param size
		     */
		    public fileResponse(String filename, String downloadUrl, String fileType, long size) {
		        this.filename = filename;
		        this.downloadUrl = downloadUrl;
		        this.fileType = fileType;
		        this.size = size;
		    }
	
		    
		    /**
		     * A getter method for the filename
		     * @return filename
		     */
		    public String getFileName() {
		        return this.filename;
		    }
	
		    /**
		     * A getter method for the url to download/view the file
		     * @return
		     */
		    public String getFileDownloadUrl() {
		        return this.downloadUrl;
		    }
		    
		    /**
		     * A getter method to get the type of the file
		     * @return fileType
		     */
		    public String getFileType() {
		        return this.fileType;
		    }
		    
		    /**
		     * A getter method to get the size of the file
		     * @return size
		     */
		    public long getSize() {
		        return size;
		    }
		    
		    /**
		     * A setter method to change the name of the file
		     * @param filename
		     */
		    public void setFileName(String filename) {
		        this.filename = filename;
		    }
	
		    /**
		     * A setter method to change the downloadUrl of the file
		     * @param downloadUrl
		     */
		    public void setFileDownloadUrl(String downloadUrl) {
		        this.downloadUrl = downloadUrl;
		    }
	
		    /**
		     * A setter method to change the type of the file
		     * @param fileType
		     */
		    public void setFileType(String fileType) {
		        this.fileType = fileType;
		    }
	
		    /**
		     * A setter method to change the size of the file 
		     * @param size
		     */
		    public void setSize(long size) {
		        this.size = size;
		    }
		}