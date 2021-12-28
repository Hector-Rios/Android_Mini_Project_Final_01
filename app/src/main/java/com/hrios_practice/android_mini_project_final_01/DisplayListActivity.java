package com.hrios_practice.android_mini_project_final_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    protected String user_name;
    protected String user_ID;
    protected String user_email;
    protected String source;
    private   User[] user_accounts;

    protected final String URL = "http://jsonplaceholder.typicode.com/users";
    protected OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        findViewById(R.id.sign_out_button_1).setOnClickListener(this);

        Intent intent = this.getIntent();
        String[] emptySlot1 = new String[11];

        user_name  = intent.getStringExtra("SignInName");
        user_ID    = intent.getStringExtra("SignInAccountID");
        user_email = intent.getStringExtra("SignInAccountEmail");

        requestUsers();  // Creates a request to obtain an array of users.

        if (user_accounts == null)
        {
            System.out.println("* * * User Account IS NULL ...");
        }
        else
        {
            System.out.println("* * * User Account IS * NOT * NULL ...");
        }

        RecyclerView myRecycleView = findViewById(R.id.recycler_view);
        PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(emptySlot1, emptySlot1);
        myRecycleView.setAdapter(myAdaptation);
    }

    private void displayAccountRecyclerView(User[] given_accts)
    {
        String image_url = "https://robohash.org/";

        // Get Information in the form of an array.
        String[] resultNames = new String[given_accts.length + 1];
        String[] resultImageLink = new String[given_accts.length + 1];
        int indexHol = 1;

        resultNames[0] = user_name; // givenNames.add(user_name);
        resultImageLink[0] = image_url + user_name;

        // Obtain needed information to display to screen in certain views.
        for (User u : given_accts)
        {
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
        //System.out.println("* * * REQUEST USERS ...");
        try
        {   run();   }
        catch (Exception e)
        {   System.out.println("* * * Exception Raised: " + e);   }

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
                            System.out.println("* * * runOnUiThread - Finsihed. ???");

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