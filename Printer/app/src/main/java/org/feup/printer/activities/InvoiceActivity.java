package org.feup.printer.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.feup.printer.R;
import org.feup.printer.models.Transaction;
import org.feup.printer.models.User;



public class InvoiceActivity extends AppCompatActivity {
    User user;
    Transaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        user = (User) getIntent().getSerializableExtra("user");
        transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        AppCompatTextView name = (AppCompatTextView) findViewById(R.id.txtName);
        AppCompatTextView email = (AppCompatTextView) findViewById(R.id.txtEmail);
        AppCompatTextView address = (AppCompatTextView) findViewById(R.id.txtAddress);
        AppCompatTextView fiscalNumber = (AppCompatTextView) findViewById(R.id.txtFiscalNumber);
        name.setText(user.getName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        fiscalNumber.setText(user.getFiscalNumber());

        
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
