package com.example.campusmarket.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.campusmarket.MainActivity;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.WebSockets;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

// Recommended validation library:
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

/**
 * Activity that lets users create a new account
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = RegisterActivity.class.getSimpleName();
    private AwesomeValidation awesomeValidation;

    EditText etFirstName;
    EditText etLastName;
    EditText etUsername;
    EditText etPassword;
    EditText etUniversity;
    EditText etEmail;
    Button bRegister;

    /**
     * Creates this instance of RegisterActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etUniversity = findViewById(R.id.etUniversity);
        etEmail = findViewById(R.id.etEmail);

        bRegister = findViewById(R.id.bRegister);

        // initializing awesomeValidation library
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // assigning regex expressions for each login field
        awesomeValidation.addValidation(this, R.id.etFirstName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.firstNameError);
        awesomeValidation.addValidation(this, R.id.etLastName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lastNameError);
        awesomeValidation.addValidation(this, R.id.etUsername, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.userNameError);
        // Password regex: at least one lower case, one upper case, and one number - must be at least 8 characters long
        awesomeValidation.addValidation(this, R.id.etPassword, "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$", R.string.passwordError);
        awesomeValidation.addValidation(this, R.id.etUniversity, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.universityError);
        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.emailError);
        // For mobile number in future: awesomeValidation.addValidation(this, R.id.etPhone, "^[2-9]{2}[0-9]{8}$", R.string.mobileError);


        bRegister.setOnClickListener(this);

    }

    /**
     * Checks if the validation is successful. If it is, then it calls finishSignUp()
     *
     * @return true if validation was successful, false otherwise
     */
    public boolean validateForm() {
        // first validate the form, then move ahead
        // if this becomes true, validation is successful
        if (awesomeValidation.validate()) {
            // now make the user info into json object & push to database.
            // then, send the user to the "start a json request" page
//            finishSignUp();
            make_register_request();
        }
        return awesomeValidation.validate();

    }

    /**
     * When the user clicks to register, calls validateForm() to make sure syntax is legal
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == bRegister) {
            // call the validateForm function to finish user sign up and load the next page
            validateForm();
        }
    }

    /**
     * Posts the use to the database
     *
     * @return successful status
     */
    public boolean make_register_request() {

        final JSONObject js = new JSONObject();
        try {
            js.put("username", (etUsername.getText()).toString());
            js.put("password", (etPassword.getText()).toString());
            js.put("firstname", (etFirstName.getText()).toString());
            js.put("lastname", (etLastName.getText()).toString());
            js.put("email", (etEmail.getText()).toString());
            js.put("university", (etUniversity.getText()).toString());
            js.put("admin", "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final boolean[] success = {false};
        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Const.URL_NEW_USER, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        success[0] = true;
                        Log.d(TAG, response.toString() + " posted");
                        createSession();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success[0] = false;
                Log.d(TAG, "Error: " + error.getMessage());
                createSession();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("username", js.getString("username"));
                    params.put("password", js.getString("password"));
                    params.put("firstname", js.getString("firstname"));
                    params.put("lastname", js.getString("lastname"));
                    params.put("email", js.getString("email"));
                    params.put("university", js.getString("university"));
                    params.put("admin", "false");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");

        return success[0];

    }

    /**
     * Creates a new session based on the user's username & password.
     */
    public void createSession() {
        JSONObject js = new JSONObject();
        try {
            js.put("username", (etUsername.getText()).toString());
            js.put("password", (etPassword.getText()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Const.URL_SESSION_NEW, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString() + " posted");
                        String sessionID = "";
                        try {
                            sessionID = response.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finishSignUp((etUsername.getText()).toString(), sessionID);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR IN CREATE SESSION ");
                if (error == null) {
                    Log.d(TAG, "ERROR is null ");
                    return;
                }
                if (error.networkResponse == null) {
                    Log.d(TAG, "ERROR network response is null");
                    return;
                }
                String body = "";
                //get status code here
                //final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }

                Log.d(TAG, "ERROR BODY: " + body);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", (etUsername.getText()).toString());
                params.put("password", (etPassword.getText()).toString());
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");
    }


    /**
     * Goes to the next intent
     */
    public void finishSignUp(String username, String sessionID) {
        // sending user to the next page by creating a new intent
        Intent intent = new Intent(this, WebSockets.class);
        EditText editText = (EditText) findViewById(R.id.etUsername);
        String message = editText.getText().toString();
        // stores username and displays it in a welcome message on the next page;
        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
        intent.putExtra("sessionID", sessionID);
        intent.putExtra("firstLogIn", "true");
        startActivity(intent);
    }
}