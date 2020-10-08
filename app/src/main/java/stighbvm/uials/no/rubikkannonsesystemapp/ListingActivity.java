package stighbvm.uials.no.rubikkannonsesystemapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

public class ListingActivity extends OptionsMenuActivity {

    private FloatingActionButton mCreateNewListingView;
    private Button mLoginbutton;
    ItemAdapter adapter = new ItemAdapter();
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        mCreateNewListingView = findViewById(R.id.fab);
        mLoginbutton = findViewById(R.id.login_button);

        mCreateNewListingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateListingActivity();
            }
        });

        mLoginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Client.getSingleton().setItemsLoadedListener(this::onItemsLoaded);

    }

    protected void onItemsLoaded(List<Item> items){
        recyclerView.post(() -> adapter.setItems(items));
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openCreateListingActivity(){
        Intent intent = new Intent(this, CreateListingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data){
        super.onActivityResult(reqestCode, resultCode, data);
    }
}