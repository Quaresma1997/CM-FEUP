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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.helpers.Utils;
import org.feup.acmeeletronicsshop.model.User;

public class TransactionHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        user = (User) getIntent().getSerializableExtra("user");

        initDrawer();


        NavigationView navView = findViewById(R.id.nav_view);
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
}
