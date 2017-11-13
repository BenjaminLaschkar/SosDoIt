package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.database.IDatabaseManager;

public class ProfileActivity extends AppCompatActivity implements IDatabaseManager.UserResult
{
    private Toolbar toolbar;
    TextView username, firstName, lastName, email, address;
    Button editProfile;
    FirebaseAuth auth;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.username);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.city);
        editProfile = findViewById(R.id.btn_edit_profile);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.btn_profile);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            email.setText(user.getEmail());
        }

        db.getUser(auth.getUid(), this);



        editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void call(User user)
    {
        if (user.hasUsername()) {
            username.setText(user.getUsername());
        }
        if (user.hasFirstName()) {
            firstName.setText(user.getFirstName());
        }
        if (user.hasLastName()) {
            lastName.setText(user.getLastName());
        }
        if (user.hasAddress()) {
            address.setText(user.getAddress().toString());
        }
    }
}
