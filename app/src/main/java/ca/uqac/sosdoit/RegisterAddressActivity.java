package ca.uqac.sosdoit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.database.DatabaseManager;
import ca.uqac.sosdoit.util.Util;

public class RegisterAddressActivity extends AppCompatActivity
{
    private EditText inputHouseNumber, inputStreet, inputCity, inputPostalCode, inputCountry;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);

        inputHouseNumber = findViewById(R.id.house_number);
        inputStreet = findViewById(R.id.street);
        inputCity = findViewById(R.id.city);
        inputPostalCode = findViewById(R.id.postal_code);
        inputCountry = findViewById(R.id.country);
        btnRegister = findViewById(R.id.btn_register_address);
        progressBar = findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
        db = DatabaseManager.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerAddress(v);
            }
        });

        inputCountry.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerAddress(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private void registerAddress(View v)
    {
        final String houseNumber = inputHouseNumber.getText().toString().trim();
        final String street = inputStreet.getText().toString().trim();
        final String city = inputCity.getText().toString().trim();
        final String postalCode = inputPostalCode.getText().toString().trim();
        final String country = inputCountry.getText().toString().trim();



        boolean exit = false;

        if (TextUtils.isEmpty(houseNumber)) {
            inputHouseNumber.setError(getString(R.string.msg_empty_house_number));
            inputHouseNumber.requestFocus();
            exit = true;
        }

        if (TextUtils.isEmpty(street)) {
            inputStreet.setError(getString(R.string.msg_empty_street));

            if (!exit) {
                inputStreet.requestFocus();
            }

            exit = true;
        }

        if (TextUtils.isEmpty(city)) {
            inputCity.setError(getString(R.string.msg_empty_city));

            if (!exit) {
                inputCity.requestFocus();
            }

            exit = true;
        }

        if (TextUtils.isEmpty(postalCode)) {
            inputPostalCode.setError(getString(R.string.msg_empty_postal_code));

            if (!exit) {
                inputPostalCode.requestFocus();
            }

            exit = true;
        }

        if (TextUtils.isEmpty(country)) {
            inputCountry.setError(getString(R.string.msg_empty_country));

            if (!exit) {
                inputCountry.requestFocus();
            }

            return;
        }

        if (exit) {
            return;
        }

        Util.hideKeyboard(RegisterAddressActivity.this, v);

        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            db.editAddressUser(user.getUid(), new Address(houseNumber, street, city, postalCode, country));
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(RegisterAddressActivity.this, ProfileActivity.class));
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }
}
