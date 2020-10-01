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

public class ListingActivity extends AppCompatActivity {
    private TextView createnewuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        createnewuser = (TextView) findViewById(R.id.createNewUser);
        createnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateUserActivity();
            }
        });



    }

    public void openCreateUserActivity(){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }
}