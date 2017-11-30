package ca.uqac.sosdoit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import ca.uqac.sosdoit.util.Util;

public class RegisterActivity extends AppCompatActivity
{
    private EditText inputEmail, inputPassword;
    private TextInputLayout passwordContainer;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    private SharedPreferences pref;

    private Util.AdvancedTextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.ra_password);
        passwordContainer = findViewById(R.id.ra_password_container);
        progressBar = findViewById(R.id.progress_bar);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        textWatcher = new Util.AdvancedTextWatcher(inputPassword, passwordContainer);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.ra_btn_register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                register();
            }
        });

        findViewById(R.id.ra_btn_login).setOnClickListener(new View.OnClickListener()
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
                    register();
                    return true;
                }
                return false;
            }
        });
    }

    private void register()
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
                textWatcher.setErrorAndListener(getString(R.string.msg_empty_password));
            } else {
                textWatcher.setErrorAndListener(getString(R.string.msg_minimum_password));
            }

            if (!exit) {
                inputPassword.requestFocus();
            }

            return;
        }

        if (exit) {
            return;
        }

        Util.toggleKeyboard(RegisterActivity.this);

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        pref.edit().putString(getString(R.string.pref_email), email).apply();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        progressBar.setVisibility(View.GONE);
                        Log.d("SOS DO IT", e.getMessage());
                        try {
                            throw e;
                        } catch (FirebaseAuthInvalidCredentialsException ex) {
                            inputEmail.setError(getString(R.string.msg_email_invalid));
                        } catch (FirebaseAuthUserCollisionException ex) {
                            inputEmail.setError(getString(R.string.msg_email_already_used));
                        } catch (Exception ex) {
                            Toast.makeText(RegisterActivity.this, getString(R.string.msg_register_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                        }
                        inputEmail.requestFocus();
                        Util.toggleKeyboard(RegisterActivity.this);
                    }
                });
    }
}
