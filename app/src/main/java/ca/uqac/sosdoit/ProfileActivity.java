package ca.uqac.sosdoit;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class ProfileActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText inputEmail, inputPassword, inputNewPassword, inputOldPassword;
    private EditText inputRegistration, inputUsername, inputFirstName, inputLastName;
    private EditText inputHouseNumber, inputStreet, inputAdditionalAddress, inputCity, inputState, inputPostalCode, inputCountry;
    private TextView passwordDescription;
    private LinearLayout buttons;
    private Button btnCancel, btnUpdate;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private DatabaseManager db;
    private DatabaseManager.ResultListener<User> userListener;
    private User user;

    private SharedPreferences pref;

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
        inputNewPassword = findViewById(R.id.new_password);
        inputOldPassword = findViewById(R.id.old_password);
        passwordDescription = findViewById(R.id.password_description);

        inputRegistration = findViewById(R.id.registration);
        inputUsername = findViewById(R.id.username);
        inputFirstName = findViewById(R.id.first_name);
        inputLastName = findViewById(R.id.last_name);

        inputHouseNumber = findViewById(R.id.house_number);
        inputStreet = findViewById(R.id.street);
        inputAdditionalAddress = findViewById(R.id.additional_address);
        inputCity = findViewById(R.id.city);
        inputState = findViewById(R.id.state);
        inputPostalCode = findViewById(R.id.postal_code);
        inputCountry = findViewById(R.id.country);

        buttons = findViewById(R.id.buttons);
        btnCancel = findViewById(R.id.cancel);
        btnUpdate = findViewById(R.id.update);

        progressBar = findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    finish();
                } else if (user == null) {
                    db.getUser(firebaseUser.getUid(), new DatabaseManager.Result<User>()
                    {
                        @Override
                        public void onSuccess(User result)
                        {
                            user = result.setEmail(firebaseUser.getEmail());
                            db.addUserEventListener(user.getUid(), userListener);
                        }
                    });
                }
            }
        };

        userListener = new DatabaseManager.ResultListener<User>()
        {
            @Override
            public void onSuccess(User result)
            {
                user = result.setEmail(user.getEmail());
                showProfile();
            }
        };

        inputEmail.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editMode) {
                    if (TextUtils.isEmpty(editable)) {
                        inputPassword.setVisibility(View.GONE);
                    } else if (TextUtils.isEmpty(inputNewPassword.getText())) {
                        inputPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    inputPassword.setVisibility(View.GONE);
                }
            }
        });

        inputNewPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editMode) {
                    if (TextUtils.isEmpty(editable)) {
                        inputPassword.setText(inputOldPassword.getText());
                        if (!TextUtils.isEmpty(inputEmail.getText().toString().trim())) {
                            inputPassword.setVisibility(View.VISIBLE);
                        }
                        inputOldPassword.setVisibility(View.GONE);
                    } else {
                        inputPassword.setVisibility(View.GONE);
                        if (!TextUtils.isEmpty(inputPassword.getText())) {
                            inputOldPassword.setText(inputPassword.getText());
                            inputPassword.setText("");
                        }
                        inputOldPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    inputOldPassword.setVisibility(View.GONE);
                }
            }
        });

        inputCountry.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    save();
                    return true;
                }
                return false;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchMode();
                inputEmail.setError(null);
                inputNewPassword.setError(null);
                inputUsername.setError(null);
                inputStreet.setError(null);
                inputCity.setError(null);
                inputPostalCode.setError(null);
                inputCountry.setError(null);
                Util.toggleKeyboard(ProfileActivity.this);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
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
        if (user != null) {
            db.addUserEventListener(user.getUid(), userListener);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        auth.removeAuthStateListener(authListener);
        if (user != null) {
            db.removeUserEventListener(user.getUid(), userListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem btnDelete = menu.findItem(R.id.btn_delete_account).setEnabled(editMode);

        SpannableString s = new SpannableString(btnDelete.getTitle());

        if (!editMode) {
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey)), 0, s.length(), 0);
        } else {
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, s.length(), 0);
        }

        btnDelete.setTitle(s);

        return super.onPrepareOptionsMenu(menu);
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
                Util.toggleKeyboard(ProfileActivity.this);
                break;
            case R.id.btn_delete_account:
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle(R.string.msg_delete_account)
                        .setMessage(R.string.msg_delete_account_warning)
                        .setCancelable(true)
                        .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                switchMode();
                                Util.toggleKeyboard(ProfileActivity.this);
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

    public void showProfile()
    {
        inputRegistration.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(user.getRegistrationDate()));

        inputFirstName.setText(user.hasFirstName() ? user.getFirstName() : "");
        inputLastName.setText(user.hasLastName() ? user.getLastName() : "");

        if (user.hasAddress()) {
            Address address = user.getAddress();

            inputHouseNumber.setText(address.hasHouseNumber() ? address.getHouseNumber() : "");
            inputStreet.setText(address.getStreet());
            inputAdditionalAddress.setText(address.hasAdditionalAddress() ? address.getAdditionalAddress() : "");
            inputCity.setText(address.getCity());
            inputState.setText(address.hasState() ? address.getState() : "");
            inputPostalCode.setText(address.getPostalCode());
            inputCountry.setText(address.getCountry());

            if (!editMode) {
                if (!address.hasHouseNumber()) {
                    inputHouseNumber.setVisibility(View.GONE);
                }
                if (!address.hasAdditionalAddress()) {
                    inputAdditionalAddress.setVisibility(View.GONE);
                }
                if (!address.hasState()) {
                    inputState.setVisibility(View.GONE);
                }
            }
        } else {
            inputHouseNumber.setText("");
            inputStreet.setText("");
            inputAdditionalAddress.setText("");
            inputCity.setText("");
            inputState.setText("");
            inputPostalCode.setText("");
            inputCountry.setText("");

            if (!editMode) {
                inputHouseNumber.setVisibility(View.GONE);
                inputAdditionalAddress.setVisibility(View.GONE);
                inputState.setVisibility(View.GONE);
            }
        }

        if (editMode) {
            inputEmail.setHint(user.getEmail());
            inputEmail.setText("");
            inputUsername.setHint(user.getUsername());
            inputUsername.setText("");
        } else {
            inputEmail.setText(user.getEmail());
            inputUsername.setText(user.getUsername());
        }

        progressBar.setVisibility(View.GONE);
    }

    public void switchMode()
    {
        editMode = !editMode;

        setEditTextEditable(inputEmail, editMode);
        setEditTextEditable(inputUsername, editMode);
        setEditTextEditable(inputFirstName, editMode);
        setEditTextEditable(inputLastName, editMode);
        setEditTextEditable(inputHouseNumber, editMode);
        setEditTextEditable(inputStreet, editMode);
        setEditTextEditable(inputAdditionalAddress, editMode);
        setEditTextEditable(inputState, editMode);
        setEditTextEditable(inputCity, editMode);
        setEditTextEditable(inputPostalCode, editMode);
        setEditTextEditable(inputCountry, editMode);

        if (editMode) {
            passwordDescription.setVisibility(View.VISIBLE);
            inputNewPassword.setVisibility(View.VISIBLE);

            inputHouseNumber.setVisibility(View.VISIBLE);
            inputAdditionalAddress.setVisibility(View.VISIBLE);
            inputState.setVisibility(View.VISIBLE);

            buttons.setVisibility(View.VISIBLE);

            inputEmail.requestFocus();
        } else {
            passwordDescription.setVisibility(View.GONE);
            inputPassword.setVisibility(View.GONE);
            inputNewPassword.setVisibility(View.GONE);
            inputOldPassword.setVisibility(View.GONE);

            buttons.setVisibility(View.GONE);
        }

        showProfile();
    }

    public void setEditTextEditable(EditText editText, boolean editable)
    {
        editText.setFocusable(editable);
        editText.setFocusableInTouchMode(editable);
        editText.setClickable(editable);
        editText.setLongClickable(editable);
        editText.setCursorVisible(editable);
    }

    public void save()
    {
        final String email = inputEmail.getText().toString().trim();
        final String password = TextUtils.isEmpty(inputPassword.getText()) ? inputOldPassword.getText().toString() : inputPassword.getText().toString();
        final String newPassword = inputNewPassword.getText().toString();

        final String username = inputUsername.getText().toString().trim();
        final String firstName = inputFirstName.getText().toString().trim();
        final String lastName = inputLastName.getText().toString().trim();

        final String houseNumber = inputHouseNumber.getText().toString().trim();
        final String street = inputStreet.getText().toString().trim();
        final String additionalAddress = inputAdditionalAddress.getText().toString().trim();
        final String city = inputCity.getText().toString().trim();
        final String state = inputState.getText().toString().trim();
        final String postalCode = inputPostalCode.getText().toString().trim();
        final String country = inputCountry.getText().toString().trim();

        final boolean updateEmail = !TextUtils.isEmpty(email);
        final boolean updatePassword = !TextUtils.isEmpty(newPassword);
        final boolean updateUsername = !TextUtils.isEmpty(username);
        boolean error = false;

        if ((updateEmail || updatePassword) && TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.msg_empty_password));
            inputOldPassword.setError(getString(R.string.msg_empty_old_password));
            error = true;
        }

        if (updatePassword && newPassword.length() < 6) {
            inputNewPassword.setError(getString(R.string.msg_minimum_password));
            error = true;
        }

        if (!TextUtils.isEmpty(houseNumber) || !TextUtils.isEmpty(street) || !TextUtils.isEmpty(additionalAddress) || !TextUtils.isEmpty(city) || !TextUtils.isEmpty(state) || !TextUtils.isEmpty(postalCode) || !TextUtils.isEmpty(country)) {
            if (TextUtils.isEmpty(street)) {
                inputStreet.setError(getString(R.string.msg_empty_street));
                error = true;
            }
            if (TextUtils.isEmpty(city)) {
                inputCity.setError(getString(R.string.msg_empty_city));
                error = true;
            }
            if (TextUtils.isEmpty(postalCode)) {
                inputPostalCode.setError(getString(R.string.msg_empty_postal_code));
                error = true;
            }
            if (TextUtils.isEmpty(country)) {
                inputCountry.setError(getString(R.string.msg_empty_country));
                error = true;
            }
        }

        final User newUser = new User(user);

        newUser.setUsernameWithCheck(username);
        newUser.setFirstNameWithCheck(firstName);
        newUser.setLastNameWithCheck(lastName);
        newUser.setHouseNumberWithCheck(houseNumber);
        newUser.setStreetWithCheck(street);
        newUser.setAdditionalAddressWithCheck(additionalAddress);
        newUser.setCityWithCheck(city);
        newUser.setStateWithCheck(state);
        newUser.setPostalCodeWithCheck(postalCode);
        newUser.setCountryWithCheck(country);
        newUser.clearAddressIfEmpty();

        if (!error) {
            Util.toggleKeyboard(ProfileActivity.this);
            progressBar.setVisibility(View.VISIBLE);

            if (updateEmail || updatePassword) {
                final FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser == null) {
                    finish();
                    return;
                }
                firebaseUser.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), password)).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (updateEmail) {
                            firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (!task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        try {
                                            if (task.getException() != null) {
                                                throw task.getException();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, getString(R.string.msg_edit_profile_failed) + ": " + getString(R.string.msg_unknown_error), Toast.LENGTH_LONG).show();
                                            }
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            inputEmail.setError(getString(R.string.msg_invalid_email));
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            inputEmail.setError(getString(R.string.msg_email_already_used));
                                        } catch (Exception e) {
                                            Toast.makeText(ProfileActivity.this, getString(R.string.msg_edit_profile_failed) + ": " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                        if (!editMode) {
                                            switchMode();
                                            inputEmail.setText(email);
                                            Util.toggleKeyboard(ProfileActivity.this);
                                        }
                                    } else {
                                        user.setEmail(email);
                                        showProfile();
                                        pref.edit().putString(getString(R.string.pref_email), email).apply();
                                    }
                                }
                            });
                        }
                        if (updatePassword) {
                            firebaseUser.updatePassword(newPassword);
                        }
                    }
                });
            }
            if (!newUser.equals(user)) {
                if (updateUsername) {
                    db.isUsernameUnique(username, new DatabaseManager.Result<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            db.setUsername(username, user.getUid(), new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful()) {
                                        db.removeUsername(user.getUsername());
                                        db.setUser(newUser);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure()
                        {
                            progressBar.setVisibility(View.GONE);
                            inputUsername.setError(getString(R.string.msg_username_already_used));
                            if (!editMode) {
                                switchMode();
                                inputUsername.setText(username);
                                inputUsername.requestFocus();
                                Util.toggleKeyboard(ProfileActivity.this);
                            }
                        }
                    });
                } else {
                    db.setUser(newUser);
                }
            }

            switchMode();
        }
    }
}
