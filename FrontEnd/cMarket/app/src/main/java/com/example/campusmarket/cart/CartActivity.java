package com.example.campusmarket.cart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.campusmarket.app.AppController.TAG;

public class CartActivity extends AppCompatActivity  {

    private String TAG = CartActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String tag_json_arry = "jarray_req";

    //    Button btnViewCart;
    ListView listView;
    Activity activity;
    List<CartItemsActivity> CartList;

    /**
     * Creates this instance of Dashboard
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        listView = findViewById(R.id.listView);
        CartList = new ArrayList<>();

        makeJsonArryReq();

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
     * Making json array request post
     */
    private void makeJsonArryReq() {
        showProgressDialog();
        String url = Const.URL_CART_ALL + "?sessid=" + UserActivity.sessionID;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        // calling add names function that handles displaying new items to the dashboard
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

                // declaring new json object
                JSONObject demoObject = response.getJSONObject(i);
                // declaring what parameters will be added
                String s = demoObject.getString("user");
                JSONObject seller = new JSONObject(s);
                CartItemsActivity item = new CartItemsActivity(demoObject.getString("name"),
                        demoObject.getString("price"), seller.getString("username"), demoObject.getString("refnum"));
                CartList.add(item); // adding all of these new items for display

                // setting up new adapter that will place items accordingly
                final CartAdapter adapter = new CartAdapter(CartList, getApplicationContext());

                // actually calling the adapter
                listView.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            // this line is the key --> we use the adapter to render the item names in their own sections individually
//            adapter.notifyDataSetChanged();
        }
//        msgResponse.setText(message); --> // we no longer want the whole message to display since items are not their own entities
    }

}
