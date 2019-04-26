package org.feup.acmeeletronicsshop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Debug;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;

    private RequestQueue queue;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        SharedPreferences mPrefs = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);

        user = null;

        Gson gson = new Gson();
        String json = mPrefs.getString("currentUser", "");

        if (!json.equals("")) {
            user = gson.fromJson(json, User.class);
            Intent intent = new Intent(LoginActivity.this, ShoppingListActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user);
            intent.putExtras(b);
            startActivity(intent);

            finish();
        }

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

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:

                //verifyFromSQLite();
                loginAPI();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void loginAPI() {
        if(!validateFields())
            return;

        JSONObject json = new JSONObject();
        try {
            String email = "", password = "";
            email = textInputEditTextEmail.getText().toString();
            password = textInputEditTextPassword.getText().toString();
            json.put("email", email);
            json.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        String url = Utils.url + "/login";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("RESPONSE", "String Response : " + response.get("message").toString());
                            if ((response.get("message").toString()).equals("Success")) {

                                Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                                JSONObject data = response.getJSONObject("data");
                                JSONObject creditCard = response.getJSONObject("creditCard");
                                User user = new User(data.getString("name"), data.getString("address"), data.getString("email"),
                                        data.getString("password"), data.getString("fiscalNumber"), data.getString("publicKey"),
                                        creditCard.getString("number"), creditCard.getString("type"),
                                        creditCard.getString("validity"));
                                user.setId(data.getInt("idUser"));
                                SharedPreferences mPrefs = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
                                user.setPrivateKey(mPrefs.getString(user.getId() + "", ""));
                                    // Launch login activity
                                saveUser(user);
                                Intent intent = new Intent(LoginActivity.this, ShoppingListActivity.class);
                                emptyInputEditText();
                                Bundle b = new Bundle();
                                b.putSerializable("user", user);
                                intent.putExtras(b);
                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_valid_email_password, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        prefsEditor.apply();

    }


    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private boolean validateFields() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }

        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return false;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return false;
        }

        return true;
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
