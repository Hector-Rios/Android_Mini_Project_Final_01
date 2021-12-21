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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DisplayListActivity extends AppCompatActivity implements View.OnClickListener{

    protected String user_name;
    protected String user_ID;
    protected String user_email;
    protected User[] user_accounts;

    protected final String URL = "http://jsonplaceholder.typicode.com/users";
    protected Integer userCount = 0;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        findViewById(R.id.sign_out_button_1).setOnClickListener(this);

        Intent intent = this.getIntent();

        user_name = intent.getStringExtra("SignInName");
        user_ID = intent.getStringExtra("SignInAccountID");
        user_email = intent.getStringExtra("SignInAccountEmail");

        requestUsers();  // Creates a request to obtain an array of users.

        displayAccountRecyclerView(); // Begins the display of information for this activity.
    }

    private void displayAccountRecyclerView()
    {
        // Get Information in the form of an array.
        ArrayList<String> givenNames = new ArrayList<>();
        ArrayList<String> givenIDs = new ArrayList<>();

        String[] resultNames;
        String[] resultIDs;
        givenNames.add(user_name);

        if (user_accounts == null)
        {   System.out.println("user_accounts is NULL");   }

        for (User u : user_accounts)
        {
            givenNames.add(u.getName());
            givenIDs.add(u.getId());
        }

        resultNames = (String[]) givenNames.toArray(); // Convert Arraylist to an Array.
        resultIDs = (String[]) givenIDs.toArray(); // Convert Arraylist to an Array.

        //String[] ppl_names = this.getResources().getStringArray(R.array.person_list);
        //givenNames.addAll(Arrays.asList(ppl_names));
        //ppl_names[0] = user_name;

        // ID generated array list.
        //Integer [] givenID = new Integer[givenNames.size()]; // { Random.nextInt(100) };
        //Random myGuess = new Random();

        //for ( int i = 0; i < givenID.length; i++)
        //{    givenID[i] = myGuess.nextInt(100);    }

        // Create RecyclerView and pass information to it.
        RecyclerView myRecycleView = findViewById(R.id.recycler_view);
        //PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(ppl_names, givenID);
        PersonAccountAdaptor myAdaptation = new PersonAccountAdaptor(resultNames, resultIDs);
        myRecycleView.setAdapter(myAdaptation);
    }

    // Follow 4A Directions and obtain an array of USERS.
    private void requestUsers()
    {
        try
        {   run();   }
        catch (Exception e)
        {   System.out.println("* * * Exception Raised: " + e);   }
    }

    // Creates a connection request to receive Json for processing to user accounts
    private void run() {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body())
                {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Headers responseHeaders = response.headers();
                    //for (int i = 0, size = responseHeaders.size(); i < size; i++)
                    //{ //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        //System.out.println("*** " + responseHeaders.get("id"));
                        //System.out.println("test-print-ID: " + responseHeaders.name(i)); }
                    // System.out.println("============ 1 ==================");

                    String source = responseBody.string();
                    System.out.println("[ START ] " + source + " [ END ] ");

                    // Try and loop to find correct values to create a user. and record it.
                    Gson gson = new Gson();
                    try {
                        user_accounts = gson.fromJson(source, User[].class);
                        System.out.println("Work??? ");
                        //for (User u : myUsers)
                        //{   System.out.println("User: ID: " + u.id + ", name: " + u.name);   }
                    }
                    catch(Exception exception)
                    {   System.out.println("EXCEPTION: " + exception);   }
                }
            }
        });
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