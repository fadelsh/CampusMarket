	package org.campusmarket.db.repositories;
	
	import java.util.ArrayList;
	
	import org.campusmarket.app.models.Session;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.query.Param;
	import org.springframework.stereotype.Repository;
	import org.springframework.transaction.annotation.Transactional;
	
	/**
	 * An interface for session to represent the SessionsRepository. It that has the declarations of the methods that we use in the controller with some customized queries 
	 * 
	 * @author nheisler 
	 *
	 */
	@Repository
	public interface SessionsRepository extends JpaRepository<Session, String>
	{
		/**
		 * A method to find the session that has a specific session id 
		 * @param sessid
		 * @return session 
		 */
	    Session findBySessId(@Param("sessid") String sessid);
	
	    /**
	     * A method to get the user id given  the session id 
	     * @param sessid
	     * @return user id found by session 
	     */
	    @Query(nativeQuery = true, value="SELECT * FROM user_sessions where sess_id=:sess_id")
	    @Transactional(readOnly = true)
	    int findUserBySession(@Param("sess_id") String sessid);
	
	    /**
	     * A method to get all active sessions for a user given the user id
	     * @param user_id
	     * @return an arraylist of session
	     */
	    @Query(nativeQuery = true, value =  "SELECT * FROM user_sessions WHERE user_id=:user_id")
	    @Transactional(readOnly = true)
	    ArrayList<Session> findAllByUserId(@Param("user_id") int user_id);
	}