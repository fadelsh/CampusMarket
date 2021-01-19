package com.example.campusmarket.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.campusmarket.cart.CartActivity;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.WebSockets;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;

import java.util.List;

import static com.example.campusmarket.app.AppController.TAG;

/**
 * Represents the dashboard adapter for items
 */
public class DashAdapter extends ArrayAdapter<DashItemsActivity> implements View.OnClickListener {

    private List<DashItemsActivity> ItemList;
    private Context mCtx;
    private String refnum;
    private String usernameString;

    /**
     * So while creating the object of this adapter class we need to give demolist and context.
     * The adapter is what actually puts the info into the dasboard in the format specified by the lv_rows layout.
     *
     * @param ItemList
     * @param mCtx
     */
    public DashAdapter(List<DashItemsActivity> ItemList, Context mCtx) {
        super(mCtx, R.layout.activity_lvrows2, ItemList);
        this.ItemList = ItemList;
        this.mCtx = mCtx;
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
        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.activity_lvrows2, null, true);
        listViewItem.setClickable(true);
//        listViewItem.setOnClickListener(myClickListener);

        // pulling the text views into the adapter
        TextView name = (TextView) listViewItem.findViewById(R.id.tvName);
        TextView price = (TextView) listViewItem.findViewById(R.id.tvPrice);
        TextView condition = (TextView) listViewItem.findViewById(R.id.tvCondition);
        TextView category = (TextView) listViewItem.findViewById(R.id.tvCategory);
        TextView postedDate = (TextView) listViewItem.findViewById(R.id.tvPostedDate);
        TextView user = (TextView) listViewItem.findViewById(R.id.tvSeller);
        Button btnContactSeller = (Button) listViewItem.findViewById(R.id.btnContactSeller);
        btnContactSeller.setOnClickListener(this);
        Button btnAddToCart = (Button) listViewItem.findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(this);

        // getting the specified positions for the items
        DashItemsActivity item = ItemList.get(position);

        // setting each parameter to text editable boxed
        name.setText(item.getName());
        price.setText(item.getPrice());
        condition.setText(item.getCondition());
        category.setText(item.getCategory());
        postedDate.setText(item.getPostedDate());
        user.setText(item.getUser());
        usernameString = item.getUser();
        refnum = item.getRefnum();

        //Adding the image to the page
        Bitmap imageBitmap = item.getImage();
        ImageView image = listViewItem.findViewById(R.id.imgPlaceholder);
        image.setImageBitmap(imageBitmap);

        //returning the list of items as a whole
        return listViewItem;

    }

    /**
     * Adds the item to the user's cart with the information
     * that was provided for the item on the page
     * Called once they click "Add To Cart"
     */
    public void addItem() {
        // make json object
        String url = Const.URL_CART_ADD
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
     * Handles the action on button click.
     * If button is ContactSeller, go to websockets chat page.
     * If button is AddToCart, add the selected item to cart.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContactSeller:
                Intent intent = new Intent(mCtx, WebSockets.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("seller", usernameString);
                intent.putExtra("buyer", UserActivity.loggedInUsername);
                mCtx.startActivity(intent);
                break;
            case R.id.btnAddToCart:
                addItem();
                Intent intent2 = new Intent(mCtx, CartActivity.class);
                intent2.putExtra("refnum", refnum);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
