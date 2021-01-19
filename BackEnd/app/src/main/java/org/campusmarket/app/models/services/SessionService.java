package org.campusmarket.app.models.services;

import java.util.List;

import org.campusmarket.app.models.Session;
import org.campusmarket.app.models.User;
import org.campusmarket.db.repositories.SessionsRepository;
import org.campusmarket.db.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service class to help testing the mockito for users
 * @author nehisler
 *
 */
@Service
public class SessionService
{
    @Autowired
    SessionsRepository repo;

    @Autowired
    UsersRepository users;

    public List<Session> getAllSessions()
    {
        return repo.findAll();
    }
    
    public User findUserBySessID(String sessid)
    {
        return users.findById(repo.findUserBySession(sessid));
    }

    public List<Session> findAllByUserId(int user_id)
    {
        return repo.findAllByUserId(user_id);
    }

    public Session save(Session s)
    {
        return repo.save(s);
    }
}