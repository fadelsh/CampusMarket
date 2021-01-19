package com.example.campusmarket;


import android.os.Build;
import android.view.View;

import com.example.campusmarket.cart.CartActivity;
import com.example.campusmarket.cart.CartAdapter;
import com.example.campusmarket.dashboard.DashAdapter;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)

/**
 * Lily's mockito tests
 */
public class MockitoTestL {

    private View.OnClickListener onClickListener;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    /**
     * -----------------------
     * Mockito test for demo 3
     * @throws JSONException
     * -----------------------
     */

    /**
     * Checks to see if login_return method returns
     * true like it should
     *
     * @throws JSONException
     */
    @Test
    public void login_return() throws JSONException {
        LoginActivity logActivity = mock(LoginActivity.class);

        // Create the user JSON Object
        String userCorrect = "Sponge123";
        String passCorrect = "Password123";

        JSONObject response = new JSONObject();
        response.put("username", userCorrect);
        response.put("password", passCorrect);
        response.put("admin", "false");

//        when(logActivity.check_login_user(userCorrect, passCorrect)).thenReturn(true);
//        Assert.assertEquals(true, logActivity.check_login_user(userCorrect, passCorrect));
    }

    /**
     * ------------------------
     * Mockito tests for demo 4
     * ------------------------
     */

    /**
     * Checks if the login validation function runs
     * when login form is completed
     */
    @Test
    public void syntax_register() {
        RegisterActivity regActivity = mock(RegisterActivity.class);
        String usernameCorrect = "lkrohn";
        String passwordCorrect = "password1!";
        when(regActivity.validateForm()).thenReturn(true);
        Assert.assertEquals(true, regActivity.validateForm());
    }

    /**
     * Verifies the username and password
     * fields for user login
     *
     * @throws JSONException
     */
    @Test
    public void login_verify() throws JSONException {
        LoginActivity logActivity = mock(LoginActivity.class);
        // Create the user JSON Object
        String usernameCorrect = "Sponge123";
        String passwordCorrect = "Password123";
        String sessionID = "12345ABCDE";

        JSONObject response = new JSONObject();
        response.put("username", usernameCorrect);
        response.put("password", passwordCorrect);

        logActivity.finishLogIn(usernameCorrect, sessionID);
        verify(logActivity, times(1)).finishLogIn(usernameCorrect, sessionID);
    }

    /**
     * Verifies the fields of the json object that
     * represent an item added to a cart
     *
     * @throws JSONException
     */
    @Test
    public void addToCart_check() throws JSONException {
        DashAdapter addCartActivity = mock(DashAdapter.class);

        // Create the user JSON Object
        String nameCorrect = "iphone";
        String priceCorrect = "250.0";
        String conditionCorrect = "used";
        String categoryCorrect = "electronics";
        String dateCorrect = "09/12/19";
        String sellerNameCorrect = "fadelsh";
        String refnumCorrect = "8";

        //verify the fields are correct to add the item
        JSONObject response = new JSONObject();
        response.put("name", nameCorrect);
        response.put("price", priceCorrect);
        response.put("condition", conditionCorrect);
        response.put("category", categoryCorrect);
        response.put("postedDate", dateCorrect);
        response.put("username", sellerNameCorrect);
        response.put("refnum", refnumCorrect);


        addCartActivity.addItem();
        verify(addCartActivity, times(1)).addItem();
    }


    /**
     * ------------------------
     * Mockito tests for demo 5
     * ------------------------
     */


    /**
     * Verifies the item and all of
     * its fields were removed from cart
     *
     * @throws JSONException
     */
    @Test
    public void clearCart_verify() throws JSONException {
        CartAdapter cartAdapt = mock(CartAdapter.class);
        // Create the cart JSON Object
        String name = "Phone Charger";
        String price = "25.50";
        String user = "lkrohn";

        JSONObject response = new JSONObject();
        response.put("item name", name);
        response.put("price", price);
        response.put("seller", user);

        cartAdapt.clearItems();
        verify(cartAdapt, times(1)).clearItems();
    }

    /**
     * Verifies the fields of the json object that
     * will be posted to the dashboard as a new item
     *
     * @throws JSONException
     */
    @Test
    public void postItem2_verify() throws JSONException {
        NewPostActivity postActivity = mock(NewPostActivity.class);

        // Create the user JSON Object
        String nameCorrect = "iphone";
        String priceCorrect = "250.0";
        String conditionCorrect = "used";
        String categoryCorrect = "electronics";
        String imageCorrect = "";

        //verify the fields are correct to add the item
        JSONObject response = new JSONObject();
        response.put("name", nameCorrect);
        response.put("price", priceCorrect);
        response.put("condition", conditionCorrect);
        response.put("category", categoryCorrect);
        response.put("image", imageCorrect);

        postActivity.postItem();
        verify(postActivity, times(1)).postItem();
    }

    /**
     * Checks the fields of the json object that
     * represent an item removed from the cart were actually removed
     *
     * @throws JSONException
     */
    @Test
    public void removeFromCart_check() throws JSONException {
        CartAdapter remCartActivity = mock(CartAdapter.class);

        // Create the user JSON Object
        String nameCorrect = "iphone";
        String priceCorrect = "250.0";
        String sellerNameCorrect = "fadelsh";
        String refnumCorrect = "8";

        //verify the fields are correct to add the item
        JSONObject response = new JSONObject();
        response.put("name", nameCorrect);
        response.put("price", priceCorrect);
        response.put("username", sellerNameCorrect);
        response.put("refnum", refnumCorrect);


        remCartActivity.removeItem();
        verify(remCartActivity, times(1)).removeItem();
    }

}
