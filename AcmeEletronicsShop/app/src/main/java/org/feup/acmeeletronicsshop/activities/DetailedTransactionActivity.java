package org.feup.acmeeletronicsshop.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.feup.acmeeletronicsshop.R;

public class DetailedTransactionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

}
