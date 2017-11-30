package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class NewAdvertActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText inputTitle, inputDescription;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_new_advert);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.btn_profile).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(NewAdvertActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(NewAdvertActivity.this, SettingsActivity.class));
            }
        });

        inputTitle = findViewById(R.id.naa_title);
        inputDescription = findViewById(R.id.naa_description);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.naa_btn_post).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                post();
            }
        });

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    finish();
                } else {
                    uid = firebaseUser.getUid();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void post()
    {
        final String title = inputTitle.getText().toString().trim();
        final String description = inputDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            inputTitle.setError(getString(R.string.msg_empty_title));
            return;
        }

        Util.toggleKeyboard(NewAdvertActivity.this);
        progressBar.setVisibility(View.VISIBLE);

        if (uid != null) {
            db.setAdvert(new Advert(uid, title).setDescriptionWithCheck(description))
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
                            progressBar.setVisibility(View.GONE);
                            Log.d("SOS DO IT", e.getMessage());
                            Toast.makeText(NewAdvertActivity.this, getString(R.string.msg_new_advert_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                            Util.toggleKeyboard(NewAdvertActivity.this);
                        }
                    });
        }
    }
}
