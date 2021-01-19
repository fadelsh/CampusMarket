package com.example.campusmarket.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.campusmarket.NewPostActivity;
import com.example.campusmarket.R;
import com.example.campusmarket.UserActivity;
import com.example.campusmarket.app.AppController;
import com.example.campusmarket.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeletePost extends AppCompatActivity implements View.OnClickListener {

    private String TAG = DeletePost.class.getSimpleName();
    private ProgressDialog pDialog;
    private Button btnEditDelete;
    JSONObject objectToEdit;


    /**
     * Creates this instance of EditPost.
     *
     * @param savedInstanceState the instance of that class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        // get the item's current information from the last activity
        Intent intent = getIntent();
        String refnum = intent.getStringExtra("refnum");
//        findItemByRefnum(refnum);

        // make buttons clickable
        btnEditDelete = findViewById(R.id.btnEditDelete);
        btnEditDelete.setOnClickListener(this);
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

//    /**
//     * Finds the item json object by the given refnum
//     *
//     * @param refnum the refnum of the item
//     */
//    private void findItemByRefnum(String refnum) {
//        showProgressDialog();
//        String url = Const.URL_ITEM_FIND_REFNUM + "/" + refnum;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        hideProgressDialog();
//                        Log.d(TAG, response.toString() + " posted");
//                        // Now initialize the fields.
//                        objectToEdit = response;
//                        String imageString = null;
//                        try {
//                            imageString = objectToEdit.getString("image");
//                            bmImage = NewPostActivity.StringToBitMap(imageString);
//                            ivImage = findViewById(R.id.imgUploadImageEdit);
//                            ivImage.setImageBitmap(bmImage);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        initializeEditTextFields(objectToEdit);
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "error ");
//
//                    }
//                });
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");
//    }


    /**
     * Deletes the item that is currently being displayed
     */
    private void deletePost() {
        // declaring urls from api
        String url = Const.URL_ITEM_DELETE + "/";

        try {
            String refnum = objectToEdit.getString("refnum");
            url += refnum;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        url += ("?sessid=" + UserActivity.sessionID);

        showProgressDialog();
        // Make request for JSONObject - delete req
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");
    }

    /**
     * Waits for the user to click a button on the screen.
     * If the button is "Submit," it updates that item's info
     * If the button is "Delete," it deletes that item
     *
     * @param view Current view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditDelete:
                deletePost();
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
