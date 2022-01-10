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

    protected SharedPreferences googleUserData; // = getSharedPreferences("googleUserData", Context.MODE_PRIVATE);
    protected SharedPreferences currentUserData; // = getSharedPreferences("profileDisplayData", Context.MODE_PRIVATE);

    protected boolean isGoogleUser = false;   // Identifier for Signed in Account.
    protected boolean validNotify = true;     // Notification Pop up control parameter

    private static final String CHANNEL_ID = "CHANNEL_01";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);

        googleUserData = getSharedPreferences("googleUserData", Context.MODE_PRIVATE);
        currentUserData = getSharedPreferences("profileDisplayData", Context.MODE_PRIVATE);

        Intent intent = this.getIntent();

        isGoogleUser = googleUserData.getBoolean("isGoogleUser", false);

        System.out.println("* * * OnCREATE gStatus - " + isGoogleUser);

        setValidProfileDetailsToView(intent);

        //displayUserProfile();

        createNotificationChannel();
    }

    // Purpose is to make sure profile is saved/loaded correctly
    // regardless if activity started from notification or activity.
    private void setValidProfileDetailsToView(Intent intent)
    {
        boolean fromListActivity = intent.getBooleanExtra("FromListActivity", false);

        if (fromListActivity) // If true -> Intent came from List Activity.
        {
            // save the current profile to needed specific sharedPreference(s).
            saveProfileToSharedPrefFromIntent(currentUserData, intent);

            if (isGoogleUser)
            {   saveProfileToSharedPrefFromIntent(googleUserData, intent);   }

            // Upload the current profile data.
            user_name  = intent.getStringExtra("user_name");
            user_name2 = intent.getStringExtra("user_name2");
            user_ID    = intent.getStringExtra("user_ID");
            user_email = intent.getStringExtra("user_email");

            user_street  = intent.getStringExtra("user_street");
            user_suite   = intent.getStringExtra("user_suite");
            user_city    = intent.getStringExtra("user_city");
            user_zipcode = intent.getStringExtra("user_zipcode");

            // Displays given information into specific Views.
            uploadUserData(user_name, user_name2, user_email, user_ID, user_street, user_suite, user_city, user_zipcode);
            // Done
        }
        else
        {
            // Came from a Notification -> Upload the current profile into View.
            // Purpose to transfer GoogleAccount Info from SharedPreferences into the app View.
            if (isGoogleUser)
            {   loadProfileFromSharedPrefToView(googleUserData);    }
            else
            {   loadProfileFromSharedPrefToView(currentUserData);   }
            // Done.

        }
    }

    // this is called when button clicked for sign out.
    public void onClickSignOut(View view)
    {
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        SharedPreferences.Editor myEdit = googleUserData.edit();
        myEdit.putBoolean("isGoogleUser", false); // reset google user log in ID status
        myEdit.apply();

        validNotify = false;
        startActivity(intent);
    }

    public void onClickReturnToAccountList(View view)
    {
        // System.out.println("* * * ReturntoAccountList - Click *.");
        Intent intent = new Intent(this, DisplayListActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra("isSignedInUser", isGoogleUser);

        // System.out.println("* * * GOOGLE USER PICKED??? " + gStatus);
        if (isGoogleUser)
        {   // Save Google user updated information.
            saveGoogleUserInfo();
        }

        // NOTE PUT isGoogleUser FALSE FLAG HERE AS ITS RETURNING TO LIST * * *
        SharedPreferences.Editor myEdit = googleUserData.edit();
        myEdit.putBoolean("isGoogleUser", false); // reset google user log in ID status
        myEdit.apply();

        validNotify = false;
        startActivity(intent);
    }

    // Purpose is to save the profile to display to a given sharedPreferences in case
    // of activity ending and holding onto profile information.
    private void saveProfileToSharedPrefFromIntent(SharedPreferences gPref, Intent intent)
    {
        SharedPreferences.Editor myEdit = gPref.edit();

        myEdit.putString("user_name", intent.getStringExtra("user_name"));
        myEdit.putString("user_name2", intent.getStringExtra("user_name2"));
        myEdit.putString("user_ID", intent.getStringExtra("user_ID"));
        myEdit.putString("user_email", intent.getStringExtra("user_email"));
        myEdit.putString("user_street", intent.getStringExtra("user_street"));
        myEdit.putString("user_suite", intent.getStringExtra("user_suite"));
        myEdit.putString("user_city", intent.getStringExtra("user_city"));
        myEdit.putString("user_zipcode", intent.getStringExtra("user_zipcode"));

        myEdit.apply();
    }

    // Purpose is to update the current view with the saved data from a given SharedPreferences reference.
    private void loadProfileFromSharedPrefToView(SharedPreferences gPref)
    {
        uploadUserData(
                gPref.getString("user_name", "ERROR???"),
                gPref.getString("user_name2", "ERROR???"),
                gPref.getString("user_ID", "ERROR???"),
                gPref.getString("user_email", "ERROR???"),
                gPref.getString("user_street", "ERROR???"),
                gPref.getString("user_suite", "ERROR???"),
                gPref.getString("user_city", "ERROR???"),
                gPref.getString("user_zipcode", "ERROR???")   );
    }

    // Purpose is to display the given information parameters into specific Views for the Profile Display/
    public void uploadUserData(String name1, String name2, String email, String id, String street1,
                               String suite, String city, String zipcode)
    {
        TextView profileName  = findViewById(R.id.profile_value_name);      // Obtain reference(s) to key Views.
        TextView profileName2 = findViewById(R.id.profile_username_value01);
        TextView profileEmail = findViewById(R.id.profile_email);
        TextView profileID    = findViewById(R.id.profile_id_value);

        EditText profileStreet  = findViewById(R.id.editText_street_input);
        TextView profileSuite   = findViewById(R.id.EditText_suite_input);
        TextView profileCity    = findViewById(R.id.editText_city_value);
        TextView profileZipcode = findViewById(R.id.editText_zipcode_value);

        profileName.setText(name1);     profileName2.setText(name2);        // Set key Views text to given profile data.
        profileEmail.setText(email);    profileID.setText(id);

        profileStreet.setText(street1); profileSuite.setText(suite);
        profileCity.setText(city);      profileZipcode.setText(zipcode);
    }

    // Purpose is to update the GoogleUser SharedPref with input from user.
    private void saveGoogleUserInfo()
    {
        // Save here.
        SharedPreferences.Editor myEdit = googleUserData.edit();
        myEdit.putBoolean("isGoogleUser", isGoogleUser);

        EditText profileStreet  = findViewById(R.id.editText_street_input);
        EditText profileSuite   = findViewById(R.id.EditText_suite_input);
        EditText profileCity    = findViewById(R.id.editText_city_value);
        EditText profileZipcode = findViewById(R.id.editText_zipcode_value);

        myEdit.putString("user_name", user_name); // Save current view input.
        myEdit.putString("user_name2", user_name2); // Save current view input.
        myEdit.putString("user_email", user_email); // Save current view input.
        myEdit.putString("user_ID", user_ID); // Save current view input.

        myEdit.putString("user_street", profileStreet.getText().toString()); // Save current view input.
        myEdit.putString("user_suite", profileSuite.getText().toString()); // Save current view input.
        myEdit.putString("user_city", profileCity.getText().toString()); // Save current view input.
        myEdit.putString("user_zipcode", profileZipcode.getText().toString()); // Save current view input.

        myEdit.apply();
        System.out.println("* * * Google Profile - Preferences SAVED for googleUser. ");
    }

    // Save the Google User's information to upload again.
    @Override
    protected void onPause() {
        super.onPause();

        if (isGoogleUser)   // Save Google user updated information.
        {   saveGoogleUserInfo();    }

        if (validNotify)    // If valid notification state -> send notification.
        {   produceNotification();   }

        System.out.println("* * * onPause State Reached");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    // Key Lifecycle methods.
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("* onStart Begin - AccountProfileActivity *");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("* onStop Begin - AccountProfileActivity *");
    }

    // Create Notification for exiting activity.
    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            System.out.println("* * * CreateNotificationChannel - Finished Correctly.");
        }
    }

    private void produceNotification()
    {
        currentUserData = getSharedPreferences("profileDisplayData", Context.MODE_PRIVATE);

        Intent returnIntent = new Intent(this, AccountProfileActivity.class);
        //returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, returnIntent, 0);

        SharedPreferences.Editor myEdit = currentUserData.edit();
        myEdit.putBoolean("FromNotification", true);
        myEdit.apply();

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