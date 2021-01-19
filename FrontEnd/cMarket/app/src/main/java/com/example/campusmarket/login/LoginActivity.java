package com.example.campusmarket.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.campusmarket.MainActivity;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.WebSockets;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity that lets a user log in to their account
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = LoginActivity.class.getSimpleName();
    private AwesomeValidation awesomeValidation;
    private ProgressDialog pDialog;
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    TextView registerLink, valid_user;

    /**
     * Creates this instance on Login
     *
     * @param savedInstanceState the Saved Instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize instance variables
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.bLogin);
        registerLink = findViewById(R.id.tvRegisterHere);
        valid_user = findViewById(R.id.log_in_response);

        // initializing awesomeValidation library
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.etUsername, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.userNameError);
        // Password regex: at least one lower case, one upper case, and one number - must be at least 8 characters long
        awesomeValidation.addValidation(this, R.id.etPassword, "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$", R.string.passwordError);

        btnLogin.setOnClickListener(this);

        // creating a new intent to handle page redirection to register for the app when link is clicked
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });
    }

    /**
     * Shows progress dialog during request
     */
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    /**
     * Hides progress dialog during request
     */
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Checks if the username and password fields meet our syntax requirements
     *
     * @return True if syntax is valid, False is syntax is invalid
     */
    public boolean validateForm() {
        return awesomeValidation.validate();
    }

    /**
     * When the user tries to click "Log In", make sure the fields are valid
     * If syntax is valid, check if the user exists in the DB
     * If syntax is not valid, tell the user what they need to change
     *
     * @param view the View
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bLogin) {
            if (validateForm()) {
                createSession();
            }
        }
    }

    /**
     * Creates a new session based on the user's username & password.
     */
    public void createSession() {
        showProgressDialog();
        final String incorrect = "Username / Password incorrect";
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
                        hideProgressDialog();
                        Log.d(TAG, response.toString() + " posted");
                        String sessionID = "";
                        try {
                            sessionID = response.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        valid_user.setText("");
                        finishLogIn((etUsername.getText()).toString(), sessionID);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
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
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    valid_user.setText(incorrect);
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
     * Starts the new UserActivity and passes the username.
     * Called when the log in has valid syntax and username / password is correct.
     *
     * @param username User's username
     */
    public void finishLogIn(String username, String sessionID) {
        Intent intent = new Intent(this, WebSockets.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        intent.putExtra("firstLogIn", "true");
        intent.putExtra("sessionID", sessionID);
        startActivity(intent);
    }
}
