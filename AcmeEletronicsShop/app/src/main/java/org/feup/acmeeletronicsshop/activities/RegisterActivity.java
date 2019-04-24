package org.feup.acmeeletronicsshop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.helpers.InputValidation;
import org.feup.acmeeletronicsshop.helpers.RequestQueueSingleton;
import org.feup.acmeeletronicsshop.helpers.Utils;
import org.feup.acmeeletronicsshop.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private User user;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputLayout textInputLayoutFiscalNumber;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutCreditCardNumber;
    private TextInputLayout textInputLayoutCreditCardValidity;


    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private TextInputEditText textInputEditTextFiscalNumber;
    private TextInputEditText textInputEditTextAddress;
    private TextInputEditText textInputEditTextCreditCardNumber;
    private TextInputEditText textInputEditTextCreditCardValidity;

    private RadioGroup radioGroupInputCreditCardType;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;

    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();

        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputLayoutFiscalNumber = (TextInputLayout) findViewById(R.id.textInputLayoutFiscalNumber);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.textInputLayoutAddress);
        textInputLayoutCreditCardNumber = (TextInputLayout) findViewById(R.id.textInputLayoutCreditCardNumber);
        textInputLayoutCreditCardValidity = (TextInputLayout) findViewById(R.id.textInputLayoutCreditCardValidity);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        textInputEditTextAddress = (TextInputEditText) findViewById(R.id.textInputEditTextAddress);
        textInputEditTextFiscalNumber = (TextInputEditText) findViewById(R.id.textInputEditTextFiscalNumber);
        textInputEditTextCreditCardNumber = (TextInputEditText) findViewById(R.id.textInputEditTextCreditCardNumber);
        textInputEditTextCreditCardValidity = (TextInputEditText) findViewById(R.id.textInputEditTextCreditCardValidity);


        radioGroupInputCreditCardType = (RadioGroup) findViewById((R.id.radioGroupInputCreditCardType));

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(!validateFields())
            return;
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                submitForm();
                break;

            case R.id.appCompatTextViewLoginLink:
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;
        }
    }


    private void submitForm() {
        int selected = radioGroupInputCreditCardType.getCheckedRadioButtonId();
        final String type;
        if (selected == R.id.rdButtonInputCreditCardTypeVisa) {
            type = "visa";
        } else {
            type = "mastercard";
        }




        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyPairGenerator.initialize(368, random);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // generate a keypair
        KeyPair pair = keyPairGenerator.generateKeyPair();
        final PrivateKey priv = pair.getPrivate();
        final PublicKey pub = pair.getPublic();
        final String name = textInputEditTextName.getText().toString();
        final String password = textInputEditTextPassword.getText().toString();
        final String address = textInputEditTextAddress.getText().toString();
        final String email = textInputEditTextEmail.getText().toString();
        final String fiscalNumber = textInputEditTextFiscalNumber.getText().toString();
        final String cardNumber = textInputEditTextCreditCardNumber.getText().toString();
        final String cardExpiration = textInputEditTextCreditCardValidity.getText().toString();


        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("email", email);
            json.put("password", password);
            json.put("address", address);
            json.put("fiscalNumber", fiscalNumber);
            json.put("publicKey", pub.toString());
            json.put("cardType", type);
            json.put("cardNumber", cardNumber);
            json.put("cardExpiration", cardExpiration);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String register_url = Utils.url + "/register";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, register_url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", "String Response : " + response.toString());
                        Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                        emptyInputEditText();

                        try {

                            if (response.get("message").equals("Success")) {
                                Toast.makeText(getApplicationContext(), "Welcome! You're signed in!", Toast.LENGTH_SHORT).show();
                                user = new User(name, address, email, password, fiscalNumber, pub.toString(), priv.toString(), cardNumber, type, cardExpiration);
                                user.setId(response.getInt("id"));
                                saveUser(user);
                                Intent intent = new Intent(RegisterActivity.this, ShoppingListActivity.class);
                                Bundle b = new Bundle();
                                b.putSerializable("user", user);
                                intent.putExtras(b);
                                startActivity(intent);
                                finish();
                            }else {
                                // Snack Bar to show error message that record already exists
                                Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Launch login activity


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);

    }

    private void saveUser(User user) {
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);

        // Writing data to SharedPreferences
        SharedPreferences.Editor prefsEditor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("currentUser", json);
        prefsEditor.putString(user.getId() + "", user.getPrivateKey());
        prefsEditor.apply();

    }


    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private boolean validateFields() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return false;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return false;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextAddress, textInputLayoutAddress, getString(R.string.error_message_address))) {
            return false;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextFiscalNumber, textInputLayoutFiscalNumber, getString(R.string.error_message_fiscal_number))) {
            return false;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextCreditCardNumber, textInputLayoutCreditCardNumber, getString(R.string.error_message_credit_card_number))) {
            return false;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextCreditCardValidity, textInputLayoutCreditCardValidity, getString(R.string.error_message_credit_card_validity))) {
            return false;
        }

        if(!inputValidation.hasInputEditTextGivenLength(9, textInputEditTextFiscalNumber, textInputLayoutFiscalNumber, getString(R.string.error_message_fiscal_number))){
            return false;
        }

        if(!inputValidation.hasInputEditTextGivenLength(8, textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))){
            return false;
        }

        if(!inputValidation.hasInputEditTextGivenLength(12, textInputEditTextCreditCardNumber, textInputLayoutCreditCardNumber, getString(R.string.error_message_credit_card_number))){
            return false;
        }

        if(!inputValidation.isValidDate(textInputEditTextCreditCardValidity, textInputLayoutCreditCardValidity, getString(R.string.error_message_credit_card_validity))){
            return false;
        }

        return true;


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
        textInputEditTextAddress.setText(null);
        textInputEditTextCreditCardNumber.setText(null);
        textInputEditTextFiscalNumber.setText(null);
        textInputEditTextCreditCardValidity.setText(null);
    }
}