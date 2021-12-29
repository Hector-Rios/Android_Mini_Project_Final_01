package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AccountProfileActivity extends AppCompatActivity {

    protected String user_name;
    protected String user_name2;
    protected String user_ID;
    protected String user_email;
    protected String user_street;
    protected String user_suite;
    protected String user_city;
    protected String user_zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);

        Intent intent = this.getIntent();

        boolean gStatus = intent.getBooleanExtra("LoggedInUser", false);

        user_name = intent.getStringExtra("user_name");
        user_name2 = intent.getStringExtra("user_name2");
        user_ID = intent.getStringExtra("user_ID");
        user_email = intent.getStringExtra("user_email");
        
        if (!gStatus) {
            user_street = intent.getStringExtra("user_street");
            user_suite = intent.getStringExtra("user_suite");
            user_city = intent.getStringExtra("user_city");
            user_zipcode = intent.getStringExtra("user_zipcode");
        }
        
        displayUserProfile();
    }

    // Display info on Views. 
    private void displayUserProfile() {

    }
}