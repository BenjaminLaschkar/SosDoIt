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

public class BidActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText description, postingDate, offerText, inputOffer;
    private TextView descriptionInfo;
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
        setContentView(R.layout.activity_bid);

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
                startActivity(new Intent(BidActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(BidActivity.this, SettingsActivity.class));
            }
        });

        descriptionInfo = findViewById(R.id.ba_description_info);
        description = findViewById(R.id.ba_description);
        postingDate = findViewById(R.id.ba_posting_date);
        offerText = findViewById(R.id.ba_offer_text);
        inputOffer = findViewById(R.id.ba_offer_value);
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

        findViewById(R.id.ba_btn_bid).setOnClickListener(new View.OnClickListener()
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
                    descriptionInfo.setVisibility(View.GONE);
                    descriptionInfo.setVisibility(View.GONE);
                } else {
                    descriptionInfo.setVisibility(View.GONE);
                    descriptionInfo.setVisibility(View.GONE);
                }
                postingDate.setText(Util.formatDate(advert.getPostingDate()));
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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure()
            {
                progressBar.setVisibility(View.GONE);
                Util.toggleKeyboard(BidActivity.this);
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

        Util.toggleKeyboard(BidActivity.this);
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
