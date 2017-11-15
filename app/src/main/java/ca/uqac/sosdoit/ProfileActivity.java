package ca.uqac.sosdoit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.database.IDatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class ProfileActivity extends AppCompatActivity implements IDatabaseManager.UserResult
{
    private Toolbar toolbar;
    private EditText inputEmail, inputPassword, inputUsername, inputFirstName, inputLastName, inputHouseNumber, inputStreet, inputAdditionalAddress, inputCity, inputPostalCode, inputCountry;
    private TextView passwordDescription;
    private LinearLayout buttons;
    private Button btnCancel, btnSave;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseManager db;

    private boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_profile);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        passwordDescription = findViewById(R.id.password_description);

        inputUsername = findViewById(R.id.username);
        inputFirstName = findViewById(R.id.first_name);
        inputLastName = findViewById(R.id.last_name);

        inputHouseNumber = findViewById(R.id.house_number);
        inputStreet = findViewById(R.id.street);
        inputAdditionalAddress = findViewById(R.id.additional_address);
        inputCity = findViewById(R.id.city);
        inputPostalCode = findViewById(R.id.postal_code);
        inputCountry = findViewById(R.id.country);

        buttons = findViewById(R.id.buttons);
        btnCancel = findViewById(R.id.cancel);
        btnSave = findViewById(R.id.save);

        progressBar = findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    if (editMode) {
                        inputEmail.setHint(user.getEmail());
                    } else {
                        inputEmail.setText(user.getEmail());
                    }

                    db.getUser(user.getUid(), ProfileActivity.this);
                }
            }
        };

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchMode();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                save();
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_edit_profile:
                switchMode();
                break;
            case R.id.btn_delete_account:
                new AlertDialog.Builder(this)
                        .setTitle("Delete account?")
                        .setMessage("(This operation can't be undone!)")
                        .setCancelable(true)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void call(User user)
    {
        if (editMode) {
            inputUsername.setHint(user.hasUsername() ? user.getUsername() : getString(R.string.username));
            inputFirstName.setHint(user.hasFirstName() ? user.getFirstName() : getString(R.string.first_name));
            inputLastName.setHint(user.hasLastName() ? user.getLastName() : getString(R.string.last_name));

            if (user.hasAddress()) {
                Address address = user.getAddress();

                inputHouseNumber.setHint(address.hasHouseNumber() ? address.getHouseNumber() : getString(R.string.house_number));
                inputStreet.setHint(address.getStreet());
                inputAdditionalAddress.setHint(address.hasAdditionalAddress() ? address.getAdditionalAddress() : getString(R.string.additional_address));
                inputCity.setHint(address.getCity());
                inputPostalCode.setHint(address.getPostalCode());
                inputCountry.setHint(address.getCountry());
            } else {
                inputHouseNumber.setHint(getString(R.string.house_number));
                inputStreet.setHint(getString(R.string.street));
                inputAdditionalAddress.setHint(getString(R.string.additional_address));
                inputCity.setHint(getString(R.string.city));
                inputPostalCode.setHint(getString(R.string.postal_code));
                inputCountry.setHint(getString(R.string.country));
            }
        } else {
            if (user.hasUsername()) {
                inputUsername.setText(user.getUsername());
            }
            if (user.hasFirstName()) {
                inputFirstName.setText(user.getFirstName());
            }
            if (user.hasLastName()) {
                inputLastName.setText(user.getLastName());
            }
            if (user.hasAddress()) {
                Address address = user.getAddress();

                if (address.hasHouseNumber()) {
                    inputHouseNumber.setText(address.getHouseNumber());
                    inputHouseNumber.setVisibility(View.VISIBLE);
                } else {
                    inputHouseNumber.setVisibility(View.GONE);
                }

                inputStreet.setText(address.getStreet());

                if (address.hasAdditionalAddress()) {
                    inputAdditionalAddress.setText(address.getAdditionalAddress());
                    inputAdditionalAddress.setVisibility(View.VISIBLE);
                } else {
                    inputAdditionalAddress.setVisibility(View.GONE);
                }

                inputCity.setText(address.getCity());
                inputPostalCode.setText(address.getPostalCode());
                inputCountry.setText(address.getCountry());
            }
        }

        progressBar.setVisibility(View.GONE);
    }

    public void switchMode()
    {
        editMode = !editMode;

        setEditTextEditable(inputEmail, editMode);
        setEditTextEditable(inputPassword, editMode);
        setEditTextEditable(inputUsername, editMode);
        setEditTextEditable(inputFirstName, editMode);
        setEditTextEditable(inputLastName, editMode);
        setEditTextEditable(inputHouseNumber, editMode);
        setEditTextEditable(inputStreet, editMode);
        setEditTextEditable(inputAdditionalAddress, editMode);
        setEditTextEditable(inputCity, editMode);
        setEditTextEditable(inputPostalCode, editMode);
        setEditTextEditable(inputCountry, editMode);

        if (editMode) {
            passwordDescription.setVisibility(View.VISIBLE);
            inputPassword.setHint(getString(R.string.password));
            inputPassword.setVisibility(View.VISIBLE);

            inputHouseNumber.setVisibility(View.VISIBLE);
            inputAdditionalAddress.setVisibility(View.VISIBLE);

            buttons.setVisibility(View.VISIBLE);

            inputEmail.requestFocus();
        } else {
            passwordDescription.setVisibility(View.GONE);
            inputPassword.setVisibility(View.GONE);

            buttons.setVisibility(View.GONE);

            inputEmail.setText(user.getEmail());
        }

        db.getUser(user.getUid(), this);

        Util.toggleKeyboard(ProfileActivity.this);
    }

    public void setEditTextEditable(EditText e, boolean editable)
    {
        e.setFocusable(editable);
        e.setFocusableInTouchMode(editable);
        e.setClickable(editable);
        e.setLongClickable(editable);
        e.setCursorVisible(editable);

        if (editable) {
            e.setHint(e.getText());
            e.setText("");
        } else {
            e.setHint("");
        }
    }

    public void save()
    {
        switchMode();
    }

    public void saveEmail()
    {

    }

    public void savePassword()
    {

    }

    public void saveUsername()
    {

    }

    public void saveFullName()
    {

    }

    public void saveAddress()
    {

    }
}
