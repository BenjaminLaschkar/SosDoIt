package ca.uqac.sosdoit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.uqac.sosdoit.database.DatabaseManager;


public class RegisterAddressActivity extends AppCompatActivity {

    private EditText inputAdress, inputPostalCode, inputCountry;
    private Button submitRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_adress);
        inputAdress = (EditText) findViewById(R.id.address);
        inputPostalCode = (EditText) findViewById(R.id.postal_code);
        inputCountry = (EditText) findViewById(R.id.country);
        submitRegister = (Button) findViewById(R.id.btn_register_address);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adress = inputAdress.getText().toString().trim();
                String postalCode = inputPostalCode.getText().toString().trim();
                String country = inputCountry.getText().toString().trim();

                if (TextUtils.isEmpty(adress)) {
                    Toast.makeText(getApplicationContext(), "Enter adress !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(postalCode)) {
                    Toast.makeText(getApplicationContext(), "Enter postalCode !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(country)) {
                    Toast.makeText(getApplicationContext(), "Enter country !", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.GONE);
                auth = FirebaseAuth.getInstance();

                final FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
                String id = firebaseuser.getUid();

                String fullAdress = adress + " " + postalCode + " " + country;

                DatabaseManager.getInstance().editAddressUser(id,fullAdress);

                startActivity(new Intent(RegisterAddressActivity.this, ProfileActivity.class));
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
