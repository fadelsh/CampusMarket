	package org.campusmarket.app.websockets;
	
	import java.io.IOException;
	import java.util.HashMap;
	import java.util.Map;
	
	import javax.websocket.OnClose;
	import javax.websocket.OnError;
	import javax.websocket.OnMessage;
	import javax.websocket.OnOpen;
	import javax.websocket.Session;
	import javax.websocket.server.PathParam;
	import javax.websocket.server.ServerEndpoint;
	
	import org.apache.commons.logging.Log;
	import org.apache.commons.logging.LogFactory;
	import org.campusmarket.app.models.User;
	import org.campusmarket.db.repositories.SessionsRepository;
	import org.campusmarket.db.repositories.UsersRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.stereotype.Component;
	import org.springframework.web.server.ResponseStatusException;
	
	@ServerEndpoint(value = "/chat/{sessid}", configurator = CustomConfigurator.class)
	@Component
	public class WebSocketServer
	{
	    private static Map<Session, String> sessionUsernameMap = new HashMap<>();
		private static Map<String, Session> usernameSessionMap = new HashMap<>();
		
		@Autowired
		SessionsRepository sessions;
	
		@Autowired
		UsersRepository users;
	    
	    Log logger = LogFactory.getLog(WebSocketServer.class);
	    
	    @OnOpen
	    public void onOpen(Session session, 
	    	      		   @PathParam("sessid") String sessid) throws IOException 
	    {
			logger.info("Entered into Open");
			
			if (sessid.isEmpty())
	        {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Invalid: Empty value for required parameter 'sessid'.");
			}
			
			logger.info("Finding active session.....");
			org.campusmarket.app.models.Session active = sessions.findBySessId(sessid);
			logger.info("Session found.");
	
			if (active == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an active session with id: " + sessid);
	
	
	
			User loggedIn = users.findById(sessions.findUserBySession(sessid));
	        logger.info("Creating HashMaps......");
	        sessionUsernameMap.put(session, loggedIn.getUsername());
	        usernameSessionMap.put(loggedIn.getUsername(), session);
			
			logger.info("HashMaps created.");
	        String message="User:" + loggedIn.getUsername() + " has Joined the Chat";
	        	broadcast(message);
			
	    }
	 
	    @OnMessage
	    public void onMessage(Session session, String message) throws IOException 
	    {
	        // Handle new messages
	    	logger.info("Entered into Message: Got Message:"+message);
	    	String username = sessionUsernameMap.get(session);
	    	
	    	if (message.startsWith("@")) // Direct message to a user using the format "@username <message>"
	    	{
	    		String destUsername = message.split(" ")[0].substring(1); // don't do this in your code!
	    		sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
	    		sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
	    	}
	    	else // Message to whole chat
	    	{
		    	broadcast(username + ": " + message);
	    	}
	    }
	 
	    @OnClose
	    public void onClose(Session session) throws IOException
	    {
	    	logger.info("Entered into Close");
	    	
	    	String username = sessionUsernameMap.get(session);
	    	sessionUsernameMap.remove(session);
	    	usernameSessionMap.remove(username);
	        
	    	String message= username + " disconnected";
	        broadcast(message);
	    }
	 
	    @OnError
	    public void onError(Session session, Throwable throwable) 
	    {
	        // Do error handling here
			logger.info("Entered into Error");
			logger.error(throwable.getMessage(), throwable);
	    }
	    
		private void sendMessageToPArticularUser(String username, String message) 
	    {	
	    	try {
	    		usernameSessionMap.get(username).getBasicRemote().sendText(message);
	        } catch (IOException e) {
	        	logger.info("Exception: " + e.getMessage().toString());
	            e.printStackTrace();
	        }
	    }
	    
	    private static void broadcast(String message) 
	    	      throws IOException 
	    {	  
	    	sessionUsernameMap.forEach((session, username) -> {
	    		synchronized (session) {
		            try {
		                session.getBasicRemote().sendText(message);
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    });
		}
	
	}