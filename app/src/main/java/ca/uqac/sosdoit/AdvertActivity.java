package ca.uqac.sosdoit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Bid;
import ca.uqac.sosdoit.data.Rating;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.BidAdapter;
import ca.uqac.sosdoit.util.RecyclerTouchListener;
import ca.uqac.sosdoit.util.Util;

public class AdvertActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private TextView descriptionInfo, completionDateInfo, workerRateInfo, workerCommentInfo, rateInfo, commentInfo, bidsInfo;
    private EditText description, status, postingDate, completionDate, workerRate, workerComment, inputComment;
    private Spinner inputRate;
    private Button btnRate;
    private RecyclerView bidsView;
    private BidAdapter bidsAdapter;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private DatabaseManager.ResultListener<Advert> advertListener;
    private DatabaseManager.ResultListener<ArrayList<Bid>> bidsListener;

    private Advert advert;
    private ArrayList<Bid> bids = new ArrayList<>();

    private String uid;
    private String aid;
    private String advertiser_uid;

    private Integer[] rates = new Integer[]{5, 4, 3, 2, 1, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

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
                startActivity(new Intent(AdvertActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(AdvertActivity.this, SettingsActivity.class));
            }
        });

        descriptionInfo = findViewById(R.id.aa_description_info);
        description = findViewById(R.id.aa_description);
        status = findViewById(R.id.aa_status);
        postingDate = findViewById(R.id.aa_posting_date);
        completionDateInfo = findViewById(R.id.aa_completion_date_info);
        completionDate = findViewById(R.id.aa_completion_date);

        workerRateInfo = findViewById(R.id.aa_worker_rate_info);
        workerRate = findViewById(R.id.aa_worker_rate);
        workerCommentInfo = findViewById(R.id.aa_worker_comment_info);
        workerComment = findViewById(R.id.aa_worker_comment);

        rateInfo = findViewById(R.id.aa_rate_info);
        inputRate = findViewById(R.id.aa_rate);
        commentInfo = findViewById(R.id.aa_comment_info);
        inputComment = findViewById(R.id.aa_comment);

        btnRate = findViewById(R.id.aa_btn_rate);

        bidsInfo = findViewById(R.id.aa_bids_info);
        bidsView = findViewById(R.id.aa_bids_view);

        progressBar = findViewById(R.id.progress_bar);

        btnRate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (advert.hasWorkerRating()) {
                    advert.getWorkerRating().setRate(rates[inputRate.getSelectedItemPosition()]);
                    advert.getWorkerRating().setCommentWithCheck(inputComment.getText().toString().trim());
                    db.setAdvert(advert);
                } else {
                    db.setAdvert(advert.setStatus(Advert.Status.RATED).setWorkerRating(new Rating(rates[inputRate.getSelectedItemPosition()]).setCommentWithCheck(inputComment.getText().toString().trim())));
                }
                if (inputComment.hasFocus()) {
                    Util.toggleKeyboard(AdvertActivity.this);
                }
            }
        });

        final ArrayAdapter<Integer> adapter = new ArrayAdapter<>(AdvertActivity.this, android.R.layout.simple_spinner_item, rates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputRate.setAdapter(adapter);

        Resources r = getResources();
        bidsAdapter = new BidAdapter(bids, new BidAdapter.ColorStatus().setAccepted(r.getColor(R.color.green)).setPending(r.getColor(R.color.white)).setRejected(r.getColor(R.color.red)));
        Util.initRecyclerView(AdvertActivity.this, bidsView);
        bidsView.setAdapter(bidsAdapter);

        bidsView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), bidsView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position)
            {
                if (advert != null && advert.getStatus() == Advert.Status.AVAILABLE) {
                    new AlertDialog.Builder(AdvertActivity.this)
                            .setTitle(R.string.msg_accept_bid)
                            .setMessage(Util.formatCurrency(bids.get(position).getOffer()))
                            .setCancelable(true)
                            .setPositiveButton(R.string.btn_accept_bid, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    HashMap<String, Bid> b = new HashMap<>();

                                    for (Bid bid : bids) {
                                        b.put(bid.getUid(), bid.reject());
                                    }
                                    bids.get(position).accept();

                                    db.setBids(advertiser_uid, aid, b).addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            db.setAdvert(advert.setStatus(Advert.Status.ACCEPTED).setWorkerUid(bids.get(position).getUid()));
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
            }

            @Override
            public void onLongClick(View view, final int position)
            {
                if (advert != null && (advert.getStatus() == Advert.Status.ACCEPTED && bids.get(position).getStatus() == Bid.Status.ACCEPTED || advert.getStatus() == Advert.Status.AVAILABLE)) {
                        new AlertDialog.Builder(AdvertActivity.this)
                            .setTitle(R.string.msg_reject_bid)
                            .setMessage(Util.formatCurrency(bids.get(position).getOffer()))
                            .setCancelable(true)
                            .setPositiveButton(R.string.btn_reject_bid, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    switch (advert.getStatus()) {
                                        case AVAILABLE:
                                            db.setBid(advertiser_uid, aid, bids.get(position).setStatus(Bid.Status.REJECTED));
                                            break;
                                        case ACCEPTED:
                                            HashMap<String, Bid> b = new HashMap<>();

                                            for (Bid bid : bids) {
                                                b.put(bid.getUid(), bid);
                                            }
                                            bids.get(position).reject();

                                            db.setBids(advertiser_uid, aid, b).addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    db.setAdvert(advert.setStatus(Advert.Status.AVAILABLE).setWorkerUid(null));
                                                }
                                            });
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }));

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
                } else {
                    uid = firebaseUser.getUid();
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
                    descriptionInfo.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                } else {
                    descriptionInfo.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                }

                status.setText(getString(advert.getStatus().value()));
                postingDate.setText(Util.formatDate(advert.getPostingDate()));

                if (advert.hasCompletionDate()) {
                    completionDate.setText(Util.formatDate(advert.getCompletionDate()));
                    completionDateInfo.setVisibility(View.VISIBLE);
                    completionDate.setVisibility(View.VISIBLE);
                } else {
                    completionDateInfo.setVisibility(View.GONE);
                    completionDate.setVisibility(View.GONE);
                }

                if ((advert.getStatus() == Advert.Status.COMPLETED || advert.getStatus() == Advert.Status.RATED)) {
                    bidsInfo.setVisibility(View.GONE);
                    bidsView.setVisibility(View.GONE);

                    if (advert.hasAdvertiserRating()) {
                        workerRate.setText(String.valueOf(advert.getAdvertiserRating().getRate()));
                        workerRateInfo.setVisibility(View.VISIBLE);
                        workerRate.setVisibility(View.VISIBLE);
                        if (advert.getAdvertiserRating().hasComment()) {
                            workerComment.setText(advert.getAdvertiserRating().getComment());
                            workerCommentInfo.setVisibility(View.VISIBLE);
                            workerComment.setVisibility(View.VISIBLE);
                        } else {
                            workerCommentInfo.setVisibility(View.GONE);
                            workerComment.setVisibility(View.GONE);
                        }
                    } else {
                        workerRateInfo.setVisibility(View.GONE);
                        workerRate.setVisibility(View.GONE);
                        workerCommentInfo.setVisibility(View.GONE);
                        workerComment.setVisibility(View.GONE);
                    }

                    if (advert.hasWorkerRating()) {
                        inputRate.setSelection(Arrays.asList(rates).indexOf(advert.getWorkerRating().getRate()));
                        if (advert.getWorkerRating().hasComment()) {
                            inputComment.setText(advert.getWorkerRating().getComment());
                        }
                    } else {
                        inputComment.requestFocus();
                        Util.toggleKeyboard(AdvertActivity.this);
                    }

                    rateInfo.setVisibility(View.VISIBLE);
                    inputRate.setVisibility(View.VISIBLE);
                    commentInfo.setVisibility(View.VISIBLE);
                    inputComment.setVisibility(View.VISIBLE);
                    btnRate.setVisibility(View.VISIBLE);
                } else {
                    bidsInfo.setVisibility(View.VISIBLE);
                    bidsView.setVisibility(View.VISIBLE);

                    workerRateInfo.setVisibility(View.GONE);
                    workerRate.setVisibility(View.GONE);
                    workerCommentInfo.setVisibility(View.GONE);
                    workerComment.setVisibility(View.GONE);

                    rateInfo.setVisibility(View.GONE);
                    inputRate.setVisibility(View.GONE);
                    commentInfo.setVisibility(View.GONE);
                    inputComment.setVisibility(View.GONE);
                    btnRate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure()
            {
                finish();
            }
        };

        bidsListener = new DatabaseManager.ResultListener<ArrayList<Bid>>()
        {
            @Override
            public void onSuccess(ArrayList<Bid> result)
            {
                bids.clear();
                bids.addAll(result);
                bidsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure()
            {
                bids.clear();
                bidsAdapter.notifyDataSetChanged();
                bidsInfo.setVisibility(View.GONE);
                bidsView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
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
        db.addBidsEventListener(uid, aid, bidsListener);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        auth.removeAuthStateListener(authListener);
        db.removeAdvertEventListener(aid, advertListener);
        db.removeBidsEventListener(uid, aid, bidsListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_advert, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean enabled = advert != null && advert.getStatus() == Advert.Status.ACCEPTED;

        MenuItem btnCompleted = menu.findItem(R.id.btn_completed_advert).setEnabled(enabled);

        SpannableString s = new SpannableString(btnCompleted.getTitle());

        if (!enabled) {
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey)), 0, s.length(), 0);
        } else {
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_grey)), 0, s.length(), 0);
        }

        btnCompleted.setTitle(s);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_completed_advert:
                db.setAdvert(advert.setStatus(Advert.Status.COMPLETED));
                break;
            case R.id.btn_edit_advert:
                startActivity(new Intent(AdvertActivity.this, EditAdvertActivity.class).putExtra(Util.UID, uid).putExtra(Util.AID, aid));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
