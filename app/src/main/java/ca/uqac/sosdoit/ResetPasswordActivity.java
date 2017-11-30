package ca.uqac.sosdoit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import ca.uqac.sosdoit.util.Util;

public class ResetPasswordActivity extends AppCompatActivity
{
    private EditText inputEmail;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = findViewById(R.id.rpa_email);
        progressBar = findViewById(R.id.progress_bar);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.rpa_btn_reset_password).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reset_password();
            }
        });

        findViewById(R.id.rpa_btn_login).setOnClickListener(new View.OnClickListener()
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

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
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
                        } catch (FirebaseAuthInvalidUserException ex) {
                            inputEmail.setError(getString(R.string.msg_email_not_found));
                        } catch (FirebaseException ex) {
                            inputEmail.setError(getString(R.string.msg_email_invalid));
                        } catch (Exception ex) {
                            Toast.makeText(ResetPasswordActivity.this, getString(R.string.msg_reset_password_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                        }
                        inputEmail.requestFocus();
                        Util.toggleKeyboard(ResetPasswordActivity.this);
                    }
                });
    }
}
