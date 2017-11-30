package ca.uqac.sosdoit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class ChooseUsernameActivity extends AppCompatActivity
{
    private EditText inputUsername;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_username);

        inputUsername = findViewById(R.id.cua_username);
        progressBar = findViewById(R.id.progress_bar);

        inputUsername.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    choose();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.cua_btn_choose).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                choose();
            }
        });

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        authListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    finish();
                }
            }
        };
    }

    @Override
    public void onResume()
    {
        super.onResume();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        auth.removeAuthStateListener(authListener);
    }

    private void choose()
    {
        final String username = inputUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            inputUsername.setError(getString(R.string.msg_empty_username));
            return;
        }

        Util.toggleKeyboard(ChooseUsernameActivity.this);
        progressBar.setVisibility(View.VISIBLE);

        if (firebaseUser != null) {
            db.isUsernameUnique(username, new DatabaseManager.Result<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    db.setUsername(username, firebaseUser.getUid())
                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    db.setUser(new User(firebaseUser.getUid(), username))
                                            .addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener()
                                            {
                                                @Override
                                                public void onFailure(@NonNull Exception e)
                                                {
                                                    Log.d("SOS DO IT", e.getMessage());
                                                    Toast.makeText(ChooseUsernameActivity.this, getString(R.string.msg_choose_username_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                                                    db.removeUsername(username).addOnCompleteListener(new OnCompleteListener<Void>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            progressBar.setVisibility(View.GONE);
                                                            Util.toggleKeyboard(ChooseUsernameActivity.this);
                                                        }
                                                    });
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("SOS DO IT", e.getMessage());
                                    Toast.makeText(ChooseUsernameActivity.this, getString(R.string.msg_choose_username_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                                }
                            });
                }

                @Override
                public void onFailure()
                {
                    progressBar.setVisibility(View.GONE);
                    inputUsername.setError(getString(R.string.msg_username_already_used));
                    inputUsername.requestFocus();
                    Util.toggleKeyboard(ChooseUsernameActivity.this);
                }
            });
        }
    }
}
