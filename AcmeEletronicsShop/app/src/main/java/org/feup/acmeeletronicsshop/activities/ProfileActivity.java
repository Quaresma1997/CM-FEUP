package org.feup.acmeeletronicsshop.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.feup.acmeeletronicsshop.R;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        initDrawer();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);

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

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case  R.id.nav_item_profile:
                break;
            case  R.id.nav_item_shopping_list:
                intent = new Intent(this, ShoppingListActivity.class);
                startActivity(intent);
                finish();
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
}
