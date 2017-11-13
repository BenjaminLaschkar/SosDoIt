package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MyAdvertsActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private ImageButton btnProfile, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adverts);

        toolbar = findViewById(R.id.toolbar);
        btnProfile = findViewById(R.id.btn_profile);
        btnSettings = findViewById(R.id.btn_settings);

        toolbar.setTitle(R.string.btn_my_adverts);
        setSupportActionBar(toolbar);

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
