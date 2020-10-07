package stighbvm.uials.no.rubikkannonsesystemapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ListingActivity extends OptionsMenuActivity {

    private FloatingActionButton mCreateNewListingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        mCreateNewListingView = findViewById(R.id.fab);

        mCreateNewListingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateListingActivity();
            }
        });




    }

    public void openCreateListingActivity(){
        Intent intent = new Intent(this, CreateListingActivity.class);
        startActivity(intent);
    }
}