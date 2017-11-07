package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.database.IDatabaseManager;

public class ProfileActivity extends AppCompatActivity implements IDatabaseManager.UserResult
{
    TextView username, firstName, lastName, email, address;
    Button editProfile;
    DatabaseManager db = DatabaseManager.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.username);
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        email = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);
        editProfile = (Button) findViewById(R.id.btn_edit_profile);

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
    public void call(User user)
    {
        username.setText(user.getPseudo());
        firstName.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        email.setText(auth.getCurrentUser().getEmail());
        address.setText(user.getAddress());

    }
}
