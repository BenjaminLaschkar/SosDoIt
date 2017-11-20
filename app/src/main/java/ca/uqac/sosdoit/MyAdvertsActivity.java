package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.database.IDatabaseManager;
import ca.uqac.sosdoit.util.AdvertAdapter;
import ca.uqac.sosdoit.util.RecyclerTouchListener;

public class MyAdvertsActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private ImageButton btnProfile, btnSettings;
    private RecyclerView recyclerView;
    private AdvertAdapter advertAdapter;
    private List<Advert> advertList = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseManager db;
    private FloatingActionButton button_adding_advert;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adverts);

        toolbar = findViewById(R.id.toolbar);
        btnProfile = findViewById(R.id.btn_profile);
        btnSettings = findViewById(R.id.btn_settings);
        db = DatabaseManager.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        advertAdapter = new AdvertAdapter(advertList);
        toolbar.setTitle(R.string.btn_my_adverts);
        setSupportActionBar(toolbar);
        button_adding_advert = findViewById(R.id.button_add_advert);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(advertAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Advert advert = advertList.get(position);
                Toast.makeText(getApplicationContext(), advert.getDescription() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareAdvertData();



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAdvertsActivity.this, ProfileActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAdvertsActivity.this, SettingsActivity.class));
            }
        });
        button_adding_advert.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAdvertsActivity.this, SettingsActivity.class));
            }
        });
    }

    private void prepareAdvertData() {

        /*Advert advert = new Advert(Task.TEST,"Ceci est un test",new Address("test-street","test-city","test-postal","test-country"), AdvertStatus.AVAILABLE, 1, "test-idAdvertiser","test-idworker");
        advert.setCreationDate(new Date());
        advertList.add(advert);
        advert  = new Advert(Task.BABYSITTING,"Besoin d'un babysitting pour 22h",new Address("211","Boulevard Talbot","Chicoutimi","G7H2K9","Canada"), AdvertStatus.AVAILABLE, 20, "idAdvertiser 2","idworker 2");
        advert.setCreationDate(new Date());
        advertList.add(advert);
        advert  = new Advert(Task.TEST,"Déblayer la neige",new Address("rue Marie-Victorin","Chicoutimi","J8B7Y6","Canada"), AdvertStatus.AVAILABLE, 10, "test-idAdvertiser 3","test-idworker 3");
        advert.setCreationDate(new Date());
        advertList.add(advert);
        */

        final IDatabaseManager.AdvertListResult advertListResult = new IDatabaseManager.AdvertListResult() {
            @Override
            public void call(List<Advert> advertListForThisUser) {
                advertList.addAll(advertListForThisUser);
            }
        };

        String idAdvertiser = auth.getCurrentUser().getUid();
        db.getAllAdvertsPublished(idAdvertiser,advertListResult);

        advertAdapter.notifyDataSetChanged();
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
