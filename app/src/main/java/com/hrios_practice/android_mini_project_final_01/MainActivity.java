package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG          = "SignInActivity";
    private static final int RC_SIGN_IN      = 9001;
    private static final String CHANNEL_ID   = "CHANNEL_03";
    private static final int NOTIFICATION_ID = 1004;
    protected boolean validNotify = true;

    protected GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in_button_1).setOnClickListener(this);
        // System.out.println("OnCreate. Before SetUpGoogleSignIn ...");

        setUpGoogleSignIn(); // Sets up Google Sign in stuff.
        createNotificationChannel();
    }

    public void setUpGoogleSignIn() {
        // [START configure_signin] Configure sign-in to request the user's ID,
        // email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();      // [END configure_signin]

        // [START build_client] : Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START customize_button] Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button_1);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
    }

    // Sign in Begin.
    private void SignIn_By_Click(View v)
    {
        try {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
            //System.out.println("* * * onClick Pressed. ");
        }
        catch (Exception e)
        {    System.out.println("Error? " + e.toString());    }
    }

    @Override   // [START onActivityResult]
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("* * * onActivityResult.");
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }   // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        System.out.println("* * * handleSignInResult.");

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }   // [END handleSignInResult]

    // Update certain UI aspects.
    private void updateUI(GoogleSignInAccount account)
    {
        // System.out.println("* * * updateUI.");
        if (account != null) {
            String name = account.getDisplayName();
            String user_name = account.getGivenName();
            String ID = account.getId();
            String email = account.getEmail();

            // Intent creation
            Intent intent = new Intent(this, DisplayListActivity.class);

            // Update Information values
            intent.putExtra("SignInName", name);
            intent.putExtra("SignInName2", user_name);
            intent.putExtra("SignInAccountID", ID);
            intent.putExtra("SignInAccountEmail", email);
            intent.putExtra("initialSignIn", true);

            validNotify = false;     // Notification flag.
            startActivity(intent);   // Launch new Activity.

        } else {
            System.out.println("* * * Account returned as NULL.");
        }
    }


    @Override
    public void onClick(View v) {   // Handles click events for this activity.
        if (v.getId() == R.id.sign_in_button_1) {
            // System.out.println("Sign In Button Pressed ... ");
            SignIn_By_Click(v);
        }
    }

    // Key lifecycle methods.
    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in] Check for existing Google Sign In account, if the
        // user is already signed in the GoogleSignInAccount will be non-null.
        // System.out.print("* * * onStart. ");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }   // [END on_start_sign_in]

    @Override
    protected void onResume() {
        super.onResume();
        // System.out.println("* onResume Begin - DisplayListActivity *");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // System.out.println("* onPause Begin - DisplayListActivity *");

    }

    @Override
    protected void onStop() {
        super.onStop();
        // System.out.println("* onStop Begin - DisplayListActivity *");

        if (validNotify)
        {   produceNotification();   }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // System.out.println("* onDestroy Begin - DisplayListActivity *");
    }

    // Notification Methods for clickable returning notification operation.
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
            //System.out.println("* * * CreateNotificationChannel - Finished Correctly.");
        }
    }

    private void produceNotification()
    {
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, returnIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Forget Me Not - Reminder Notification")
                .setContentText("Don't Forget About Me  ... ")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}