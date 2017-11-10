package ca.uqac.sosdoit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ca.uqac.sosdoit.util.Util;

public class LoginActivity extends AppCompatActivity
{
    private EditText inputEmail, inputPassword;
    private Button btnLogIn, btnRegister, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogIn = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register_name);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        btnLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TestForDatabase.testAdvert();
                //login(v);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), Util.REGISTRATION_COMPLETE_REQUEST);
                //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Util.REGISTRATION_COMPLETE_REQUEST && resultCode == RESULT_OK) {
            finish();
        }
    }

    private void login(View v)
    {
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        boolean exit = false;

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.msg_empty_email));
            inputEmail.requestFocus();
            exit = true;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.msg_empty_password));

            if (!exit) {
                inputPassword.requestFocus();
            }

            return;
        }

        if (exit) {
            return;
        }

        Util.hideKeyboard(LoginActivity.this, v);

        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    Util.showKeyboard(LoginActivity.this, inputEmail);
                    Toast.makeText(LoginActivity.this, getString(R.string.msg_auth_failed), Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }
            }
        });
    }
}
