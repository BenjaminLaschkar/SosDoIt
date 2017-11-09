package ca.uqac.sosdoit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class RegisterNameActivity extends AppCompatActivity
{
    private EditText inputUsername, inputFirstName, inputLastName;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        inputUsername = (EditText) findViewById(R.id.username);
        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputLastName = (EditText) findViewById(R.id.last_name);
        btnRegister = (Button) findViewById(R.id.btn_register_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerName(v);
            }
        });

        inputLastName.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerName(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Util.REGISTRATION_COMPLETE_REQUEST && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
        }
        finish();
    }

    private void registerName(View v)
    {
        final String username = inputUsername.getText().toString().trim();
        final String firstName = inputFirstName.getText().toString().trim();
        final String lastName = inputLastName.getText().toString().trim();

        boolean exit = false;

        if (TextUtils.isEmpty(username)) {
            inputUsername.setError(getString(R.string.msg_empty_username));
            inputUsername.requestFocus();
            exit = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            inputFirstName.setError(getString(R.string.msg_empty_first_name));

            if (!exit) {
                inputFirstName.requestFocus();
            }

            exit = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            inputLastName.setError(getString(R.string.msg_empty_last_name));

            if (!exit) {
                inputLastName.requestFocus();
            }

            return;
        }

        if (exit) {
            return;
        }

        Util.hideKeyboard(RegisterNameActivity.this, v);

        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            db.addUser(user.getUid(), firstName, lastName, username);
            progressBar.setVisibility(View.GONE);
            startActivityForResult(new Intent(RegisterNameActivity.this, RegisterAddressActivity.class), Util.REGISTRATION_COMPLETE_REQUEST);
        } else {
            finish();
        }
    }
}
