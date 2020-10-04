package stighbvm.uials.no.rubikkannonsesystemapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class CreateUserActivity extends AppCompatActivity {

    private CreateUserTask mCreateUserTask = null;

    // UI refrences.
    private EditText mUseridView;
    private EditText mPasswordView;
    private EditText mEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        mUseridView = findViewById(R.id.create_username);
        mEmailView = findViewById(R.id.create_email);
        mPasswordView = findViewById(R.id.create_password);

        Button createUserButton = findViewById(R.id.create_user);
        createUserButton.setOnClickListener(view -> attemptUserCreation());

    }

    private boolean isUseridValid(String userid) {
        //TODO: Replace this with your own logic
        return userid.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own lgoic
        return email.length() > 2;
    }

    private void attemptUserCreation(){

        // Store values at the time of the login attempt.
        String userid = mUseridView.getText().toString();
        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUseridValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_userid));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mCreateUserTask = new CreateUserTask(userid, password, email);
            mCreateUserTask.execute((Void) null);
        }
    }



public class CreateUserTask extends AsyncTask<Void, Void, Boolean> {

    private final String mUserid;
    private final String mPassword;
    private final String mEmail;

    CreateUserTask(String userid, String password, String email) {
        mUserid = userid;
        mPassword = password;
        mEmail = email;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return Client.getSingleton().createUser(mUserid,mPassword,mEmail) != null;
        } catch (IOException e) {
            System.out.println("Failed to create user");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mCreateUserTask = null;


        if (success) {
            setResult(RESULT_OK);
            finish();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mCreateUserTask = null;
    }
}
}

