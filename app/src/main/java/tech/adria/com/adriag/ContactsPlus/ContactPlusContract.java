package tech.adria.com.adriag.ContactsPlus;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Represents the View and Presenter contract
 */
public interface ContactPlusContract {
    interface View {
    }

    interface Presenter {
        void setUpForPeopleAPI();
        void getCOnnections();
    }
}
