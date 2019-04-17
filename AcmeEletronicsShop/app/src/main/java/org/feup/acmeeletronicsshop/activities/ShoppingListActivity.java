package org.feup.acmeeletronicsshop.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.adapters.ProductsRecyclerAdapter;
import org.feup.acmeeletronicsshop.helpers.RequestQueueSingleton;
import org.feup.acmeeletronicsshop.model.Product;
import org.feup.acmeeletronicsshop.sql.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppCompatActivity activity = ShoppingListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewProducts;
    private List<Product> listProducts;
    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Button b = findViewById(R.id.btnPay);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingListActivity.this,PayPopup.class));
            }
        });

        NavigationView navView = findViewById(R.id.nav_view);
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);

        initViews();
        initObjects();
        initDrawer();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
//        bundle.putCharSequence("Message", message.getText());
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
//        message.setText(bundle.getCharSequence("Message"));
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewProducts = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewProducts.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerViewProducts.addItemDecoration(dividerItemDecoration);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listProducts = new ArrayList<>();
        getProducts();
        listProducts.add(new Product(1, "prod1", "model", "maker", "red", "description", 10));
        listProducts.add(new Product(2, "prod2", "model", "maker", "red", "description", 10));
        productsRecyclerAdapter = new ProductsRecyclerAdapter(listProducts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewProducts.setLayoutManager(mLayoutManager);
        recyclerViewProducts.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setAdapter(productsRecyclerAdapter);
//        databaseHelper = new DatabaseHelper(activity);

//        String emailFromIntent = getIntent().getStringExtra("EMAIL");
//        textViewName.setText(emailFromIntent);

//        getDataFromSQLite();
    }

    private void initDrawer() {

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public boolean onNavigationItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case  R.id.nav_item_profile:

                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case  R.id.nav_item_shopping_list:
                break;
            case  R.id.nav_item_history:
                intent = new Intent(this, TransactionHistoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case  R.id.nav_item_logout:
                break;
            default:
                break;
        }

        return true;
    }

    public void scan() {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                act.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        byte[] baMess = null;

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                try {
                    baMess = contents.getBytes("ISO-8859-1");
                }
                catch (Exception ex) {
                }
                //message.setText("Format: " + format + "\nMessage: " + contents + "\n\nHex: " + byteArrayToHex(baMess));
            }
        }
    }

    String byteArrayToHex(byte[] ba) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for(byte b: ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnAdd) {
            scan();
        }else if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getProducts() {

        RequestQueue queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();


        String url = "http://6d10e24a.ngrok.io/shoppinglist/5";

        JsonObjectRequest productsRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                           JSONArray products = response.getJSONArray("products");
                           for(int i = 0; i < products.length(); i++){
                               JSONObject product = products.getJSONObject(i);
                               int id = product.getInt("idProduct");
                               String name = "name";
                               String model = product.getString("model");
                               String maker = product.getString("maker");
                               String color = product.getString("color");
                               String description = product.getString("description");
                               int price = product.getInt("price");
                               listProducts.add(new Product(id, model, model, maker, color, description, price));

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


        queue.add(productsRequest);

    }
}
