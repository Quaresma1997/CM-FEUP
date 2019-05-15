package org.feup.printer.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.feup.printer.R;
import org.feup.printer.adapters.TransactionItemRecyclerAdapter;
import org.feup.printer.models.Transaction;
import org.feup.printer.models.User;

import java.util.ArrayList;
import java.util.List;


public class InvoiceActivity extends AppCompatActivity {
    User user;
    Transaction transaction;
    List items;
    RecyclerView recyclerViewTransactionItems;
    TransactionItemRecyclerAdapter transactionItemRecyclerAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        user = (User) getIntent().getSerializableExtra("user");
        transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        items = transaction.getItemlist();
        initViews();




        transactionItemRecyclerAdaper = new TransactionItemRecyclerAdapter(items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTransactionItems.setLayoutManager(mLayoutManager);
        recyclerViewTransactionItems.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTransactionItems.setHasFixedSize(true);
        recyclerViewTransactionItems.setAdapter(transactionItemRecyclerAdaper);

        
    }

    public void initViews(){
        AppCompatTextView name = (AppCompatTextView) findViewById(R.id.txtName);
        AppCompatTextView email = (AppCompatTextView) findViewById(R.id.txtEmail);
        AppCompatTextView address = (AppCompatTextView) findViewById(R.id.txtAddress);
        AppCompatTextView fiscalNumber = (AppCompatTextView) findViewById(R.id.txtFiscalNumber);
        name.setText(user.getName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        fiscalNumber.setText(user.getFiscalNumber());

        AppCompatTextView uuid = (AppCompatTextView) findViewById(R.id.txtUUID);
        AppCompatTextView date = (AppCompatTextView) findViewById(R.id.txtDate);
        AppCompatTextView totalCost = (AppCompatTextView) findViewById(R.id.txtTotalCost);
        uuid.setText(transaction.getToken());
        date.setText(transaction.getDate().toString());
        totalCost.setText("" + transaction.getTotalCost() );

        recyclerViewTransactionItems = (RecyclerView) findViewById(R.id.recyclerViewTransactionItems);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewTransactionItems.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerViewTransactionItems.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
