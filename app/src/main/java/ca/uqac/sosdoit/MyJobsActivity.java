package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.JobAdapter;
import ca.uqac.sosdoit.util.RecyclerTouchListener;
import ca.uqac.sosdoit.util.Util;

public class MyJobsActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private RecyclerView jobsView;
    private JobAdapter jobAdapter;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private DatabaseManager.ResultListener<ArrayList<String>> jobsListener;

    private ArrayList<Advert> jobs = new ArrayList<>();
    private String uid;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.btn_my_jobs);
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
                startActivity(new Intent(MyJobsActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyJobsActivity.this, SettingsActivity.class));
            }
        });

        jobsView = findViewById(R.id.mja_jobs_view);
        progressBar = findViewById(R.id.progress_bar);

        jobAdapter = new JobAdapter(jobs, new JobAdapter.ColorStatus(getResources().getColor(R.color.green), getResources().getColor(R.color.orange), getResources().getColor(R.color.red)));
        Util.initRecyclerView(MyJobsActivity.this, jobsView);
        jobsView.setAdapter(jobAdapter);

        jobsView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), jobsView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(MyJobsActivity.this, JobActivity.class).putExtra(Util.UID, uid).putExtra(Util.AID, jobs.get(position).getAid()).putExtra(Util.ADVERTISER_UID, jobs.get(position).getAdvertiserUid()));
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                }
            }
        };

        jobsListener = new DatabaseManager.ResultListener<ArrayList<String>>()
        {
            @Override
            public void onSuccess(final ArrayList<String> result)
            {
                progressBar.setVisibility(View.VISIBLE);
                jobs.clear();

                db.getJobs(result, uid, new DatabaseManager.ResultSynced<Advert>(result.size())
                {
                    @Override
                    public void onSuccess(Advert advert)
                    {
                        jobs.add(advert);
                        sync();
                    }

                    @Override
                    public void onFailure()
                    {
                        sync();
                    }

                    @Override
                    public void onSynced()
                    {
                        jobAdapter.notifyDataSetChanged();
                        jobsView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        };

        uid = getIntent().getStringExtra(Util.UID);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        auth.addAuthStateListener(authListener);
        db.addJobsEventListener(uid, jobsListener);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        auth.removeAuthStateListener(authListener);
        db.addJobsEventListener(uid, jobsListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
