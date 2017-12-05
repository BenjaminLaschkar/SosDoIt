package ca.uqac.sosdoit;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.AdvertAdapter;
import ca.uqac.sosdoit.util.RecyclerTouchListener;
import ca.uqac.sosdoit.util.Util;

public class MyAdvertsActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private RecyclerView advertsView;
    private AdvertAdapter advertAdapter;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private DatabaseManager.ResultListener<ArrayList<Advert>> advertListener;

    private ArrayList<Advert> adverts = new ArrayList<>();
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adverts);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_my_adverts);
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
                startActivity(new Intent(MyAdvertsActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAdvertsActivity.this, SettingsActivity.class));
            }
        });

        advertsView = findViewById(R.id.maa_adverts_view);
        progressBar = findViewById(R.id.progress_bar);

        Resources r = getResources();
        advertAdapter = new AdvertAdapter(MyAdvertsActivity.this, adverts, new AdvertAdapter.ColorStatus(r.getColor(R.color.white), r.getColor(R.color.yellow), r.getColor(R.color.orange), r.getColor(R.color.green), r.getColor(R.color.red), r.getColor(R.color.red)));
        Util.initRecyclerView(MyAdvertsActivity.this, advertsView);
        advertsView.setAdapter(advertAdapter);

        advertsView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), advertsView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(MyAdvertsActivity.this, AdvertActivity.class).putExtra(Util.UID, uid).putExtra(Util.AID, adverts.get(position).getAid()).putExtra(Util.ADVERTISER_UID, adverts.get(position).getAdvertiserUid()));
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

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

        advertListener = new DatabaseManager.ResultListener<ArrayList<Advert>>()
        {
            @Override
            public void onSuccess(ArrayList<Advert> result)
            {
                adverts.clear();
                adverts.addAll(result);
                advertAdapter.notifyDataSetChanged();
                advertsView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure()
            {
                advertsView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };

        uid = getIntent().getStringExtra(Util.UID);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        auth.addAuthStateListener(authListener);
        db.addAdvertsEventListener(uid, advertListener);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        auth.removeAuthStateListener(authListener);
        db.removeAdvertsEventListener(advertListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_adverts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_new_advert:
                startActivity(new Intent(MyAdvertsActivity.this, NewAdvertActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
