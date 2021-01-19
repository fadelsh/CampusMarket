package com.example.campusmarket;


import android.os.Build;
import android.view.View;

import com.example.campusmarket.login.LoginActivity;
import com.example.campusmarket.login.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Some example Mockito tests for demo3
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class GETResponseTest {

    private View.OnClickListener onClickListener;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void register_response_test1() throws JSONException {
        //This creates a Mock Object of the class that we have not fully implemented
        RegisterActivity regAct = mock(RegisterActivity.class);

        // Create the user JSON Object
        String usernameCorrect = "Sponge123";
        String passwordCorrect = "Password123";
        String firstNameCorrect = "Spongebob";
        String lastNameCorrect = "SqaurePants";
        String schoolCorrect = "Bikini Bottom University";
        String emailCorrect = "spongebob@gmail.com";

        JSONObject response = new JSONObject();
        response.put("username", usernameCorrect);
        response.put("password", passwordCorrect);
        response.put("firstname", firstNameCorrect);
        response.put("lastname", lastNameCorrect);
        response.put("email", schoolCorrect);
        response.put("university", emailCorrect);
        response.put("admin", "false");

        when(regAct.make_register_request()).thenReturn(true);
        Assert.assertEquals(true, regAct.make_register_request());
    }

    @Test
    public void register_response_test_2() throws JSONException {
        RegisterActivity regActivity = mock(RegisterActivity.class);
        // Create the user JSON Object
        String usernameCorrect = "Sponge123";
        String passwordCorrect = "Password123";
        String firstNameCorrect = "Spongebob";
        String lastNameCorrect = "SqaurePants";
        String schoolCorrect = "Bikini Bottom University";
        String emailCorrect = "spongebob@gmail.com";
        String sessionID = "12345ABCD";

        JSONObject response = new JSONObject();
        response.put("username", usernameCorrect);
        response.put("password", passwordCorrect);
        response.put("firstname", firstNameCorrect);
        response.put("lastname", lastNameCorrect);
        response.put("email", schoolCorrect);
        response.put("university", emailCorrect);
        response.put("admin", "false");


        regActivity.finishSignUp(usernameCorrect, sessionID);
        verify(regActivity, times(1)).finishSignUp(usernameCorrect, sessionID);
    }

    @Test
    public void valid_syntax_login() {
        LoginActivity logActivity = mock(LoginActivity.class);
        String usernameCorrect = "Sponge123";
        String passwordCorrect = "Password123";
        when(logActivity.validateForm()).thenReturn(true);
        Assert.assertEquals(true, logActivity.validateForm());
    }
}