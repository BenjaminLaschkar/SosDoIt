package ca.uqac.sosdoit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import ca.uqac.sosdoit.util.Util;

public class RegisterActivity extends AppCompatActivity
{
    private EditText inputEmail, inputPassword;
    private Button btnLogIn, btnRegister;
    private TextInputLayout passwordContainer;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private AdvancedTextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        passwordContainer = (TextInputLayout) findViewById(R.id.password_container);
        btnRegister = (Button) findViewById(R.id.btn_register_name);
        btnLogIn = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        textWatcher = new AdvancedTextWatcher(inputPassword);

        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                register(v);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    register(v);
                    return true;
                }
                return false;
            }
        });
    }

    private void register(View v)
    {
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        boolean exit = false;

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.msg_empty_email));
            inputEmail.requestFocus();
            exit = true;
        }

        if (password.length() < 6) {
            passwordContainer.setPasswordVisibilityToggleEnabled(false);

            if (TextUtils.isEmpty(password)) {
                inputPassword.setError(getString(R.string.msg_empty_password));
            } else {
                inputPassword.setError(getString(R.string.msg_minimum_password));
            }

            textWatcher.addListener();

            if (!exit) {
                inputPassword.requestFocus();
            }

            return;
        }

        if (exit) {
            return;
        }

        Util.hideKeyboard(RegisterActivity.this, v);

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        inputEmail.setError(getString(R.string.msg_invalid_email));
                    } catch(FirebaseAuthUserCollisionException e) {
                        inputEmail.setError(getString(R.string.msg_email_already_used));
                    } catch(Exception e) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.msg_register_failed) + ": " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Util.showKeyboard(RegisterActivity.this, inputEmail);
                } else {
                    startActivity(new Intent(RegisterActivity.this, RegisterNameActivity.class));
                    finish();
                }
            }
        });
    }

    private class AdvancedTextWatcher implements TextWatcher
    {
        private boolean added;
        EditText e;

        public AdvancedTextWatcher(EditText e) {
            added = false;
            this.e = e;
        }

        public void addListener() {
            if (!added) {
                e.addTextChangedListener(this);
                added = true;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            passwordContainer.setPasswordVisibilityToggleEnabled(true);
            e.removeTextChangedListener(this);
            added = false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    }
}
