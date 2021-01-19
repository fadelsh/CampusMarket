package com.example.campusmarket.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.campusmarket.MainActivity;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * Activity that represents the user's personal account settings, linked from Profile.
 */
public class AccountSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = AccountSettingsActivity.class.getSimpleName();
    private Button deleteAcc;
    private ProgressDialog pDialog;

    /**
     * Creates this instance of AccountSettings
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        String welcomeMessage = UserActivity.loggedInUsername + "'s Profile";
        TextView welcome = findViewById(R.id.tvAccSettingsWelcome);
        welcome.setText(welcomeMessage);
        deleteAcc = findViewById(R.id.btnDeleteAccount);
        deleteAcc.setOnClickListener(this);
    }

    /**
     * Shows the progress dialog while it's loading
     */
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    /**
     * Hides the progress dialog
     */
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Finds the ID of the user with the given username.
     * Once it finds it, it calls deleteAccount() to delete the user.
     *
     * @param username the username of the user to be deleted
     */
    private void findByID(String username) {
        String url = Const.URL_USER_USERNAME + "/" + username + "?sessid=" + UserActivity.sessionID;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString() + " found");
                        hideProgressDialog();
                        // we found the user, now delete it from the DB
                        try {
                            deleteAccount(response.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");
    }

    /**
     * Deletes the account of the user with the id
     *
     * @param ID the ID of the user to delete
     */
    private void deleteAccount(String ID) {
        String url = Const.URL_USER_DELETE + "/" + ID + "?sessid=" + UserActivity.sessionID;
        //to delete cart item  "refnum"
        showProgressDialog();
        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString() + " deleted");
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR IN DELETE ACCOUNT ");
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
                hideProgressDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");

        // Account is delete, go back to main page
        startActivity(new Intent(AccountSettingsActivity.this,
                MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnDeleteAccount) {
            findByID(UserActivity.loggedInUsername);
        }

    }
}
