package ca.uqac.sosdoit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.database.DatabaseManager;


public class RegisterNameActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName;
    private Button submitRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputLastName = (EditText) findViewById(R.id.last_name);
        submitRegister = (Button) findViewById(R.id.submit_register_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(getApplicationContext(), "Enter firstName !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(getApplicationContext(), "Enter lastName !", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth = FirebaseAuth.getInstance();

                final FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
                String id = firebaseuser.getUid();
                User user = new User(id,inputFirstName.getText().toString(),inputLastName.getText().toString(),null, null, false,null );
                DatabaseManager.getInstance().addUser(user);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
