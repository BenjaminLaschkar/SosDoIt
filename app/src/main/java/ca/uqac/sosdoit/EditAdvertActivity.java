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

public class EditAdvertActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText inputTitle, inputDescription;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private Advert advert;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advert);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
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
                startActivity(new Intent(EditAdvertActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditAdvertActivity.this, SettingsActivity.class));
            }
        });

        inputTitle = findViewById(R.id.eaa_title);
        inputDescription = findViewById(R.id.eaa_description);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.eaa_btn_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        findViewById(R.id.eaa_btn_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                save();
            }
        });

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        authListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    finish();
                }
            }
        };

        db.getAdvert(getIntent().getStringExtra(Util.AID), new DatabaseManager.Result<Advert>()
        {
            @Override
            public void onSuccess(Advert result)
            {
                advert = result;
                toolbar.setTitle(advert.getTitle());
                inputTitle.setText(advert.getTitle());
                if (advert.hasDescription()) {
                    inputDescription.setText(advert.getDescription());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure()
            {
                finish();
            }
        });
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

    public void save()
    {
        if (advert != null) {
            final String title = inputTitle.getText().toString().trim();
            final String description = inputDescription.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                inputTitle.setError(getString(R.string.msg_empty_title));
                return;
            }

            Util.toggleKeyboard(EditAdvertActivity.this);
            progressBar.setVisibility(View.VISIBLE);

            db.setAdvert(advert.setTitle(title).setDescriptionWithCheck(description))
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
                            Toast.makeText(EditAdvertActivity.this, getString(R.string.msg_edit_advert_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                            Util.toggleKeyboard(EditAdvertActivity.this);
                        }
                    });
        }
    }
}
