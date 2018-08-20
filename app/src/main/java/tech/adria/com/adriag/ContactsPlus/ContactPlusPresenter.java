package tech.adria.com.adriag.ContactsPlus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
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

/**
 * Represents the P part in MVP architecture
 * Simple presenter that implements {@link ContactPlusContract.Presenter},
 * delegate view requests to model, and update the view with data
 */
public class ContactPlusPresenter implements ContactPlusContract.Presenter {
    ContactPlusModel mModel;
    ContactPlusContract.View mView;
    public static String OAUTH_URL =  "oauth2:https://www.googleapis.com/auth/plus.login";
    PeopleService peopleService =null;
    List<Person> connections =null;

    public static String serverClientId="764100145881-49sdiupnnbu66tmp0s2bcvtefui2rltr.apps.googleusercontent.com";

    GoogleSignInAccount account;
    public static String TAG="SignInPresenter";

    public ContactPlusPresenter(ContactPlusContract.View view) {
        this.mView = view;
    }


    @Override
    public void setUpForPeopleAPI() {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId =serverClientId;
        String clientSecret = "o_zCeR6h3qfRMIcmC7c4kXJG";
        //clientSecret="";

        // Or your redirect URL for web based applications.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";//https://accounts.google.com/o/oauth2/auth
        redirectUrl= "https://accounts.google.com/o/oauth2/auth";
        String scope = "https://www.googleapis.com/auth/contacts";

        // Step 1: Authorize -->
        String authorizationUrl =
                new GoogleBrowserClientRequestUrl(clientId, redirectUrl, Arrays.asList(scope)).build();

        // Point or redirect your user to the authorizationUrl.
        System.out.println("Go to the following link in your browser:");
        System.out.println(authorizationUrl);
        String code="";
        try {
            // Read the authorization code from the standard input stream.
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("What is the authorization code?");
             code =in.readLine();
            //code = account.getServerAuthCode();
            // End of Step 1 <--

        }catch (Exception e){

        }

        GoogleTokenResponse tokenResponse=null;

        try {
            // Step 2: Exchange -->
             tokenResponse =  new GoogleAuthorizationCodeTokenRequest(
                    httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                    .execute();
            // End of Step 2 <--

        }catch (Exception e){

        }



        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

    }


    public void getCOnnections()
    {
        ListConnectionsResponse response=null;
        try
        {
            response = peopleService.people().connections().list("people/me")
                    .setPersonFields("names,emailAddresses")
                    .execute();
            connections = response.getConnections();

        }catch (Exception ex){
            Log.i("ListConnectionsResponse","ListConnectionsResponse ex: "+ex.toString());
        }
    }
}
