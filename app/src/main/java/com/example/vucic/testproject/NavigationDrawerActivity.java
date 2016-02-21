package com.example.vucic.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnFragmentInteractionListener {

    private SharedPreferences preferences;
    private ApiRequests apiRequests = new ApiRequests();
    private Toolbar toolbar;
    private static final String TAG = "NavigationDrawer";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        preferences = this.getSharedPreferences("stuff", Context.MODE_PRIVATE);
        Log.i("Email:", email = preferences.getString("email", ""));

        handleWeeklySchedule();

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
        //TODO: opet mrtvi null pointer exception za ovaj prokleti textView?!?!!?!?!?!?! jedno vreme je radilo i onda prestade
        //TODO: kad sam dodao fragment.. ne kapiram leba mi
//        emailTextView.setText(email);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_weekly_schedule) {
            handleWeeklySchedule();
        } else if (id == R.id.nav_yearly_calendar) {
            handleYearlyCalendar();
        } else if (id == R.id.nav_logout) {
            handleLogout();
        } else if (id == R.id.nav_notifications) {
            handleNotifications();
        } else if (id == R.id.nav_settings) {
            handleSettings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean logout(String accessToken, String deviceToken) {
        apiRequests.deactivateDeviceToken(deviceToken, accessToken);
        return apiRequests.logout(accessToken);
    }

    private void setPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accessToken", "");
        editor.putString("slug", "");
        editor.putInt("userId", 0);
        editor.apply();
    }

    private void handleWeeklySchedule() {
        //TODO: implement the method
        Log.i(TAG, "Entering weekly schedule view.");

        toolbar.setTitle(R.string.weeklySchedule);
    }

    private void handleYearlyCalendar() {
        //TODO: implement the method
        Log.i(TAG, "Entering yearly calendar view.");

        toolbar.setTitle(R.string.yearlyCalender);
    }

    private void handleNotifications() {
        //TODO: implement the method
        Log.i(TAG, "Entering notifications view.");

        toolbar.setTitle(R.string.notifications);
    }

    private void handleSettings() {
        Log.i(TAG, "Entering settings view.");

        Fragment fragment = new SettingsFragment();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_navigation_drawer, fragment)
                .commit();

        toolbar.setTitle(R.string.settings);
    }


    private void handleLogout() {
        final Thread logout = new Thread(new Runnable() {
            @Override
            public void run() {
                String accessToken = preferences.getString("accessToken", "");
                String deviceToken = preferences.getString("deviceToken", "");
                logout(accessToken, deviceToken);
                setPreferences();
                finish();
                Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        logout.start();
        finish();
        Log.i(TAG, "Logging out.");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
