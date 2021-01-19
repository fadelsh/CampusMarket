package com.example.campusmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.campusmarket.dashboard.DashboardActivity;
import com.example.campusmarket.profile.ProfileActivity;

/**
 * Activity that represents the page after a user logs in / signs up
 */
public class UserActivity extends Activity implements OnClickListener {
    private Button btnDashboard, btnNewPost, btnProfile;
    public static String loggedInUsername;
    public static String sessionID = "";

    /**
     * Creates this instance of UserActivity.
     * Display's "Welcome, Username" where username is from previous activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Stores the  user's username
        Intent intent = getIntent();
        loggedInUsername = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String sessMessage = intent.getStringExtra("sessionID");
        if (sessMessage != null) {
            sessionID = intent.getStringExtra("sessionID");
        }


        Log.d("This is the sessionID: ", sessionID);
//        String firstLogIn =  intent.getStringExtra("firstLogIn");
//        if (firstLogIn != null)
//        {
//            startActivity(new Intent(UserActivity.this,WebSockets.class));
//            finish();
//        }

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.usernameWelcome);
        textView.setText(loggedInUsername);

        btnDashboard = (Button) findViewById(R.id.btnGoToDashboard);
        btnDashboard.setOnClickListener(this);
        btnNewPost = (Button) findViewById(R.id.btnNewPost);
        btnNewPost.setOnClickListener(this);
        btnProfile = (Button) findViewById(R.id.btnGoToProfile);
        btnProfile.setOnClickListener(this);
    }

    /**
     * Sees which button the user is going to click.
     * Almost acts as a navbar
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGoToDashboard:
                startActivity(new Intent(UserActivity.this,
                        DashboardActivity.class));
                break;
            case R.id.btnNewPost:
                startActivity(new Intent(UserActivity.this,
                        NewPostActivity.class));
                break;
            case R.id.btnGoToProfile:
                startActivity(new Intent(UserActivity.this,
                        ProfileActivity.class));
                break;
            default:
                break;
        }
    }
}
