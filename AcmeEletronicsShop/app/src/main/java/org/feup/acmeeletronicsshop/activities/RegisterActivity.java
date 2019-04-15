package org.feup.acmeeletronicsshop.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.helpers.InputValidation;
import org.feup.acmeeletronicsshop.helpers.RequestQueueSingleton;
import org.feup.acmeeletronicsshop.model.User;
import org.feup.acmeeletronicsshop.server.API;
import org.feup.acmeeletronicsshop.sql.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private API api = new API();
    private User user;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

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
    private DatabaseHelper databaseHelper;

    private String url;

    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();

        url = "http://6d10e24a.ngrok.io";

        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();


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
        databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                submitForm();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                //postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }


    private void submitForm() {
        int selected = radioGroupInputCreditCardType.getCheckedRadioButtonId();
        String type;
        if(selected == R.id.rdButtonInputCreditCardTypeVisa){
            type = "visa";
        }
        else{
            type = "mastercard";
        }

        /*
        registerUser(textInputEditTextName.getText().toString(), textInputEditTextEmail.getText().toString(), textInputEditTextPassword.getText().toString(), textInputEditTextAddress.getText().toString(), textInputEditTextFiscalNumber.getText().toString(), textInputEditTextCreditCardNumber.getText().toString(), textInputEditTextCreditCardValidity.getText().toString(),type);
         */





        //final RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

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
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

        JSONObject json = new JSONObject();
        try {
            json.put("name",textInputEditTextName.getText().toString());
            json.put("email", textInputEditTextEmail.getText().toString());
            json.put("password",textInputEditTextConfirmPassword.getText().toString());
            json.put("address",textInputEditTextAddress.getText().toString());
            json.put("fiscalNumber",textInputEditTextFiscalNumber.getText().toString());
            json.put("publicKey",pub.toString());
            json.put("cardType",type);
            json.put("cardNumber", textInputEditTextCreditCardNumber.getText().toString());
            json.put("cardExpiration", textInputEditTextCreditCardValidity.getText().toString());




        } catch (JSONException e) {
            e.printStackTrace();
        }

        String register_url = url + "/register";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, register_url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", "String Response : "+ response.toString());
                        Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                        emptyInputEditText();

                        Toast.makeText(getApplicationContext(), "Welcome! You're signed in.", Toast.LENGTH_SHORT).show();
                        try {
                            if(response.get("message").equals("User and card successfully added"));
                            Intent intent = new Intent(
                                    RegisterActivity.this,
                                    ShoppingListActivity.class);
                            //intent.putExtra("user", (Serializable) user);
                            startActivity(intent);
                            finish();

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

/**
    private void registerUser(final String name,  final String email, final String password,
                              final String address, final String nif, final String number,
                              final String validity, final String type) {
        try {
            //Log.i(TAG, "enrr");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyPairGenerator.initialize(368, random);

            // generate a keypair
            KeyPair pair = keyPairGenerator.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();

            String[] str = new String[10];
            user = new User(name, address, email, password, nif, pub.toString(), priv.toString(), type, number, validity);
            str[0] = "register";
            str[1] = email;
            str[2] = name;
            str[3] = password;
            str[4] = nif;
            str[5] = address;
            str[6] = type;
            str[7] = number;
            str[8] = validity;
            str[9] = pub.toString();
            new registerAPI().execute(str);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
/**
    private class registerAPI extends AsyncTask<String, Void, String> {

        private String name = "";

        public registerAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "register":
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("email", strings[1]);
                    map.put("name", strings[2]);
                    name = strings[2];
                    map.put("password", strings[3]);
                    map.put("nif", strings[4]);
                    map.put("address", strings[5]);
                    map.put("type", strings[6]);
                    map.put("number", strings[7]);
                    map.put("validity", strings[8]);
                    map.put("publicKey", strings[9]);

                    JSONObject us = new JSONObject(map);
                    return api.registerUser(us);
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //Log.i(TAG, s);
            if (s.equals("Error")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            } else {
                user.saveObject(getFilesDir());

                Toast.makeText(getApplicationContext(), "Hi " + name + ", You are successfully Added!", Toast.LENGTH_SHORT).show();

                // Launch login activity
                Intent intent = new Intent(
                        RegisterActivity.this,
                        ShoppingListActivity.class);
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
                finish();
            }
        }
    }





    /**
     * This method is to validate the input text fields and post data to SQLite
     */
 /**   private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}