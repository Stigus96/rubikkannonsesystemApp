package stighbvm.uials.no.rubikkannonsesystemapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.math.BigDecimal;

public class CreateListingActivity extends AppCompatActivity {

    private CreateListingTask mCreateListingTask = null;

    //UI refrences
    private EditText mTitleView;
    private EditText mPriceView;
    private EditText mDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createlisting);

        mTitleView = findViewById(R.id.create_title);
        mPriceView = findViewById(R.id.create_price);
        mDescriptionView = findViewById(R.id.create_description);

        Button createListingButton = findViewById(R.id.create_listing_button);
        createListingButton.setOnClickListener(view -> attemptListingCreation());
    }

    private void attemptListingCreation(){

        // Store values at the time of the login attempt.
        String title = mTitleView.getText().toString();
        String stringprice = mPriceView.getText().toString();
        BigDecimal price = new BigDecimal(stringprice);
        String description = mDescriptionView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(title)) {
            mTitleView.setError(getString(R.string.error_field_required));
            focusView = mTitleView;
            cancel = true;
        } else if (TextUtils.isEmpty(stringprice)) {
            mPriceView.setError(getString(R.string.error_field_required));
            focusView = mPriceView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mCreateListingTask = new CreateListingTask(title, price, description);
            mCreateListingTask.execute((Void) null);
        }
    }


    public class CreateListingTask extends AsyncTask<Void, Void, Boolean> {

        private final String mTitle;
        private final BigDecimal mPrice;
        private final String mDescription;

        CreateListingTask(String title, BigDecimal price, String description) {
            mTitle = title;
            mPrice = price;
            mDescription = description;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return Client.getSingleton().createItem(mTitle,mPrice,mDescription) != null;
            } catch (IOException e) {
                System.out.println("Failed to create Listing");
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCreateListingTask = null;


            if (success) {
                setResult(RESULT_OK);
                finish();
            } else {
                mTitleView.setError(getString(R.string.error_vauge));
                mTitleView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mCreateListingTask = null;
        }
    }

}


