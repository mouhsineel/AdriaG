package tech.adria.com.adriag.SignInMPV;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;

import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import android.support.v7.app.AppCompatActivity;

import tech.adria.com.adriag.ContactsPlus.ContactPlusActivity;
import tech.adria.com.adriag.R;

public class SignINActivity extends AppCompatActivity implements View.OnClickListener, SignINContract.View {

    private static final String TAG = "MainActivity";

    SignInButton signInButton;
    Button goContact;

    private int userIdCounter = 0;
    private Context mContext;

    //private FragmentActivity mFragmentActivity;
    private GoogleTokenResponse mResponseListener;
    SignINPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_main);

        mPresenter = new SignINPresenter(this);




        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        goContact= findViewById(R.id.contacplus);

        signInButton.setOnClickListener(this);
        goContact.setOnClickListener(this);

        mContext = this;
        mPresenter.signIn(this);
        updateUI(null);


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                mPresenter.startSignIn((Activity) mContext);
                break;
            case R.id.contacplus:
                finish();
                startActivity(new Intent(this, ContactPlusActivity.class));

                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SignINPresenter.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            mPresenter.handleSignInResult(task);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void updateUI(GoogleSignInAccount account) {

        if(account!=null){

            setMessage(account.getDisplayName());
            signInButton.setVisibility(View.INVISIBLE);

        }else
        {
            setMessage( getResources().getString(R.string.notConnected));
        }


    }



    @Override
    public void setMessage(String text){
        ((TextView)findViewById(R.id.status)).setText(text);
    }
}


