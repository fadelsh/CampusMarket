package com.example.campusmarket;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusmarket.login.LoginActivity;
import com.example.campusmarket.login.RegisterActivity;

/**
 * Activity that represents the main page / welcome page / first page you go to when you open the app
 */
public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.example.campusmarket.MESSAGE";
    public String test;
    public static final String DIRECT_MESSAGE_CHANNEL_ID = "directMessageChannel";
    static int notificationId = 1;
    static NotificationManager notificationManager;


    /**
     * Starting the notification channel
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            //String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel dmChannel = new NotificationChannel(DIRECT_MESSAGE_CHANNEL_ID,
                    "Direct Message", importance);
            dmChannel.setDescription("Channel for any direct message");
            dmChannel.setLightColor(Color.GREEN);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(dmChannel);

        }
    }

    /**
     * Creates instance of MainActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
    }

    /**
     * Called when the user clicks to log in.
     * Brings user to LoginActivity page
     *
     * @param view
     */
    public void logIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks to sign up.
     * Brings user to RegisterActivity page
     *
     * @param view
     */
    public void signUp(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Called when user clicks on "Test"
     * Brings user to page after Login/Register
     * For testing purposes only
     *
     * @param view
     */
    public void testButton(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    /**
     * Function to test Mockito.
     * For testing purposes only
     *
     * @param s
     * @return the  string
     */
    public String testMockitoFunction(String s) {
        test = s;
        return s.toLowerCase();
    }

}
