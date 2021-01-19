package org.campusmarket.app;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.when;

import org.campusmarket.app.models.Session;
import org.campusmarket.app.models.User;
import org.campusmarket.app.models.services.SessionService;
import org.campusmarket.db.repositories.SessionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestSessionsService extends TestServices
{
    @InjectMocks
    SessionService sessionService;

    @Test // test 3 - NH - Demo5
    public void TestNewSession()
    {
        User sellerOne=new User ("nheisler", "abc123","Nick","Heisler","nheisler@iastate.edu","isu", false);
        Session sess = new Session("123456789abcdefg", false);

        SessionsRepo.save(sess);
        if (sess != null && sellerOne != null) sellerOne.addSession(sess);
        Session s = new Session();

        for (Iterator<Session> it = sellerOne.getSessions().iterator(); it.hasNext();)
        {
            s = it.next();
            if (sess.equals(s)) break;
        }

        assertEquals(s, sess);
    }
}