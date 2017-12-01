package ca.uqac.sosdoit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Bid;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class JobActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText description, postingDate, completionDate, offerText, inputOffer, houseNumber, street, additionalAddress, city, state, postalCode, country;
    private TextView descriptionInfo, completionDateInfo, addressInfo;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private DatabaseManager.ResultListener<Advert> advertListener;
    private DatabaseManager.ResultListener<Bid> bidListener;

    private Advert advert;
    private Bid bid;

    private String uid;
    private String aid;
    private String advertiser_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

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
                startActivity(new Intent(JobActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(JobActivity.this, SettingsActivity.class));
            }
        });

        descriptionInfo = findViewById(R.id.ja_description_info);
        description = findViewById(R.id.ja_description);
        postingDate = findViewById(R.id.ja_posting_date);
        completionDateInfo = findViewById(R.id.ja_completion_date_info);
        completionDate = findViewById(R.id.ja_completion_date);
        offerText = findViewById(R.id.ja_offer_text);
        inputOffer = findViewById(R.id.ja_offer_value);

        addressInfo = findViewById(R.id.ja_address_info);
        houseNumber = findViewById(R.id.ja_house_number);
        street = findViewById(R.id.ja_street);
        additionalAddress = findViewById(R.id.ja_additional_address);
        city = findViewById(R.id.ja_city);
        state = findViewById(R.id.ja_state);
        postalCode = findViewById(R.id.ja_postal_code);
        country = findViewById(R.id.ja_country);

        progressBar = findViewById(R.id.progress_bar);

        inputOffer.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    bid();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.ja_btn_bid).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bid();
            }
        });

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        authListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                }
            }
        };

        advertListener = new DatabaseManager.ResultListener<Advert>()
        {
            @Override
            public void onSuccess(Advert result)
            {
                advert = result;
                toolbar.setTitle(advert.getTitle());
                if (advert.hasDescription()) {
                    description.setText(advert.getDescription());
                    description.setVisibility(View.VISIBLE);
                    descriptionInfo.setVisibility(View.VISIBLE);
                } else {
                    description.setVisibility(View.GONE);
                    descriptionInfo.setVisibility(View.GONE);
                }
                postingDate.setText(Util.formatDate(advert.getPostingDate()));
                if (advert.hasCompletionDate()) {
                    completionDate.setText(Util.formatDate(advert.getCompletionDate()));
                    completionDate.setVisibility(View.VISIBLE);
                    completionDateInfo.setVisibility(View.VISIBLE);
                } else {
                    completionDate.setVisibility(View.GONE);
                    completionDateInfo.setVisibility(View.GONE);
                }

                inputOffer.setVisibility(advert.getStatus() == Advert.Status.AVAILABLE ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailure()
            {
                finish();
            }
        };

        bidListener = new DatabaseManager.ResultListener<Bid>()
        {
            @Override
            public void onSuccess(Bid result)
            {
                bid = result;
                offerText.setText(Util.formatCurrency(bid.getOffer()));
                offerText.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure()
            {
                offerText.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Util.toggleKeyboard(JobActivity.this);
            }
        };

        uid = getIntent().getStringExtra(Util.UID);
        aid = getIntent().getStringExtra(Util.AID);
        advertiser_uid = getIntent().getStringExtra(Util.ADVERTISER_UID);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        auth.addAuthStateListener(authListener);
        db.addAdvertEventListener(aid, advertListener);
        db.addBidEventListener(advertiser_uid, aid, uid, bidListener);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        auth.removeAuthStateListener(authListener);
        db.removeAdvertEventListener(advert.getAid(), advertListener);
        db.removeBidEventListener(advertiser_uid, aid, uid, bidListener);
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

    public void bid()
    {
        String s = inputOffer.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            inputOffer.setError(getString(R.string.msg_empty_bid));
            return;
        }
        double offer;
        try {
            offer = Double.valueOf(s);
        } catch (NumberFormatException e) {
            inputOffer.setError(getString(R.string.msg_bid_not_number));
            return;
        }

        Util.toggleKeyboard(JobActivity.this);
        progressBar.setVisibility(View.VISIBLE);

        if (bid == null) {
            db.setBid(advertiser_uid, aid, new Bid(uid, offer)).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    db.setJob(uid, advert.getAid()).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        } else {
            db.setBid(advertiser_uid, aid, bid.setOffer(offer)).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
