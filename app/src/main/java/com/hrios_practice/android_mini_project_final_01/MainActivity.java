package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    protected GoogleSignInClient mGoogleSignInClient;
    // protected TextView mStatusTextView;

/*
* Note: Put a flag that deletes the history of the previous activity.
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in_button_1).setOnClickListener(this);
        System.out.println("OnCreate. Before SetUpGoogleSignIn ...");
        setUpGoogleSignIn(); // Sets up Google Sign in stuff.
    }

    public void setUpGoogleSignIn() {
        // [START configure_signin] Configure sign-in to request the user's ID,
        // email address, and basic profile. ID and basic profile are
        // included in DEFAULT_SIGN_IN.
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

            // Signed in successfully, show authenticated UI.
            updateUI(account);
            // Prob launch new activity. * * *

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
            // Prob launch new activity. * * *
            // Intent creation
            Intent intent = new Intent(this, DisplayListActivity.class);
            // Update Information values
            // Do this in the activivty and not here. displayAccountRecyclerView(name, ID, email);
            // Launch new Activity.
            intent.putExtra("SignInName", name);
            intent.putExtra("SignInName2", user_name);
            intent.putExtra("SignInAccountID", ID);
            intent.putExtra("SignInAccountEmail", email);

            startActivity(intent);

        } else {
            //mStatusTextView.setText(R.string.signed_out);
            System.out.println("* * * Account returned as NULL.");
            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    /*private void displayAccountRecyclerView(String name, String ID, String email)
    {
        // Get Information in the form of an array.
        ArrayList<String> givenNames = new ArrayList<>();
        givenNames.add("name");

        String[] ppl_names = this.getResources().getStringArray(R.array.person_list);
        givenNames.addAll(Arrays.asList(ppl_names));
        ppl_names[0] = name;

        // ID generated array list.
        Integer [] givenID = new Integer[givenNames.size()]; // { Random.nextInt(100) };
        Random myGuess = new Random();

        for ( int i = 0; i < givenID.length; i++)
        {    givenID[i] = myGuess.nextInt(100);    }

        // Create RecyclerView and pass information to it.
        RecyclerView myRecycleView = findViewById(R.id.recycler_view);
        PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(ppl_names, givenID);
        myRecycleView.setAdapter(myAdaptation);
    }*/


    @Override
    public void onClick(View v) {   // Handles click events for this activity.
        if (v.getId() == R.id.sign_in_button_1) {
            System.out.println("Sign In Button Pressed ... ");
            SignIn_By_Click(v);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in] Check for existing Google Sign In account, if the
        // user is already signed in the GoogleSignInAccount will be non-null.
        System.out.print("* * * onStart. ");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }
}