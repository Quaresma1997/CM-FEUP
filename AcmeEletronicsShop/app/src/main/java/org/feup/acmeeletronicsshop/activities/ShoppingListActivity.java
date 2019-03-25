package org.feup.acmeeletronicsshop.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.adapters.ProductsRecyclerAdapter;
import org.feup.acmeeletronicsshop.model.Product;
import org.feup.acmeeletronicsshop.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    private AppCompatActivity activity = ShoppingListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewProducts;
    private List<Product> listProducts;
    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        initViews();
        initObjects();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewProducts = (RecyclerView) findViewById(R.id.recyclerViewProducts);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listProducts = new ArrayList<>();
        listProducts.add(new Product(1, "prod1", "model", "maker", "red", "description", 10 ));
        listProducts.add(new Product(2, "prod2", "model", "maker", "red", "description", 10 ));
        productsRecyclerAdapter = new ProductsRecyclerAdapter(listProducts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewProducts.setLayoutManager(mLayoutManager);
        recyclerViewProducts.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setAdapter(productsRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);

//        String emailFromIntent = getIntent().getStringExtra("EMAIL");
//        textViewName.setText(emailFromIntent);

//        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                listUsers.clear();
//                listUsers.addAll(databaseHelper.getAllUser());
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                productsRecyclerAdapter.notifyDataSetChanged();
//            }
//        }.execute();


    }
}
