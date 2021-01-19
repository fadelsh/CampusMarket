package com.example.campusmarket.cart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.dashboard.DashboardActivity;
import com.example.campusmarket.utils.Const;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.example.campusmarket.app.AppController.TAG;

public class CartAdapter extends ArrayAdapter<CartItemsActivity> implements View.OnClickListener {

    private List<CartItemsActivity> CartList;
    private Context mCtx;
    private Button btnRemove;
    private Button btnClear;
    private String refnum, price, name, seller;
    private ProgressDialog pDialog;


    /** These methods need to be public since
     *  they are referenced in other classes
     *
     */

    /**
     * So while creating the object of this adapter class we need to give demolist and context.
     * The adapter is what actually puts the info into the dashboard in the format specified by the lv_rows layout.
     *
     * @param CartList
     * @param mCtx
     */
    public CartAdapter(List<CartItemsActivity> CartList, Context mCtx) {
        super(mCtx, R.layout.activity_cartrows, CartList);
        this.CartList = CartList;
        this.mCtx = mCtx;
        pDialog = new ProgressDialog(mCtx);
        pDialog.setMessage("Loading...");

    }

    /**
     * Returns the list of items
     *
     * @param position
     * @param convertView
     * @param parent
     * @return the View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        // aids in specifically placing items
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        // creating a view with our xml layout
        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.activity_cartrows, null, true);
        listViewItem.setClickable(true);
//        listViewItem.setOnClickListener(myClickListener);

        // pulling the text views into the adapter
        TextView name = (TextView) listViewItem.findViewById(R.id.tvName);
        TextView price = (TextView) listViewItem.findViewById(R.id.tvCategory);
//        TextView condition = (TextView) listViewItem.findViewById(R.id.tvCondition);
//        TextView category = (TextView) listViewItem.findViewById(R.id.tvCategory);
        TextView user = (TextView) listViewItem.findViewById(R.id.tvSeller);
        btnRemove = (Button) listViewItem.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);
        btnClear = (Button) listViewItem.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        Button checkout = listViewItem.findViewById(R.id.btnCheckOut);
        checkout.setOnClickListener(this);
        Button received = listViewItem.findViewById(R.id.btnItemReceived);
        received.setOnClickListener(this);

        // getting the specified positions for the items
        CartItemsActivity item = CartList.get(position);

        // setting each parameter to text editable boxed
        name.setText(item.getName());
        price.setText(item.getPrice());
        this.price = item.getPrice();
        this.name = item.getName();
        Log.d(TAG, "WATCHING ITEM: " + this.name);
        this.seller = item.getUser();
        user.setText(item.getUser());
        this.refnum = item.getRefnum();

        //returning the list of items as a whole
        return listViewItem;

    }

    //making this public for testing
    /**
     * Removes the item from the user's cart with the information
     * that was provided for the item on the page
     * Called once they click "Remove from Cart"
     */
    public void removeItem() {
        // make json object
        String url = Const.URL_CART_DELETE
                + "/" + refnum + "?sessid=" + UserActivity.sessionID;

        // Make post request for JSONObject using the url:
        StringRequest stringReq = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {


        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, "jobj_req");

    }


    /**
     * Clears all of the items from user's cart, including all
     * the information that was provided for the items on the page
     * Called once they click "Clear Cart"
     */
    public void clearItems() {
        // make json object
        String url = Const.URL_CART_CLEAR
                + "?sessid=" + UserActivity.sessionID;

        // Make post request for JSONObject using the url:
        StringRequest stringReq = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {


        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, "jobj_req");

    }

    private void checkOutRequest()
    {
        //showProgressDialog();
        String url  = Const.URL_CART_CHECKOUT + "/" + refnum + "?sessid=" + UserActivity.sessionID;
        StringRequest stringReq = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hideProgressDialog();
                        Log.d(TAG, response.toString() + " success, check out");
                        finishCheckout();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //hideProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, "jobj_req");
    }

    private void finishCheckout()
    {
        // go on to the next activity
        Intent intent = new Intent(mCtx, CheckOutActivity.class);
        intent.putExtra("sellers", seller);
       intent.putExtra("itemNames", name);
        intent.putExtra("total", price);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(intent);
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

    private void itemReceivedRequest() {
        //showProgressDialog();
        String url  = Const.URL_GOT_ITEM + "/" + refnum + "?sessid=" + UserActivity.sessionID;
        StringRequest stringReq = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hideProgressDialog();
                        Log.d(TAG, response.toString() + " success, item received ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error in item received");
                if (error == null) {
                    //hideProgressDialog();
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
                } catch (UnsupportedEncodingException e) {
                    // exception
                }

                Log.d(TAG, "ERROR BODY: " + body);
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, "jobj_req");
    }

    /**
     * Handles the action on button click.
     * If button is Remove, call removeItem function
     * and remove one item from cart.
     * If button is Clear, call clearItems fucntion
     * and remove all from cart.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRemove:
                removeItem();
                Intent intent = new Intent(mCtx, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
                break;
            case R.id.btnClear:
                clearItems();
                Intent intent2 = new Intent(mCtx, DashboardActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent2);
                break;
            case R.id.btnCheckOut:
                checkOutRequest();
                break;
            case R.id.btnItemReceived:
                itemReceivedRequest();
                break;
            default:
                break;

        }
    }
}

