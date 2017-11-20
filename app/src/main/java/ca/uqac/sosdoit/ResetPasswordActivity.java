package ca.uqac.sosdoit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import ca.uqac.sosdoit.util.Util;

public class ResetPasswordActivity extends AppCompatActivity
{
    private EditText inputEmail;
    private Button btnResetPassword, btnLogIn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = findViewById(R.id.email);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        btnLogIn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        auth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reset_password();
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

        inputEmail.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    reset_password();
                    return true;
                }
                return false;
            }
        });
    }

    private void reset_password()
    {
        final String email = inputEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.msg_empty_email));
            return;
        }

        Util.toggleKeyboard(ResetPasswordActivity.this);

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        if (task.getException() != null) {
                            throw task.getException();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, getString(R.string.msg_reset_password_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                        }
                    } catch (FirebaseAuthInvalidUserException e) {
                        inputEmail.setError(getString(R.string.msg_email_not_found));
                    } catch (FirebaseException e) {
                        inputEmail.setError(getString(R.string.msg_invalid_email));
                    } catch (Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.msg_reset_password_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                    }
                    inputEmail.requestFocus();
                    Util.toggleKeyboard(ResetPasswordActivity.this);
                } else {
                    pref.edit().putString(getString(R.string.pref_email), email).apply();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
