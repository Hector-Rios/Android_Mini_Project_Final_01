package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    protected boolean gStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);

        Intent intent = this.getIntent();

        gStatus = intent.getBooleanExtra("LoggedInUser", false);

        user_name = intent.getStringExtra("user_name");
        user_name2 = intent.getStringExtra("user_name2");
        user_ID = intent.getStringExtra("user_ID");
        user_email = intent.getStringExtra("user_email");

        user_street = intent.getStringExtra("user_street");
        user_suite = intent.getStringExtra("user_suite");
        user_city = intent.getStringExtra("user_city");
        user_zipcode = intent.getStringExtra("user_zipcode");
        
        displayUserProfile();
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

        if (!gStatus)
        {
            EditText profileStreet = findViewById(R.id.editText_street_input);
            TextView profileSuite = findViewById(R.id.EditText_suite_input);
            TextView profileCity = findViewById(R.id.editText_city_value);
            TextView profileZipcode = findViewById(R.id.editText_zipcode_value);

            profileStreet.setText(user_street);
            profileSuite.setText(user_suite);
            profileCity.setText(user_city);
            profileZipcode.setText(user_zipcode);
        }
    }

    // this is called when button clicked for sign out.
    public void onClickSignOut(View view) {
        System.out.println("* * * AccountProfileFunc - Sign Out Op.");

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
        System.out.println("* * * ReturntoAccountList - Click *.");
        Intent intent = new Intent(this, DisplayListActivity.class);

        intent.putExtra("isSignedInUser", gStatus);

        intent.putExtra("SignInName", user_name);
        intent.putExtra("SignInName2", user_name2);
        intent.putExtra("SignInAccountID", user_ID);
        intent.putExtra("SignInAccountEmail", user_email);

        intent.putExtra("user_street", user_street);
        intent.putExtra("user_suite", user_suite);
        intent.putExtra("user_city", user_city);
        intent.putExtra("user_zipcode", user_zipcode);

        startActivity(intent);
    }
}