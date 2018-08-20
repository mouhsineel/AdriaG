package tech.adria.com.adriag.SignInMPV;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

/**
 * Represents the View and Presenter contract
 */
public interface SignINContract {
    interface View {
        void setMessage(String message);
        void updateUI(GoogleSignInAccount account);
    }

    interface Presenter {
        void startSignIn(Activity mContext);
        void handleSignInResult(Task<GoogleSignInAccount> completedTask);
    }
}
