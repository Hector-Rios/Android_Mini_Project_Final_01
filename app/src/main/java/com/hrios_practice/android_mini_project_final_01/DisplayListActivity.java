package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DisplayListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String CHANNEL_ID      = "CHANNEL_02";
    private static final int NOTIFICATION_ID    = 1003;
    protected String user_name;
    protected String user_name2;
    protected String user_ID;
    protected String user_email;
    protected String source;
    protected User   curGoogleUser;
    private   User[] user_accounts;
    protected Intent profileIntent;
    protected boolean firstSignIn = false;
    protected boolean validNotify = true;
    protected SharedPreferences googleUserData;  // = getSharedPreferences("googleUserData", Context.MODE_PRIVATE);
    protected SharedPreferences currentUserData; // = getSharedPreferences("profileDisplayData", Context.MODE_PRIVATE);

    protected final String URL = "http://jsonplaceholder.typicode.com/users";
    protected OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        googleUserData = getSharedPreferences("googleUserData", Context.MODE_PRIVATE);
        currentUserData = getSharedPreferences("profileDisplayData", Context.MODE_PRIVATE);


        findViewById(R.id.sign_out_button_1).setOnClickListener(this);
        Intent intent = this.getIntent();

        // boolean localStatus = intent.getBooleanExtra("isSignedInUser", false);
        firstSignIn = intent.getBooleanExtra("initialSignIn", false);

        String[] emptySlot1 = new String[11];

        if (firstSignIn) {
            user_name   = intent.getStringExtra("SignInName");
            user_name2  = intent.getStringExtra("SignInName2");
            user_ID     = intent.getStringExtra("SignInAccountID");
            user_email  = intent.getStringExtra("SignInAccountEmail");

            curGoogleUser = new User(user_name, user_name2, user_ID, user_email);
        }
        else
        {
            updateCurrentGoogleUser(); // Purpose is to update logged in user information with activity.
        }

        requestUsers();  // Creates a request to obtain an array of users.

        // Generate basic list view of accounts.
        RecyclerView myRecycleView = findViewById(R.id.recycler_view);
        PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(emptySlot1, emptySlot1);
        myRecycleView.setAdapter(myAdaptation);

        createNotificationChannel(); // Notification Update.
    }


    // Updates the Signed in User's Address based on given input.
    private void updateCurrentGoogleUser()
    {
        if (curGoogleUser == null)
        {   curGoogleUser = new User();   }

        SharedPreferences givenPref = getSharedPreferences("googleUserData", Context.MODE_PRIVATE);

        String iName     = givenPref.getString("user_name", "Error-Pref");
        String iUserName = givenPref.getString("user_name2", "Error-Pref");
        String iEmail    = givenPref.getString("user_email", "Error-Pref");
        String i_ID      = givenPref.getString("user_ID", "Error-Pref");

        String iStreet  = givenPref.getString("user_street", "[ Enter Street Value ]");
        String iSuite   = givenPref.getString("user_suite", "[ Enter Suite Value ]");
        String iCity    = givenPref.getString("user_city", "[ Enter City Value ]");
        String iZipcode = givenPref.getString("user_zipcode", "[ Enter ZipCode Value ]");

        curGoogleUser.updatePersonal(iName, iUserName, iEmail, i_ID);
        curGoogleUser.updateAddress(iStreet, iSuite, iCity, iZipcode);
        user_name = iName;
    }

    private void displayAccountRecyclerView(User[] given_accts)
    {
        String image_url = "https://robohash.org/";

        // Get Information in the form of an array.
        String[] resultNames     = new String[given_accts.length + 1];
        String[] resultImageLink = new String[given_accts.length + 1];
        int indexHol = 1;

        resultNames[0] = user_name; // givenNames.add(user_name);
        resultImageLink[0] = image_url + user_name;

        // Obtain needed information to display to screen in certain views.
        for (User u : given_accts)
        {
            //System.out.println("User: " + u);
            //System.out.println("\tAddress: " + u.address);
            resultNames[indexHol] = u.getName();//givenNames.add(u.getName());
            resultImageLink[indexHol] = image_url + u.getName();
            indexHol += 1;                      // increment value index for result values array.
        }

        // Create RecyclerView and pass information to it.
        RecyclerView myRecycleView = findViewById(R.id.recycler_view);
        PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(resultNames, resultImageLink);
        myRecycleView.setAdapter(myAdaptation);
    }

    // Follow 4A Directions and obtain an array of USERS.
    private void requestUsers()
    {
        try
        {   run();   }
        catch (Exception e)
        {   System.out.println("* * * RequestUsers - Exception Raised: " + e);   }

    }

    // Creates a connection request to receive Json for processing to user accounts
    public void run()
    {
        //System.out.println("* * Begin Run()");
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();

        Callback msgCall = new Callback() {
            private User[] tempUsers;

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    source = responseBody.string();

                    // System.out.println("[ START ] " + source + " [ END ] ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            user_accounts = gson.fromJson(source, User[].class);
                            System.out.println("* * * runOnUiThread - Finished. ???");

                            if (user_accounts == null)
                            { System.out.println("* * * runOnUiThread - user_accounts is NULL. ???"); }
                            else
                            {
                                System.out.println("* * * runOnUiThread - user_accounts is NOT NULL. ???");
                                displayAccountRecyclerView(user_accounts);
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("* * * GSON Exception Raised: " + e.getMessage());
                }}};

        client.newCall(request).enqueue(msgCall);
    }

    public void onClickProfile(View v)
    {
        System.out.println("* * * DisplayListActivity - OnClickProfile...");
        boolean signedInUser = false;
        User picked_user     = null;
        SharedPreferences.Editor googleEdit = googleUserData.edit();
        profileIntent = new Intent(this, AccountProfileActivity.class);

        // profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TextView profileView = v.findViewById(R.id.account_name_text_view);
        String cur_name      = (String) profileView.getText();

        if (cur_name.compareTo(user_name) == 0)
        {
            signedInUser = true; // Identifier for if the google user was clicked on.
            picked_user  = curGoogleUser;
            System.out.println("* * * Google USER Detected ??? >>>" + signedInUser);
        }
        else  // Obtain the chosen user thats not signed in.
        {
            for (User u : user_accounts)
            {
                if (u.getName().compareTo(cur_name) == 0) // match found.
                {   picked_user = u;   break; }
            }
        }

        //profileIntent.putExtra("isGoogleUser", signedInUser);    // Identifier for Signed in Account.
        googleEdit.putBoolean("isGoogleUser", signedInUser);   googleEdit.apply();

        profileIntent.putExtra("FromListActivity", true);  // Identifier for Data Source.

        saveExtra("user_name", picked_user.getName());
        saveExtra("user_name2", picked_user.getUsername());
        saveExtra("user_ID", picked_user.getId());
        saveExtra("user_email", picked_user.getEmail());

        saveExtra("user_street", picked_user.getAddress().getStreet());
        saveExtra("user_suite", picked_user.getAddress().getSuite());
        saveExtra("user_city", picked_user.getAddress().getCity());
        saveExtra("user_zipcode", picked_user.getAddress().getZipcode());

        /*if(!signedInUser)  // Save User info if other account picked to save info.
        {                  // Maybe use saved Preferences.
            saveExtra("g_user_name", curGoogleUser.getName());
            saveExtra("g_user_name2", curGoogleUser.getUsername());
            saveExtra("g_user_ID", curGoogleUser.getId());
            saveExtra("g_user_email", curGoogleUser.getEmail());

            saveExtra("g_user_street", curGoogleUser.getAddress().getStreet());
            saveExtra("g_user_suite", curGoogleUser.getAddress().getSuite());
            saveExtra("g_user_city", curGoogleUser.getAddress().getCity());
            saveExtra("g_user_zipcode", curGoogleUser.getAddress().getZipcode());
        }*/

        validNotify = false;   // Shift from one activity to next shouldn't cause notification.
        startActivity(profileIntent);  // Start Activity with certain data/info.
    }

    public void saveExtra(String label, String value)
    {   profileIntent.putExtra(label, value);   }

    @Override
    public void onClick(View v)
    {
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
        launchSignInActivity();
    }

    private void launchSignInActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("SignInAccountEmail", email);

        validNotify = false;   // Shift from one activity to next shouldn't cause notification.
        startActivity(intent);
    }

    // Key Lifecycle methods.
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("* onStart Begin - DisplayListActivity *");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("* onResume Begin - DisplayListActivity *");

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("* onPause Begin - DisplayListActivity *");

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("* onStop Begin - DisplayListActivity *");

        if (validNotify)
        {   produceNotification();   }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("* onDestroy Begin - DisplayListActivity *");

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
            //System.out.println("* * * CreateNotificationChannel - Finished Correctly.");
        }
    }

    private void produceNotification()
    {
        Intent returnIntent = new Intent(this, DisplayListActivity.class);
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
        //System.out.println("* * * onClick 01 - Version 01");
    }
}