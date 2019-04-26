package org.feup.acmeeletronicsshop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.adapters.ProductsRecyclerAdapter;
import org.feup.acmeeletronicsshop.adapters.TransactionItemRecyclerAdapter;
import org.feup.acmeeletronicsshop.adapters.TransactionRecyclerAdapter;
import org.feup.acmeeletronicsshop.helpers.RequestQueueSingleton;
import org.feup.acmeeletronicsshop.helpers.Utils;
import org.feup.acmeeletronicsshop.model.Product;
import org.feup.acmeeletronicsshop.model.Transaction;
import org.feup.acmeeletronicsshop.model.TransactionItem;
import org.feup.acmeeletronicsshop.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerViewTransactions;
    private List<Transaction> transactions;
    private TransactionRecyclerAdapter transactionRecyclerAdapter;
    RequestQueue queue;

    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        user = (User) getIntent().getSerializableExtra("user");

        queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        initDrawer();

        recyclerViewTransactions = (RecyclerView) findViewById(R.id.recyclerViewTransactions);

        transactions = new ArrayList<>();

        transactionRecyclerAdapter = new TransactionRecyclerAdapter(transactions);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTransactions.setLayoutManager(mLayoutManager);
        recyclerViewTransactions.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTransactions.setHasFixedSize(true);
        recyclerViewTransactions.setAdapter(transactionRecyclerAdapter);

        transactionRecyclerAdapter.setOnItemClickListener(new TransactionRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                showDetailedTransaction(position);
            };
        });

        NavigationView navView = findViewById(R.id.nav_view);
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getTransactions();
    }

    private void initDrawer() {

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navView = findViewById(R.id.nav_view);
        View mHeaderView = navView.getHeaderView(0);
        TextView txtDrawerName = (TextView) mHeaderView.findViewById(R.id.txtDrawerName);
        txtDrawerName.setText(user.getName());

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item){
        Intent intent;
        Bundle b;
        switch (item.getItemId()){
            case  R.id.nav_item_profile:
                intent = new Intent(this, ProfileActivity.class);
                b = new Bundle();
                b.putSerializable("user", user);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                break;
            case  R.id.nav_item_shopping_list:
                intent = new Intent(this, ShoppingListActivity.class);
                b = new Bundle();
                b.putSerializable("user", user);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                break;
            case  R.id.nav_item_history:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case  R.id.nav_item_logout:
                SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);

                // Writing data to SharedPreferences
                SharedPreferences.Editor prefsEditor = settings.edit();
                prefsEditor.putString("currentUser", "");
                prefsEditor.apply();

                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

        return true;
    }


    public void getTransactions() {

        String url = Utils.url + "/transaction/previous/" + user.getId();

        JsonObjectRequest transactionsRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jTransactions = response.getJSONArray("result");
                            for(int i = 0; i < jTransactions.length(); i++){
                                int totalCost = 0;

                                List<TransactionItem> tItems = new ArrayList<TransactionItem>();

                                JSONObject transaction = jTransactions.getJSONObject(i);

                                String day = transaction.getString("day");
                                SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                                Date date = formatter.parse(day);

                                String token = transaction.getString("idOrder");

                                JSONArray products = transaction.getJSONArray("products");
                                for(int j = 0; j < products.length(); j++){
                                    JSONObject product = products.getJSONObject(j);
                                    int idProduct = product.getInt("idProduct");
                                    int quantity = product.getInt("quantity");
                                    String name = product.getString("name");

                                    totalCost += product.getInt("price");

                                    TransactionItem item = new TransactionItem(idProduct, quantity, name);
                                    tItems.add(item);
                                }

                                Transaction t = new Transaction(token, date, totalCost, tItems);
                                transactions.add(t);
                                transactionRecyclerAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(transactionsRequest);
    }


    public void showDetailedTransaction(int position){
        Transaction t = transactions.get(position);
        Intent intent = new Intent(this, DetailedTransactionActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("transaction", t);
        intent.putExtras(b);
        startActivity(intent);
    }
}
