package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AccountProfileActivity extends AppCompatActivity {

    protected String user_name;
    protected String user_name2;
    protected String user_ID;
    protected String user_email;
    protected String user_street;
    protected String user_suite;
    protected String user_city;
    protected String user_zipcode;
    protected SharedPreferences sharedPref;
    protected boolean gStatus = false;

    private static final String CHANNEL_ID      = "CHANNEL_01";
    private static final int NOTIFICATION_ID    = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);

        Intent intent = this.getIntent();

        gStatus = intent.getBooleanExtra("LoggedInUser", false);

        if (gStatus)
        {   sharedPref = getSharedPreferences("sharedPref_GoogleUser", Context.MODE_PRIVATE);   }

        user_name = intent.getStringExtra("user_name");
        user_name2 = intent.getStringExtra("user_name2");
        user_ID = intent.getStringExtra("user_ID");
        user_email = intent.getStringExtra("user_email");

        user_street = intent.getStringExtra("user_street");
        user_suite = intent.getStringExtra("user_suite");
        user_city = intent.getStringExtra("user_city");
        user_zipcode = intent.getStringExtra("user_zipcode");
        
        displayUserProfile();

        createNotificationChannel();
    }

    // Create Notification for exiting activity.
    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            //CharSequence name_2 = getString(R.string.channel_name_2);
            String description = getString(R.string.channel_description);
            //String description_2 = getString(R.string.channel_description_2);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            //NotificationChannel channel_02 = new NotificationChannel(CHANNEL_02_ID, name_2, importance);

            channel.setDescription(description);
            //channel_02.setDescription(description_2);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            //notificationManager.createNotificationChannel(channel_02);
            System.out.println("* * * CreateNotificationChannel - Finished Correctly.");
        }
    }



    // Display info on Views. 
    private void displayUserProfile() {
        TextView profileName = findViewById(R.id.profile_value_name);
        TextView profileName2 = findViewById(R.id.profile_username_value01);
        TextView profileEmail = findViewById(R.id.profile_email);
        TextView profileID = findViewById(R.id.profile_id_value);

        profileName.setText(user_name);
        profileName2.setText(user_name2);
        profileEmail.setText(user_email);
        profileID.setText(user_ID);

        //if (!gStatus)
        //{
        EditText profileStreet = findViewById(R.id.editText_street_input);
        TextView profileSuite = findViewById(R.id.EditText_suite_input);
        TextView profileCity = findViewById(R.id.editText_city_value);
        TextView profileZipcode = findViewById(R.id.editText_zipcode_value);

        profileStreet.setText(user_street);
        profileSuite.setText(user_suite);
        profileCity.setText(user_city);
        profileZipcode.setText(user_zipcode);
        //}
    }

    // this is called when button clicked for sign out.
    public void onClickSignOut(View view) {
        //System.out.println("* * * AccountProfileFunc - Sign Out Op.");

        // Start a sign out and return to login in activity.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();      // [END configure_signin]

        // [START build_client] : Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignOutClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignOutClient.signOut()
                .addOnCompleteListener(this, task -> {
                    // ...
                    System.out.println("Sign Out Process complete?? ");
                });

        // Launch main activity.
        Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("SignInAccountEmail", email);
        startActivity(intent);
    }

    public void onClickReturnToAccountList(View view)
    {
        // System.out.println("* * * ReturntoAccountList - Click *.");
        Intent intent = new Intent(this, DisplayListActivity.class);

        intent.putExtra("isSignedInUser", gStatus);

        // System.out.println("* * * GOOGLE USER PICKED??? " + gStatus);
        if (gStatus)
        {   // Save Google user updated information.
            saveGoogleUserInfo();
        }
        /* intent.putExtra("SignInName", user_name);
        intent.putExtra("SignInName2", user_name2);
        intent.putExtra("SignInAccountID", user_ID);
        intent.putExtra("SignInAccountEmail", user_email);

        intent.putExtra("user_street", user_street);
        intent.putExtra("user_suite", user_suite);
        intent.putExtra("user_city", user_city);
        intent.putExtra("user_zipcode", user_zipcode); */
        startActivity(intent);
    }

    private void saveGoogleUserInfo()
    {
        // Save here.
        SharedPreferences.Editor myEdit = sharedPref.edit();

        TextView profileName  = findViewById(R.id.profile_value_name);
        TextView profileName2 = findViewById(R.id.profile_username_value01);
        TextView profileEmail = findViewById(R.id.profile_email);
        TextView profileID    = findViewById(R.id.profile_id_value);

        EditText profileStreet  = findViewById(R.id.editText_street_input);
        TextView profileSuite   = findViewById(R.id.EditText_suite_input);
        TextView profileCity    = findViewById(R.id.editText_city_value);
        TextView profileZipcode = findViewById(R.id.editText_zipcode_value);

        myEdit.putString("input_01", profileName.getText().toString()); // Save current view input.
        myEdit.putString("input_02", profileName2.getText().toString()); // Save current view input.
        myEdit.putString("input_03", profileEmail.getText().toString()); // Save current view input.
        myEdit.putString("input_04", profileID.getText().toString()); // Save current view input.

        myEdit.putString("input_05", profileStreet.getText().toString()); // Save current view input.
        myEdit.putString("input_06", profileSuite.getText().toString()); // Save current view input.
        myEdit.putString("input_07", profileCity.getText().toString()); // Save current view input.
        myEdit.putString("input_08", profileZipcode.getText().toString()); // Save current view input.

        myEdit.apply();
        System.out.println("Account Profile - Preferences SAVED for googleUser. ");
    }

    // Save the Google User's information to upload again.
    @Override
    protected void onPause() {
        super.onPause();

        if (gStatus)
        {   // Save Google user updated information.
            saveGoogleUserInfo();
        }
        else {
            saveCurrentProfile();
        }

        produceNotification();

        System.out.println("* * * onPause State Reached");
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCurrentProfile();
    }

    private void loadCurrentProfile() {
        // Load the current profile.
        if (gStatus)
        {
            // Load GoogleUser profile.
        }
        else
        {
            // regular user profile.
        }

    }

    private void saveCurrentProfile()
    {
        SharedPreferences.Editor givenEdit = sharedPref.edit();

        givenEdit.putBoolean("gStatus", gStatus);

        givenEdit.putString("cur_user_name",  user_name);
        givenEdit.putString("cur_user_name2", user_name2);
        givenEdit.putString("cur_user_email", user_email);
        givenEdit.putString("cur_user_id",    user_ID);

        givenEdit.putString("cur_user_street",  user_street);
        givenEdit.putString("cur_user_suite",   user_suite);
        givenEdit.putString("cur_user_city",    user_city);
        givenEdit.putString("cur_user_zipcode", user_zipcode);

        givenEdit.apply();
    }

    private void produceNotification()
    {
        Intent returnIntent = new Intent(this, AccountProfileActivity.class);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, returnIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reminder Notification")
                .setContentText("Forget me not Message ... ")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        System.out.println("* * * onClick 01 - Version 01");
    }
}