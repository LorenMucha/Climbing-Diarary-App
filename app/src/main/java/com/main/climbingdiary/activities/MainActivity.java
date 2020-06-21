package com.main.climbingdiary.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.AppFileProvider;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.TimeSlider;
import com.main.climbingdiary.controller.button.AppFloatingActionButton;
import com.main.climbingdiary.controller.button.ShowTimeSlider;


public class MainActivity extends AppCompatActivity{

    private static final int layoutId = R.layout.activity_main;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static ComponentName componentName;
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity mainActivity;

    public static Context getMainAppContext() {
        return MainActivity.context;
    }
    public static ComponentName getMainComponentName(){ return MainActivity.componentName;}
    public static AppCompatActivity getMainActivity(){return MainActivity.mainActivity;};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainActivity.context = getApplicationContext();
        MainActivity.componentName = getComponentName();
        MainActivity.mainActivity = this;

        //manager for the tabs
        new FragmentPager(this);

        //the add buttons
        new ShowTimeSlider();
        //new AppFloatingActionButton();
        //the slider
        new TimeSlider();
        new AppFloatingActionButton();
        //navigation View
        //new NavDrawer(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.app_settings){
            Intent intent = new Intent(MainActivity.getMainAppContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getMainActivity().startActivity(intent);
        }
        return false;
    }
}