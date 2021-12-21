package com.hrios_practice.android_mini_project_final_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DisplayListActivity extends AppCompatActivity implements View.OnClickListener{

    protected String user_name;
    protected String user_ID;
    protected String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        findViewById(R.id.sign_out_button_1).setOnClickListener(this);

        Intent intent = this.getIntent();

        user_name = intent.getStringExtra("SignInName");
        user_ID = intent.getStringExtra("SignInAccountID");
        user_email = intent.getStringExtra("SignInAccountEmail");

        displayAccountRecyclerView();
    }

    private void displayAccountRecyclerView()
    {
        // Get Information in the form of an array.
        ArrayList<String> givenNames = new ArrayList<>();
        givenNames.add("name");

        String[] ppl_names = this.getResources().getStringArray(R.array.person_list);
        givenNames.addAll(Arrays.asList(ppl_names));
        ppl_names[0] = user_name;

        // ID generated array list.
        Integer [] givenID = new Integer[givenNames.size()]; // { Random.nextInt(100) };
        Random myGuess = new Random();

        for ( int i = 0; i < givenID.length; i++)
        {    givenID[i] = myGuess.nextInt(100);    }

        // Create RecyclerView and pass information to it.
        RecyclerView myRecycleView = findViewById(R.id.recycler_view);
        PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(ppl_names, givenID);
        myRecycleView.setAdapter(myAdaptation);
    }

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

        // Launch main activiy.
        launchSignInActivity();
    }

    private void launchSignInActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        // intent.putExtra("SignInAccountEmail", email);

        startActivity(intent);
    }
}