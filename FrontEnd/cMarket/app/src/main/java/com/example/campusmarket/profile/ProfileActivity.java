package com.example.campusmarket.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity that represents the user's profile.
 * Has a button to go settings.
 * Shows all of the items this user has posted and can click on them
 * to go to the edit page.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button accSettings;
    private String TAG = ProfileActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String tag_json_arry = "jarray_req";
    private LinearLayout item_layout;
    private ArrayList<JSONObject> myItems = new ArrayList<>();
    private ArrayList<Button> myButtons = new ArrayList<>();

    /**
     * Creates this instance of ProfileActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        accSettings = findViewById(R.id.btnAccSettings);
        accSettings.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        item_layout = findViewById(R.id.profile_item_layout);
        item_layout.setOrientation(LinearLayout.VERTICAL);

        // start the request and create the buttons
        showSoldItemsProfile();

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Displays all of the items that this user has sold by making a request
     * to the url "items/seller/their_username"
     */
    private void showSoldItemsProfile() {
        String url = Const.URL_ITEM_SELLER + "/" + UserActivity.loggedInUsername;
        showProgressDialog();

        // make the request
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        addItemNames(response);
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
    }

    /**
     * Parse the JSON item array so you only add the item names.
     *
     * @param response
     */
    private void addItemNames(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                String theString = obj.getString("name");
                myItems.add(obj);

                // create a new Button
                Button theButton = new Button(this);
                theButton.setText(theString);
                item_layout.addView(theButton);
                myButtons.add(theButton);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        createListeners(myButtons, myItems);
    }


    /**
     * Create a listener for each button and goes to a new activity when it is clicked
     *
     * @param buttons
     * @param items
     */
    private void createListeners(ArrayList<Button> buttons, ArrayList<JSONObject> items) {
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            final JSONObject o = items.get(i);
            b.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, EditPost.class);
                    //String message = o.toString();
                    String refnum = "";
                    try {
                        refnum = o.getString("refnum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("refnum", refnum);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * When the user clicks on the Account Settings button, brings the user to that activity
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAccSettings) {
            startActivity(new Intent(ProfileActivity.this,
                    AccountSettingsActivity.class));
        }
    }
}
